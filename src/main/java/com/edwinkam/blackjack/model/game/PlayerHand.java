package com.edwinkam.blackjack.model.game;

import com.edwinkam.blackjack.model.poker.Hand;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PlayerHand extends Hand {
    private double bet;

    public PlayerHand(double bet) {
        super();
        this.bet = bet;
    }
    public void doubleBet() {
        this.bet = this.bet * 2;
    }

    public String toString() {
        return this.getHand().toString();
    }
}
