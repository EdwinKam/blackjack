package com.edwinkam.blackjack.model.poker;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class Hand {
    private List<Card> hand;
    private boolean hasAce;

    public Hand() {
        hand = new ArrayList<>();
    }

    public Hand(String... cards) {
        hand = new ArrayList<>();
        for (String card: cards) {
            this.add(new Card(card));
        }
    }

    public void setHand(List<Card> hand) {
        this.hand = new ArrayList<>(hand);
    }
    public void add(Card card) {
        if (card.getNumber() == 1) {
            hasAce = true;
        }
        hand.add(card);
    }
    public int getSum() {
        int sum = 0;
        for (Card card: hand) {
            sum += card.getValue();
        }

        if (hasAce && sum <= 11) {
            // ace could be 1 or 11
            sum += 10;
        }

        return sum;
    }

    public boolean isSoft17() {
        return getSum() == 17 && hasAce;
    }

    public boolean hasBlackjack() {
        return hand.size() == 2 && getSum() == 21;
    }

    public String toString() {
        return hand.toString();
    }
}
