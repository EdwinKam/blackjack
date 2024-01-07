package com.edwinkam.blackjack.model.game;

import com.edwinkam.blackjack.model.poker.Hand;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
public class GameRecord {
    private List<PlayerHand> playerAllHands; // if player split, player has multiple hands
    private Hand dealer;
    private double playerOriginalAsset;
    private double playerAfterGameAsset;
    private List<GameResult> results; // if split, can have multiple results

    public String toString() {
        if (playerAllHands.size() == 1) {
            return String.format("[Player: %s Dealer: %s result: %s playerAfterGame: %f]\n", playerAllHands.get(0), dealer, results.get(0).name(), playerAfterGameAsset);
        } else {
            StringBuilder sb = new StringBuilder("Player: ");
            for (int i = 0; i < playerAllHands.size(); i++) {
                sb.append(String.format("Hand%d %s %s\n", i + 1, playerAllHands.get(i), results.get(i)));
            }
            sb.append(String.format("Dealer: %s playerAfterGame %f", dealer, playerAfterGameAsset));
            return sb.toString();
        }
    }
}
