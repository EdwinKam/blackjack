package com.edwinkam.blackjack.provider;

import com.edwinkam.blackjack.model.poker.Deck;
import org.springframework.stereotype.Service;

// this class is created to easier testing
// instead of doing Deck deck = new Deck(), we now do Deck deck = deckProvider.newDeck();
// that way we can mock the deck
@Service
public class DeckProvider {
    public Deck newDeck(int numOfDeck) {
        return new Deck(numOfDeck);
    }
}
