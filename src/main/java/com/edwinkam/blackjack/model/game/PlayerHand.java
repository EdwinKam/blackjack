package com.edwinkam.blackjack.model.game;

import com.edwinkam.blackjack.model.poker.Card;
import com.edwinkam.blackjack.model.poker.Hand;

import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;

@EqualsAndHashCode(callSuper=false)
@Data
@NoArgsConstructor
public class PlayerHand extends Hand {
    private double bet;

    public PlayerHand(double bet) {
        super();
        this.bet = bet;
    }

    public void setHand(Card... cards) {
        this.hand = new ArrayList<>();
        for (Card card: cards) {
            this.add(card);
        }
    }
    public void doubleBet() {
        this.bet = this.bet * 2;
    }

    public String toString() {
        return super.toString();
    }
}
