package com.edwinkam.blackjack.service.simulator;

import com.edwinkam.blackjack.model.game.PlayerAction;
import com.edwinkam.blackjack.model.poker.Card;
import com.edwinkam.blackjack.model.poker.Deck;
import com.edwinkam.blackjack.model.poker.Hand;
import com.edwinkam.blackjack.model.simulator.SimulatorRequest;
import com.edwinkam.blackjack.model.simulator.SimulatorResponse;
import com.edwinkam.blackjack.provider.DeckProvider;
import com.edwinkam.blackjack.service.strategy.StrategyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class SimulatorServiceTest {
    @InjectMocks
    private SimulatorService simulatorService;

    @Mock
    private DeckProvider deckProvider;

    @Mock
    private Deck deck;

    @Mock
    private StrategyService strategyService;

    @Before
    public void init() {
        Mockito.when(deckProvider.newDeck(Mockito.anyInt())).thenReturn(deck);
    }

    @Test
    public void testSimulateNumOfGame() {
        Mockito.when(strategyService.getPlayerAction(Mockito.any(), Mockito.any()))
                .thenReturn(PlayerAction.STAND);
        Mockito.when(deck.next()).thenReturn(new Card("4"));
        SimulatorRequest request = new SimulatorRequest(3);

        SimulatorResponse response = simulatorService.simulate(request);

        assertEquals(3, response.getGameRecords().size());
    }

    @Test
    public void testSimulateLargeNumOfGame() {
        Mockito.when(strategyService.getPlayerAction(Mockito.any(), Mockito.any()))
                .thenReturn(PlayerAction.STAND);
        Mockito.when(deck.next()).thenReturn(new Card("4"));
        SimulatorRequest request = new SimulatorRequest(3000);

        SimulatorResponse response = simulatorService.simulate(request);

        assertEquals(3000, response.getGameRecords().size());
    }

    @Test
    public void testMockDeck() {
        Card c1 = new Card("A");
        Card c2 = new Card("K");
        Card c3 = new Card("5");
        Card c4 = new Card("7");
        SimulatorRequest request = new SimulatorRequest(1);
        Mockito.when(deck.next()).thenReturn(c1, c2, c3, c4);
        Mockito.when(strategyService.getPlayerAction(Mockito.any(Hand.class), Mockito.any(Hand.class)))
                .thenReturn(PlayerAction.STAND);

        SimulatorResponse response = simulatorService.simulate(request);

        assertEquals(c1, response.getGameRecords().get(0).getPlayerAllHands().get(0).getHand().get(0));
        assertEquals(c2, response.getGameRecords().get(0).getDealer().getHand().get(0));
        assertEquals(c3, response.getGameRecords().get(0).getPlayerAllHands().get(0).getHand().get(1));
        assertEquals(c4, response.getGameRecords().get(0).getDealer().getHand().get(1));
    }

    @Test
    public void testHit() {
        Card p1 = new Card("A");
        Card d1 = new Card("K");
        Card p2 = new Card("2");
        Card d2 = new Card("Q");
        Card p3 = new Card("3");
        SimulatorRequest request = new SimulatorRequest(1);
        Mockito.when(deck.next()).thenReturn(p1, d1, p2, d2, p3);
        Mockito.when(strategyService.getPlayerAction(Mockito.any(Hand.class), Mockito.any(Hand.class)))
                .thenReturn(PlayerAction.HIT, PlayerAction.STAND);

        SimulatorResponse response = simulatorService.simulate(request);

        // dealer hand
        assertEquals(d1, response.getGameRecords().get(0).getDealer().getHand().get(0));
        assertEquals(d2, response.getGameRecords().get(0).getDealer().getHand().get(1));
        // player split
        assertEquals(p1, response.getGameRecords().get(0).getPlayerAllHands().get(0).getHand().get(0));
        assertEquals(p2, response.getGameRecords().get(0).getPlayerAllHands().get(0).getHand().get(1));
        assertEquals(p3, response.getGameRecords().get(0).getPlayerAllHands().get(0).getHand().get(2));
    }

    @Test
    public void testSplit() {
        Card p1 = new Card("A");
        Card d1 = new Card("K");
        Card p2 = new Card("A");
        Card d2 = new Card("7");
        Card p3 = new Card("2");
        Card p4 = new Card("3");
        SimulatorRequest request = new SimulatorRequest(1);
        Mockito.when(deck.next()).thenReturn(p1, d1, p2, d2, p3);
        Mockito.when(strategyService.getPlayerAction(Mockito.any(Hand.class), Mockito.any(Hand.class)))
                .thenReturn(PlayerAction.SPLIT, PlayerAction.STAND, PlayerAction.STAND);

        SimulatorResponse response = simulatorService.simulate(request);

        // dealer hand
        assertEquals(d1, response.getGameRecords().get(0).getDealer().getHand().get(0));
        assertEquals(d2, response.getGameRecords().get(0).getDealer().getHand().get(1));
        // player split
        assertEquals(p1, response.getGameRecords().get(0).getPlayerAllHands().get(0).getHand().get(0));
        assertEquals(p3, response.getGameRecords().get(0).getPlayerAllHands().get(0).getHand().get(1));
        assertEquals(p2, response.getGameRecords().get(0).getPlayerAllHands().get(1).getHand().get(0));
        assertEquals(p4, response.getGameRecords().get(0).getPlayerAllHands().get(1).getHand().get(1));
    }

    @Test
    public void testSplitHit() {
        Card p1 = new Card("A");
        Card d1 = new Card("K");
        Card p2 = new Card("A");
        Card d2 = new Card("4");
        Card p3 = new Card("2");
        Card p4 = new Card("3");
        Card p5 = new Card("7");
        Card p6 = new Card("8");
        SimulatorRequest request = new SimulatorRequest(1);
        Mockito.when(deck.next()).thenReturn(p1, d1, p2, d2, p3, p4, p5, p6);
        Mockito.when(strategyService.getPlayerAction(Mockito.any(Hand.class), Mockito.any(Hand.class)))
                .thenReturn(PlayerAction.SPLIT, PlayerAction.HIT, PlayerAction.STAND, PlayerAction.HIT, PlayerAction.STAND);

        SimulatorResponse response = simulatorService.simulate(request);

        // dealer hand
        assertEquals(d1, response.getGameRecords().get(0).getDealer().getHand().get(0));
        assertEquals(d2, response.getGameRecords().get(0).getDealer().getHand().get(1));
        // player split
        assertEquals(p1, response.getGameRecords().get(0).getPlayerAllHands().get(0).getHand().get(0));
        assertEquals(p3, response.getGameRecords().get(0).getPlayerAllHands().get(0).getHand().get(1));
        assertEquals(p5, response.getGameRecords().get(0).getPlayerAllHands().get(0).getHand().get(2));
        assertEquals(p2, response.getGameRecords().get(0).getPlayerAllHands().get(1).getHand().get(0));
        assertEquals(p4, response.getGameRecords().get(0).getPlayerAllHands().get(1).getHand().get(1));
        assertEquals(p6, response.getGameRecords().get(0).getPlayerAllHands().get(1).getHand().get(2));
    }

    @Test
    public void testSplitTwice() {
        Card p1 = new Card("A");
        Card d1 = new Card("K");
        Card p2 = new Card("A");
        Card d2 = new Card("7");
        Card p3 = new Card("A");
        Card p4 = new Card("4");
        Card p5 = new Card("5");
        Card p6 = new Card("6");
        SimulatorRequest request = new SimulatorRequest(1);
        Mockito.when(deck.next()).thenReturn(p1, d1, p2, d2, p3, p4, p5, p6);
        Mockito.when(strategyService.getPlayerAction(Mockito.any(Hand.class), Mockito.any(Hand.class)))
                .thenReturn(PlayerAction.SPLIT, PlayerAction.SPLIT, PlayerAction.STAND, PlayerAction.STAND, PlayerAction.STAND);

        SimulatorResponse response = simulatorService.simulate(request);

        System.out.println(response.getGameRecords());

        // dealer hand
        assertEquals(d1, response.getGameRecords().get(0).getDealer().getHand().get(0));
        assertEquals(d2, response.getGameRecords().get(0).getDealer().getHand().get(1));
        // player split
        assertEquals(p1, response.getGameRecords().get(0).getPlayerAllHands().get(0).getHand().get(0));
        assertEquals(p5, response.getGameRecords().get(0).getPlayerAllHands().get(0).getHand().get(1));
        assertEquals(p2, response.getGameRecords().get(0).getPlayerAllHands().get(1).getHand().get(0));
        assertEquals(p4, response.getGameRecords().get(0).getPlayerAllHands().get(1).getHand().get(1));
        assertEquals(p3, response.getGameRecords().get(0).getPlayerAllHands().get(2).getHand().get(0));
        assertEquals(p6, response.getGameRecords().get(0).getPlayerAllHands().get(2).getHand().get(1));
    }

}