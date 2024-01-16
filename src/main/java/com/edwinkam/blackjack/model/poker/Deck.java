package com.edwinkam.blackjack.model.poker;

import java.util.Random;

public class Deck {
    private Card[] cards;
    private int currIndex;
    private int numOfDecks;
    private int runningCount;

    public Deck(int numOfDecks) {
        this.cards = new Card[numOfDecks * 52];
        this.numOfDecks = numOfDecks;
        this.runningCount = 0;

        init();
        shuffle();
    }

    public void init() {
        int index = 0;
        for (int i = 0; i < numOfDecks; i++) {
            for (int cardNumber = 1; cardNumber <= 13; cardNumber++) {
                for (int suit = 0; suit < 4; suit++) {
                    this.cards[index++] = new Card(cardNumber);
                }
            }
        }
        this.currIndex = 0;
        this.runningCount = 0;
    }

    public void shuffle() {
        Random random = new Random();
        for (int i = cards.length - 1; i > 0; i--) {
            int j = random.nextInt(cards.length);

            // Swap cards[i] with cards[j]
            Card temp = cards[i];
            cards[i] = cards[j];
            cards[j] = temp;
        }
        currIndex = 0;  // Reset current index after shuffling
        this.runningCount = 0;
    }

    public Card next() {
        Card card = cards[currIndex++];
        if (card.getValue() == 1 || card.getValue() == 10) {
            runningCount--;
        } else if (card.getValue() >= 2 && card.getValue() <= 6) {
            runningCount++;
        }
        return card;
    }

    public int getTrueRunningCount() {
        return this.runningCount / getDeckRemaining();
    }

    public int getDeckRemaining() {
        return (cards.length - currIndex) / 52 + 1;
    }

    public double percentageUsed() {
        return (double) currIndex / (double) cards.length;
    }

}
