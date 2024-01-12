package com.edwinkam.blackjack.model.poker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HandTest {
    private Hand hand;
    @BeforeEach
    public void init() {
        hand = new Hand();
    }

    @Test
    public void testGetSumWithAce() {
        hand.add(new Card(1));
        hand.add(new Card(10));

        assertEquals(21, hand.getSum());
    }

    @Test
    public void testGetSumWithHead() {
        hand.add(new Card(2));
        hand.add(new Card(13));

        assertEquals(12, hand.getSum());
    }

    @Test
    public void testGetSumWithHeadAce() {
        hand.add(new Card(1));
        hand.add(new Card(13));

        assertEquals(21, hand.getSum());
    }

    @Test
    public void testGetSumWithHeadAceMoreThan2Cards() {
        hand.add(new Card(1));
        hand.add(new Card(2));
        hand.add(new Card(13));

        assertEquals(13, hand.getSum());
    }

    @Test
    public void isSoft17() {
        hand.add(new Card(1));
        hand.add(new Card(6));

        assertTrue(hand.isSoft17());
    }

    @Test
    public void isSoft17And3Cards() {
        hand.add(new Card(1));
        hand.add(new Card(3));
        hand.add(new Card(3));

        assertTrue(hand.isSoft17());
    }

    @Test
    public void is17ButNoAce() {
        hand.add(new Card(7));
        hand.add(new Card(7));
        hand.add(new Card(3));

        assertFalse(hand.isSoft17());
    }

    @Test
    public void hasBlackjack() {
        hand.add(new Card(1));
        hand.add(new Card(10));

        assertTrue(hand.hasBlackjack());
    }

    @Test
    public void hasBlackjackWithHead() {
        hand.add(new Card(1));
        hand.add(new Card(12));

        assertTrue(hand.hasBlackjack());
    }

    @Test
    public void hasAce21But3Cards() {
        hand.add(new Card(10));
        hand.add(new Card(10));
        hand.add(new Card(1));

        assertFalse(hand.hasBlackjack());
    }

    @Test
    public void has21But3Cards() {
        hand.add(new Card(7));
        hand.add(new Card(7));
        hand.add(new Card(7));

        assertFalse(hand.hasBlackjack());
    }

    @Test
    public void addList() {
        List<Hand> hands = new ArrayList<>();
        hands.add(new Hand());
        hands.get(0).add(new Card(1));
    }
}