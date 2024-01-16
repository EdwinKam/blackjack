package com.edwinkam.blackjack.model.poker;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    protected List<Card> hand;
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

    public Hand(Card... cards) {
        hand = new ArrayList<>();
        for (Card card: cards) {
            this.add(card);
        }
    }

    public void setHand(List<Card> hand) {
        this.hand = new ArrayList<>(hand);
    }
    public void add(Card card) {
        if (card.getId() == 1) {
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

    public int getHardSum() {
        int sum = 0;
        for (Card card: hand) {
            sum += card.getValue();
        }

        return sum;
    }

    public boolean isSoft17() {
        return getSum() == 17 && hasAce;
    }

    public boolean hasBlackjack() {
        return hand.size() == 2 && getSum() == 21;
    }

    public boolean isHasAce() {
        return this.hasAce;
    }

    public Card getCard(int index) {
        return this.hand.get(index);
    }

    public int getHandCount() {
        return this.hand.size();
    }

    public int getCardCount() {
        return hand.size();
    }

    public String toString() {
        return hand.toString() + " Sum: " + this.getSum();
    }
}
