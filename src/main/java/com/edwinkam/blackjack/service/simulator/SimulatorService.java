package com.edwinkam.blackjack.service.simulator;

import com.edwinkam.blackjack.cache.SimulatorProgressCache;
import com.edwinkam.blackjack.model.game.GameRecord;
import com.edwinkam.blackjack.model.game.GameResult;
import com.edwinkam.blackjack.model.game.PlayerAction;
import com.edwinkam.blackjack.model.game.PlayerHand;
import com.edwinkam.blackjack.model.poker.Card;
import com.edwinkam.blackjack.model.poker.Deck;
import com.edwinkam.blackjack.model.poker.Hand;
import com.edwinkam.blackjack.model.simulator.SimulatorRequest;
import com.edwinkam.blackjack.model.simulator.SimulatorResponse;
import com.edwinkam.blackjack.model.strategy.CustomPlayerBetStrategy;
import com.edwinkam.blackjack.model.strategy.GetPlayerBetRequest;
import com.edwinkam.blackjack.provider.DeckProvider;
import com.edwinkam.blackjack.service.strategy.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimulatorService {
    @Autowired
    private StrategyService strategyService;
    @Autowired
    private DeckProvider deckProvider;
    @Autowired
    private SimulatorProgressCache simulatorProgressCache;

    private int NUM_OF_DECK = 2;
    private double CUT_OFF = 0.7;
    private double BASE_BET = 1;

    public SimulatorResponse simulate(SimulatorRequest request) throws Exception {
        GetPlayerBetRequest betRequest = request.getGetPlayerBetRequest();
        Deck deck = deckProvider.newDeck(NUM_OF_DECK);
        double playerAsset = 0;
        List<GameRecord> gameRecords = new ArrayList<>();
        for (int currGame = 1; currGame <= request.getNumOfGame(); currGame++) {
            double playerOriginalAsset = playerAsset;
            if (deck.percentageUsed() > CUT_OFF) {
                // RESHUFFLE
                deck.shuffle();
            }

            double gameBet = BASE_BET;
            if (request.isUseRunningCount()) {
                betRequest.setCurrentRunningCount(deck.getTrueRunningCount());
                gameBet = strategyService.getPlayerBet(betRequest);
//                System.out.printf("count: %d, bet: %f\n", deck.getTrueRunningCount(), gameBet);
            }
            PlayerHand playerFirstHand = new PlayerHand(gameBet);
            List<PlayerHand> playerHands = new ArrayList<>();
            playerHands.add(playerFirstHand);
            Hand dealer = new Hand();

            playerFirstHand.add(deck.next());
            dealer.add(deck.next());
            playerFirstHand.add(deck.next());
            dealer.add(deck.next());

            GameResult resultIfSomeoneHadBlackjack = null;

            if (playerFirstHand.hasBlackjack() && dealer.hasBlackjack()) {
                // push
                resultIfSomeoneHadBlackjack = GameResult.PUSH;
            } else if (playerFirstHand.hasBlackjack()) {
                // player win 1.5
                resultIfSomeoneHadBlackjack = GameResult.PLAYER_BLACKJACK;
            } else if (dealer.hasBlackjack()) {
                // dealer win 1
                resultIfSomeoneHadBlackjack = GameResult.DEALER_WIN;
            } else {
                // PLAYER start to play
                boolean atLeastOneHandNoBust = false;
                for (int i = 0; i < playerHands.size(); i++) {
                    PlayerHand playerCurrentHand = playerHands.get(i);
                    PlayerAction action;
                    boolean firstAction = true;
                    while (true) {
                        if (playerCurrentHand.getHardSum() > 21) {
                            break;
                        }

                        action = strategyService.getPlayerAction(playerCurrentHand, dealer);
                        if (action == PlayerAction.DOUBLE && !firstAction) {
                            action = PlayerAction.HIT;
                        }

                        if (action == PlayerAction.STAND) {
                            break;
                        } else if (action == PlayerAction.DOUBLE) {
                            playerCurrentHand.doubleBet();
                            playerCurrentHand.add(deck.next());
                            break;
                        } else if (action == PlayerAction.HIT) {
                            playerCurrentHand.add(deck.next());
                        } else if (action == PlayerAction.SPLIT) {
                            Card firstCard = playerCurrentHand.getCard(0);
                            Card secondCard = playerCurrentHand.getCard(1);

                            PlayerHand newHand = new PlayerHand(playerCurrentHand.getBet());

                            // split two hand, assign one new card to each
                            playerCurrentHand.setHand(firstCard, deck.next());
                            newHand.setHand(secondCard, deck.next());

                            playerHands.add(newHand);
                        }
                        firstAction = false;
                    }
                    if (playerCurrentHand.getSum() <= 21) {
                        atLeastOneHandNoBust = true;
                    }
                }

                if (atLeastOneHandNoBust) {
                    // handle blackjack situation
                    dealDealersCard(dealer, deck);
                }
            }

            List<GameResult> results = new ArrayList<>();
            for (PlayerHand playerHand : playerHands) {
                GameResult finalResult;
                if (resultIfSomeoneHadBlackjack != null) {
                    // no need to calculate
                    finalResult = resultIfSomeoneHadBlackjack;
                } else if (playerHand.getSum() > 21) {
                    finalResult = GameResult.DEALER_WIN;
                } else if (dealer.getSum() > 21) {
                    finalResult = GameResult.PLAYER_WIN;
                } else if (playerHand.getSum() > dealer.getSum()) {
                    finalResult = GameResult.PLAYER_WIN;
                } else if (dealer.getSum() > playerHand.getSum()) {
                    finalResult = GameResult.DEALER_WIN;
                } else {
                    finalResult = GameResult.PUSH;
                }

                results.add(finalResult);
                switch (finalResult) {
                    case PLAYER_BLACKJACK:
                        playerAsset += playerHand.getBet() * 1.5;
                        break;
                    case PLAYER_WIN:
                        playerAsset += playerHand.getBet();
                        break;
                    case DEALER_WIN:
                        playerAsset -= playerHand.getBet();
                        break;
                    case PUSH:
                        // do nothing
                    default:
                        break;
                }
            }

            if (currGame <= 10 || currGame == request.getNumOfGame()) {
                GameRecord record = new GameRecord();
                record.setDealer(dealer);
                record.setPlayerAllHands(playerHands);
                record.setPlayerOriginalAsset(playerOriginalAsset);
                record.setPlayerAfterGameAsset(playerAsset);
                record.setResults(results);
                record.setGameNumber(currGame);
                gameRecords.add(record);
            }

            logProgress(currGame, request);

        }

        SimulatorResponse response = new SimulatorResponse(gameRecords);
        return response;
    }

    private void dealDealersCard(Hand dealer, Deck deck) {
        while (dealer.getSum() < 17 || dealer.isSoft17()) {
            dealer.add(deck.next());
        }
    }

    private void logProgress(int currGame, SimulatorRequest request) {
        int numOfGame = request.getNumOfGame();

        // If numOfGame is less than or equal to 10, put the currGame as the progress
        // directly
        if (numOfGame <= 10) {
            simulatorProgressCache.put(request.getTrackingUuid(), currGame);
            return;
        }

        int progressPoint = numOfGame / 10;

        if (currGame % progressPoint == 0 && currGame / progressPoint <= 10) {
            simulatorProgressCache.put(request.getTrackingUuid(), currGame / progressPoint);
        }
    }
}
