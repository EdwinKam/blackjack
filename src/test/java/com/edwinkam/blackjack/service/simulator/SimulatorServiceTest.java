package com.edwinkam.blackjack.service.simulator;

import com.edwinkam.blackjack.cache.SimulatorProgressCache;
import com.edwinkam.blackjack.model.game.GameResult;
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
import java.util.Arrays;

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

    @Mock
    private SimulatorProgressCache simulatorProgressCache;

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

        assertEquals(c1, response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(0));
        assertEquals(c2, response.getGameRecords().get(0).getDealer().getCard(0));
        assertEquals(c3, response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(1));
        assertEquals(c4, response.getGameRecords().get(0).getDealer().getCard(1));
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
        assertEquals(d1, response.getGameRecords().get(0).getDealer().getCard(0));
        assertEquals(d2, response.getGameRecords().get(0).getDealer().getCard(1));
        // player split
        assertEquals(p1, response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(0));
        assertEquals(p2, response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(1));
        assertEquals(p3, response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(2));
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
        assertEquals(d1, response.getGameRecords().get(0).getDealer().getCard(0));
        assertEquals(d2, response.getGameRecords().get(0).getDealer().getCard(1));
        // player split
        assertEquals(p1, response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(0));
        assertEquals(p3, response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(1));
        assertEquals(p2, response.getGameRecords().get(0).getPlayerAllHands().get(1).getCard(0));
        assertEquals(p4, response.getGameRecords().get(0).getPlayerAllHands().get(1).getCard(1));
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
        assertEquals(d1, response.getGameRecords().get(0).getDealer().getCard(0));
        assertEquals(d2, response.getGameRecords().get(0).getDealer().getCard(1));
        // player split
        assertEquals(p1, response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(0));
        assertEquals(p3, response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(1));
        assertEquals(p5, response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(2));
        assertEquals(p2, response.getGameRecords().get(0).getPlayerAllHands().get(1).getCard(0));
        assertEquals(p4, response.getGameRecords().get(0).getPlayerAllHands().get(1).getCard(1));
        assertEquals(p6, response.getGameRecords().get(0).getPlayerAllHands().get(1).getCard(2));
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

        // dealer hand
        assertEquals(d1, response.getGameRecords().get(0).getDealer().getCard(0));
        assertEquals(d2, response.getGameRecords().get(0).getDealer().getCard(1));
        // player split
        assertEquals(p1, response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(0));
        assertEquals(p5, response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(1));
        assertEquals(p2, response.getGameRecords().get(0).getPlayerAllHands().get(1).getCard(0));
        assertEquals(p4, response.getGameRecords().get(0).getPlayerAllHands().get(1).getCard(1));
        assertEquals(p3, response.getGameRecords().get(0).getPlayerAllHands().get(2).getCard(0));
        assertEquals(p6, response.getGameRecords().get(0).getPlayerAllHands().get(2).getCard(1));
    }

    @Test
    public void testPlayerBusted() {
        mockDealCard("K", "2", "3", "2", "10", "10");
        SimulatorRequest request = new SimulatorRequest(1);
        Mockito.when(strategyService.getPlayerAction(Mockito.any(Hand.class), Mockito.any(Hand.class)))
                .thenReturn(PlayerAction.HIT, PlayerAction.HIT, PlayerAction.STAND);

        SimulatorResponse response = simulatorService.simulate(request);

        // dealer hand
        assertEquals(card("K"), response.getGameRecords().get(0).getDealer().getCard(0));
        assertEquals(card("2"), response.getGameRecords().get(0).getDealer().getCard(1));
        // player hand
        assertEquals(card("3"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(0));
        assertEquals(card("2"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(1));
        assertEquals(card("10"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(2));
        assertEquals(card("10"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(2));
        assertEquals(GameResult.DEALER_WIN, response.getGameRecords().get(0).getResults().get(0));
        assertEquals(-1, response.getGameRecords().get(0).getPlayerAfterGameAsset());
    }

    @Test
    public void testPlayerBustedWithoutStrategyService() {
        mockDealCard("K", "2", "3", "2", "10", "10");
        SimulatorRequest request = new SimulatorRequest(1);
        Mockito.when(strategyService.getPlayerAction(Mockito.any(Hand.class), Mockito.any(Hand.class)))
                .thenReturn(PlayerAction.HIT); // strategy service always hit

        SimulatorResponse response = simulatorService.simulate(request);

        // dealer hand
        assertEquals(card("K"), response.getGameRecords().get(0).getDealer().getCard(0));
        assertEquals(card("2"), response.getGameRecords().get(0).getDealer().getCard(1));
        // player hand
        assertEquals(card("3"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(0));
        assertEquals(card("2"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(1));
        assertEquals(card("10"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(2));
        assertEquals(card("10"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(2));
        assertEquals(GameResult.DEALER_WIN, response.getGameRecords().get(0).getResults().get(0));
        assertEquals(-1, response.getGameRecords().get(0).getPlayerAfterGameAsset());
    }

    @Test
    public void testDealerBusted() {
        mockDealCard("K", "2", "10", "10", "K");
        SimulatorRequest request = new SimulatorRequest(1);
        Mockito.when(strategyService.getPlayerAction(Mockito.any(Hand.class), Mockito.any(Hand.class)))
                .thenReturn(PlayerAction.STAND); // strategy service always hit

        SimulatorResponse response = simulatorService.simulate(request);

        // dealer hand
        assertEquals(card("K"), response.getGameRecords().get(0).getDealer().getCard(0));
        assertEquals(card("2"), response.getGameRecords().get(0).getDealer().getCard(1));
        assertEquals(card("K"), response.getGameRecords().get(0).getDealer().getCard(2));
        // player hand
        assertEquals(card("10"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(0));
        assertEquals(card("10"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(1));
        assertEquals(GameResult.PLAYER_WIN, response.getGameRecords().get(0).getResults().get(0));
        assertEquals(1, response.getGameRecords().get(0).getPlayerAfterGameAsset());
    }

    @Test
    public void testPlayerGotBlackjack() {
        mockDealCard("K", "2", "10", "A");
        SimulatorRequest request = new SimulatorRequest(1);
        // strategyService shouldn't get called

        SimulatorResponse response = simulatorService.simulate(request);

        // dealer hand
        assertEquals(card("K"), response.getGameRecords().get(0).getDealer().getCard(0));
        assertEquals(card("2"), response.getGameRecords().get(0).getDealer().getCard(1));
        // player hand
        assertEquals(card("10"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(0));
        assertEquals(card("A"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(1));
        assertEquals(GameResult.PLAYER_BLACKJACK, response.getGameRecords().get(0).getResults().get(0));
        assertEquals(1.5, response.getGameRecords().get(0).getPlayerAfterGameAsset());
    }

    @Test
    public void testDealerGotBlackjack() {
        mockDealCard("K", "A", "10", "10");
        SimulatorRequest request = new SimulatorRequest(1);
        // strategyService shouldn't get called

        SimulatorResponse response = simulatorService.simulate(request);

        // dealer hand
        assertEquals(card("K"), response.getGameRecords().get(0).getDealer().getCard(0));
        assertEquals(card("A"), response.getGameRecords().get(0).getDealer().getCard(1));
        // player hand
        assertEquals(card("10"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(0));
        assertEquals(card("10"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(1));
        assertEquals(GameResult.DEALER_WIN, response.getGameRecords().get(0).getResults().get(0));
        assertEquals(-1, response.getGameRecords().get(0).getPlayerAfterGameAsset());
    }

    @Test
    public void testBothGotBlackjack() {
        mockDealCard("K", "A", "A", "10");
        SimulatorRequest request = new SimulatorRequest(1);
        // strategyService shouldn't get called

        SimulatorResponse response = simulatorService.simulate(request);

        // dealer hand
        assertEquals(card("K"), response.getGameRecords().get(0).getDealer().getCard(0));
        assertEquals(card("A"), response.getGameRecords().get(0).getDealer().getCard(1));
        // player hand
        assertEquals(card("A"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(0));
        assertEquals(card("10"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(1));
        assertEquals(GameResult.PUSH, response.getGameRecords().get(0).getResults().get(0));
        assertEquals(0, response.getGameRecords().get(0).getPlayerAfterGameAsset());
    }

    @Test
    public void testSplitOneWinOneLost() {
        mockDealCard("K", "K", "10", "10", "3", "A");
        SimulatorRequest request = new SimulatorRequest(1);
        Mockito.when(strategyService.getPlayerAction(Mockito.any(Hand.class), Mockito.any(Hand.class)))
                .thenReturn(PlayerAction.SPLIT, PlayerAction.STAND, PlayerAction.STAND); // strategy service always hit

        SimulatorResponse response = simulatorService.simulate(request);

        // dealer hand
        assertEquals(card("K"), response.getGameRecords().get(0).getDealer().getCard(0));
        assertEquals(card("K"), response.getGameRecords().get(0).getDealer().getCard(1));
        // player hand
        assertEquals(card("10"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(0));
        assertEquals(card("3"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(1));
        assertEquals(card("10"), response.getGameRecords().get(0).getPlayerAllHands().get(1).getCard(0));
        assertEquals(card("A"), response.getGameRecords().get(0).getPlayerAllHands().get(1).getCard(1));
        assertEquals(GameResult.DEALER_WIN, response.getGameRecords().get(0).getResults().get(0));
        assertEquals(GameResult.PLAYER_WIN, response.getGameRecords().get(0).getResults().get(1));
        assertEquals(0, response.getGameRecords().get(0).getPlayerAfterGameAsset());
    }

    @Test
    public void testSplitLostBoth() {
        mockDealCard("K", "K", "10", "10", "3", "2");
        SimulatorRequest request = new SimulatorRequest(1);
        Mockito.when(strategyService.getPlayerAction(Mockito.any(Hand.class), Mockito.any(Hand.class)))
                .thenReturn(PlayerAction.SPLIT, PlayerAction.STAND, PlayerAction.STAND); // strategy service always hit

        SimulatorResponse response = simulatorService.simulate(request);

        // dealer hand
        assertEquals(card("K"), response.getGameRecords().get(0).getDealer().getCard(0));
        assertEquals(card("K"), response.getGameRecords().get(0).getDealer().getCard(1));
        // player hand
        assertEquals(card("10"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(0));
        assertEquals(card("3"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(1));
        assertEquals(card("10"), response.getGameRecords().get(0).getPlayerAllHands().get(1).getCard(0));
        assertEquals(card("2"), response.getGameRecords().get(0).getPlayerAllHands().get(1).getCard(1));
        assertEquals(GameResult.DEALER_WIN, response.getGameRecords().get(0).getResults().get(0));
        assertEquals(GameResult.DEALER_WIN, response.getGameRecords().get(0).getResults().get(1));
        assertEquals(-2, response.getGameRecords().get(0).getPlayerAfterGameAsset());
    }

    @Test
    public void testSplitWonBoth() {
        mockDealCard("K", "5", "10", "10", "3", "2", "8");
        SimulatorRequest request = new SimulatorRequest(1);
        Mockito.when(strategyService.getPlayerAction(Mockito.any(Hand.class), Mockito.any(Hand.class)))
                .thenReturn(PlayerAction.SPLIT, PlayerAction.STAND, PlayerAction.STAND); // strategy service always hit

        SimulatorResponse response = simulatorService.simulate(request);

        // dealer hand
        assertEquals(card("K"), response.getGameRecords().get(0).getDealer().getCard(0));
        assertEquals(card("5"), response.getGameRecords().get(0).getDealer().getCard(1));
        assertEquals(card("8"), response.getGameRecords().get(0).getDealer().getCard(2));
        // player hand
        assertEquals(card("10"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(0));
        assertEquals(card("3"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(1));
        assertEquals(card("10"), response.getGameRecords().get(0).getPlayerAllHands().get(1).getCard(0));
        assertEquals(card("2"), response.getGameRecords().get(0).getPlayerAllHands().get(1).getCard(1));
        assertEquals(GameResult.PLAYER_WIN, response.getGameRecords().get(0).getResults().get(0));
        assertEquals(GameResult.PLAYER_WIN, response.getGameRecords().get(0).getResults().get(1));
        assertEquals(2, response.getGameRecords().get(0).getPlayerAfterGameAsset());
    }

    @Test
    public void testTwoGame() {
        mockDealCard("K", "9", "10", "10", "3", "2", "8", "3", "9", "10");
        SimulatorRequest request = new SimulatorRequest(2);
        Mockito.when(strategyService.getPlayerAction(Mockito.any(Hand.class), Mockito.any(Hand.class)))
                .thenReturn(PlayerAction.STAND, PlayerAction.STAND); // strategy service always hit

        SimulatorResponse response = simulatorService.simulate(request);

        // first game
        // dealer hand
        assertEquals(card("K"), response.getGameRecords().get(0).getDealer().getCard(0));
        assertEquals(card("9"), response.getGameRecords().get(0).getDealer().getCard(1));
        // player hand
        assertEquals(card("10"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(0));
        assertEquals(card("10"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(1));
        assertEquals(GameResult.PLAYER_WIN, response.getGameRecords().get(0).getResults().get(0));
        assertEquals(1, response.getGameRecords().get(0).getPlayerAfterGameAsset());
        // second game
        // dealer hand
        assertEquals(card("2"), response.getGameRecords().get(1).getDealer().getCard(0));
        assertEquals(card("3"), response.getGameRecords().get(1).getDealer().getCard(1));
        assertEquals(card("9"), response.getGameRecords().get(1).getDealer().getCard(2));
        assertEquals(card("10"), response.getGameRecords().get(1).getDealer().getCard(3));
        // player hand
        assertEquals(card("3"), response.getGameRecords().get(1).getPlayerAllHands().get(0).getCard(0));
        assertEquals(card("8"), response.getGameRecords().get(1).getPlayerAllHands().get(0).getCard(1));
        assertEquals(GameResult.PLAYER_WIN, response.getGameRecords().get(1).getResults().get(0));
        assertEquals(1, response.getGameRecords().get(1).getPlayerOriginalAsset());
        assertEquals(2, response.getGameRecords().get(1).getPlayerAfterGameAsset());
    }

    @Test
    public void testDouble() {
        mockDealCard("10", "5", "4", "5", "6", "K");
        SimulatorRequest request = new SimulatorRequest(1);
        Mockito.when(strategyService.getPlayerAction(Mockito.any(Hand.class), Mockito.any(Hand.class)))
                .thenReturn(PlayerAction.DOUBLE); // strategy service will double once

        SimulatorResponse response = simulatorService.simulate(request);

        // first game
        // dealer hand
        assertEquals(card("10"), response.getGameRecords().get(0).getDealer().getCard(0));
        assertEquals(card("5"), response.getGameRecords().get(0).getDealer().getCard(1));
        assertEquals(card("K"), response.getGameRecords().get(0).getDealer().getCard(2));
        // player hand
        assertEquals(card("4"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(0));
        assertEquals(card("5"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(1));
        assertEquals(card("6"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(2));
        assertEquals(3, response.getGameRecords().get(0).getPlayerAllHands().get(0).getHandCount());
        assertEquals(GameResult.PLAYER_WIN, response.getGameRecords().get(0).getResults().get(0));
        assertEquals(2, response.getGameRecords().get(0).getPlayerAfterGameAsset());
    }

    @Test
    public void testDoubleBusted() {
        mockDealCard("10", "5", "10", "5", "10", "K");
        SimulatorRequest request = new SimulatorRequest(1);
        Mockito.when(strategyService.getPlayerAction(Mockito.any(Hand.class), Mockito.any(Hand.class)))
                .thenReturn(PlayerAction.DOUBLE); // strategy service will double once

        SimulatorResponse response = simulatorService.simulate(request);

        // first game
        // dealer hand
        assertEquals(card("10"), response.getGameRecords().get(0).getDealer().getCard(0));
        assertEquals(card("5"), response.getGameRecords().get(0).getDealer().getCard(1));
        assertEquals(2, response.getGameRecords().get(0).getDealer().getHandCount());
        // player hand
        assertEquals(card("10"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(0));
        assertEquals(card("5"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(1));
        assertEquals(card("10"), response.getGameRecords().get(0).getPlayerAllHands().get(0).getCard(2));
        assertEquals(3, response.getGameRecords().get(0).getPlayerAllHands().get(0).getHandCount());
        assertEquals(GameResult.DEALER_WIN, response.getGameRecords().get(0).getResults().get(0));
        assertEquals(-2, response.getGameRecords().get(0).getPlayerAfterGameAsset());
    }

    private void mockDealCard(String dealer1, String dealer2, String... playerThenDealer) {
        // Create an array of Card objects from the input String array
        Card[] cardObjects = new Card[playerThenDealer.length + 2];
        cardObjects[0] = new Card(playerThenDealer[0]);
        cardObjects[1] = new Card(dealer1);
        cardObjects[2] = new Card(playerThenDealer[1]);
        cardObjects[3] = new Card(dealer2);

        for (int i = 4; i < cardObjects.length; i++) {
            cardObjects[i] = new Card(playerThenDealer[i - 2]);
        }
    
        // Use Mockito to mock the next() method
        Mockito.when(deck.next()).thenReturn(cardObjects[0], Arrays.copyOfRange(cardObjects, 1, cardObjects.length));
    }

    private Card card(String s) {
        return new Card(s);
    }

}