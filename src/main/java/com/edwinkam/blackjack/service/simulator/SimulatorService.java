package com.edwinkam.blackjack.service.simulator;

import com.edwinkam.blackjack.model.game.GameRecord;
import com.edwinkam.blackjack.model.game.GameResult;
import com.edwinkam.blackjack.model.game.PlayerAction;
import com.edwinkam.blackjack.model.game.PlayerHand;
import com.edwinkam.blackjack.model.poker.Card;
import com.edwinkam.blackjack.model.poker.Deck;
import com.edwinkam.blackjack.model.poker.Hand;
import com.edwinkam.blackjack.model.simulator.SimulatorRequest;
import com.edwinkam.blackjack.model.simulator.SimulatorResponse;
import com.edwinkam.blackjack.provider.DeckProvider;
import com.edwinkam.blackjack.service.strategy.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SimulatorService {
    private StrategyService strategyService;
    private DeckProvider deckProvider;

    @Autowired
    public SimulatorService(StrategyService strategyService,
                            DeckProvider deckProvider) {
        this.strategyService = strategyService;
        this.deckProvider = deckProvider;
    }

    private int NUM_OF_ONE_DECK = 52;
    private int NUM_OF_DECK = 2;
    private double CUT_OFF = 0.5;
    private double BASE_BET = 1;

    private List<GameRecord> gameRecords = new ArrayList<>();
    public SimulatorResponse simulate(SimulatorRequest request) {
        int gameLeft = request.getNumOfGame();
        Deck deck = deckProvider.newDeck(2);
        double playerAsset = 0;

        while (gameLeft > 0) {
            gameLeft--;
            double playerOriginalAsset = playerAsset;

            if (deck.percentageUsed() < CUT_OFF) {
                // RESHUFFLE
                deck.shuffle();
            }
            PlayerHand playerFirstHand = new PlayerHand(BASE_BET);
            List<PlayerHand> playerHands = new ArrayList<>();
            playerHands.add(playerFirstHand);
            Hand dealer = new Hand();

            playerFirstHand.add(deck.next());
            dealer.add(deck.next());
            playerFirstHand.add(deck.next());
            dealer.add(deck.next());

            GameResult gameResult;

            if (playerFirstHand.hasBlackjack() && dealer.hasBlackjack()) {
                // push
                gameResult = GameResult.PUSH;
            } else if (playerFirstHand.hasBlackjack()) {
                // player win 1.5
                gameResult = GameResult.PLAYER_BLACKJACK;
            } else if (dealer.hasBlackjack()) {
                // dealer win 1
                gameResult = GameResult.DEALER_WIN;
            } else {
                // PLAYER start to play
                for (int i = 0; i < playerHands.size(); i++) {
                    PlayerAction action;
                    while (true) {
                        PlayerHand playerCurrentHand = playerHands.get(i);
                        action = strategyService.getPlayerAction(playerCurrentHand, dealer);
                        if (action == PlayerAction.STAND) {
                            break;
                        }

                        switch (action) {
                            case HIT:
                                playerCurrentHand.add(deck.next());
                                break;
                            case DOUBLE:
                                playerCurrentHand.doubleBet();
                                break;
                            case SPLIT:
                                Card firstCard = playerCurrentHand.getHand().get(0);
                                Card secondCard = playerCurrentHand.getHand().get(1);

                                PlayerHand newHand = new PlayerHand(playerCurrentHand.getBet());

                                // split two hand, assign one new card to each
                                playerFirstHand.setHand(Arrays.asList(firstCard, deck.next()));
                                newHand.setHand(Arrays.asList(secondCard, deck.next()));

                                playerHands.add(newHand);
                                break;
                            default:
                                break;
                        }
                    }

                }
            }
            // handle blackjack situation
            dealDealersCard(dealer, deck);

            List<GameResult> results = new ArrayList<>();
            for (PlayerHand playerHand: playerHands) {
                if (playerHand.getSum() > dealer.getSum()) {
                    gameResult = GameResult.PLAYER_WIN;
                } else if (dealer.getSum() > playerHand.getSum()) {
                    gameResult = GameResult.DEALER_WIN;
                } else {
                    gameResult = GameResult.PUSH;
                }

                results.add(gameResult);

                switch (gameResult) {
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

            GameRecord record = new GameRecord();
            record.setDealer(dealer);
            record.setPlayerAllHands(playerHands);
            record.setPlayerOriginalAsset(playerOriginalAsset);
            record.setPlayerAfterGameAsset(playerAsset);
            record.setResults(results);

            gameRecords.add(record);

        }

        SimulatorResponse response = new SimulatorResponse(gameRecords);
        return response;
    }
    private void dealDealersCard(Hand dealer, Deck deck) {
        while (dealer.getSum() < 17 || dealer.isSoft17()) {
            dealer.add(deck.next());
        }
    }
}
