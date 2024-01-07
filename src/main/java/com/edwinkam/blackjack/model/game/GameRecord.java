package com.edwinkam.blackjack.model.game;

import com.edwinkam.blackjack.model.poker.Hand;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameRecord {
    private Hand player;
    private Hand dealer;
    private double originalBet;
    private double playerOriginalAsset;
    private double playerAfterGameAsset;
    private GameResult result;

    public String toString() {
        return String.format("[Player: %s Dealer: %s result: %s playerAfterGame: %f]\n", player, dealer, result.name(), playerAfterGameAsset);
    }
}
