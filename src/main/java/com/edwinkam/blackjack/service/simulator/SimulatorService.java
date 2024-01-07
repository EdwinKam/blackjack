package com.edwinkam.blackjack.service.simulator;

import com.edwinkam.blackjack.model.game.GameRecord;
import com.edwinkam.blackjack.model.game.GameResult;
import com.edwinkam.blackjack.model.game.PlayerAction;
import com.edwinkam.blackjack.model.poker.Card;
import com.edwinkam.blackjack.model.poker.Deck;
import com.edwinkam.blackjack.model.poker.Hand;
import com.edwinkam.blackjack.model.simulator.SimulatorRequest;

import java.util.ArrayList;
import java.util.List;

public class SimulatorService {
    private int NUM_OF_ONE_DECK = 52;
    private int NUM_OF_DECK = 2;
    private double CUT_OFF = 0.5;

    private List<GameRecord> gameRecords = new ArrayList<>();
    public List<GameRecord> simulate(SimulatorRequest request) {
        int gameLeft = request.getNumOfGame();
        int cardLeft = NUM_OF_ONE_DECK * NUM_OF_DECK;
        Deck deck = new Deck(2);
        double playerAsset = 0;

        while (gameLeft > 0) {
            gameLeft--;
            double playerOriginalAsset = playerAsset;

            if ((double) cardLeft / (double) NUM_OF_ONE_DECK < CUT_OFF) {
                // need to use a new deck
                cardLeft = NUM_OF_ONE_DECK * NUM_OF_DECK;
                // RESHUFFLE
                deck.shuffle();
            }
            Hand player = new Hand();
            Hand dealer = new Hand();

            player.add(deck.next());
            dealer.add(deck.next());
            player.add(deck.next());
            dealer.add(deck.next());

            GameResult gameResult;

            if (player.hasBlackjack() && dealer.hasBlackjack()) {
                // push
                gameResult = GameResult.PUSH;
            } else if (player.hasBlackjack()) {
                // player win 1.5
                gameResult = GameResult.PLAYER_BLACKJACK;
            } else if (dealer.hasBlackjack()) {
                // dealer win 1
                gameResult = GameResult.DEALER_WIN;
            } else {
                // PLAYER start to play
                while (true) {
                    PlayerAction action = playerChoice(player, dealer);
                    if (action == PlayerAction.STAND) {
                        break;
                    }
                }

                if (player.getSum() > dealer.getSum()) {
                    gameResult = GameResult.PLAYER_WIN;
                } else if (dealer.getSum() > player.getSum()) {
                    gameResult = GameResult.DEALER_WIN;
                } else {
                    gameResult = GameResult.PUSH;
                }
            }

            switch (gameResult) {
                case PLAYER_BLACKJACK:
                    playerAsset += 1.5;
                    break;
                case PLAYER_WIN:
                    playerAsset += 1;
                    break;
                case DEALER_WIN:
                    playerAsset -= 1;
                    break;
                case PUSH:
                    // do nothing
                default:
                    break;
            }

            GameRecord record = new GameRecord();
            record.setDealer(dealer);
            record.setPlayer(player);
            record.setPlayerOriginalAsset(playerOriginalAsset);
            record.setPlayerAfterGameAsset(playerAsset);
            record.setOriginalBet(1);
            record.setResult(gameResult);

            gameRecords.add(record);

        }
        return gameRecords;
    }

    private PlayerAction playerChoice(Hand player, Hand dealer) {
        return PlayerAction.STAND;
    }
}
