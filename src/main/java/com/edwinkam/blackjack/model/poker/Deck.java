package com.edwinkam.blackjack.model.poker;

public class Deck {
    private Card[] cards;
    private int currIndex;
    private int numOfDecks;

    public Deck(int numOfDecks) {
        this.cards = new Card[numOfDecks * 52];
        this.numOfDecks = numOfDecks;

        init();
        shuffle();
    }

    public void init() {
        int index = 0;
        for (int i = 0; i < numOfDecks; i++) {
            for (int cardNumber = 1; cardNumber <= 13; cardNumber++) {
                this.cards[index++] = new Card(cardNumber);
            }
        }
        this.currIndex = 0;
    }

    public void shuffle() {
        this.currIndex = 0;
        // do nothing for now
    }
    public Card next() {
        return cards[currIndex++];
    }

    public double percentageUsed() {
        return (double) currIndex / (double) cards.length;
    }

}
