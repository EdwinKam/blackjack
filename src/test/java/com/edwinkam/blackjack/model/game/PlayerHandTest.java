package com.edwinkam.blackjack.model.game;

import com.edwinkam.blackjack.model.poker.Card;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PlayerHandTest {
    @Test
    public void testAdd() {
        PlayerHand hand = new PlayerHand(1);
        hand.add(new Card(1));
    }

    @Test
    public void testAddList() {
        List<PlayerHand> hands = new ArrayList<>();
        hands.add(new PlayerHand(1));
        hands.get(0).add(new Card(1));
    }

}