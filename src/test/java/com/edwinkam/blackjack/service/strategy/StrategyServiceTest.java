package com.edwinkam.blackjack.service.strategy;

import com.edwinkam.blackjack.model.game.PlayerAction;
import com.edwinkam.blackjack.model.poker.Hand;
import org.junit.Assert;
import org.junit.Test;

public class StrategyServiceTest {
    StrategyService strategyService = new StrategyService();
//    @Test
//    public void simpleSpotCheckHard() {
//        Hand player = new Hand("2", "3");
//        Hand dealer = new Hand("A", "5");
//
//        PlayerAction playerAction = strategyService.getPlayerAction(player, dealer);
//
//        Assert.assertEquals(PlayerAction.SPLIT, playerAction);
//
//    }

    @Test
    public void simpleSpotCheckAcePlayer() {
        Hand player = new Hand("A", "2");
        Hand dealer = new Hand("2", "5");

        PlayerAction playerAction = strategyService.getPlayerAction(player, dealer);

        Assert.assertEquals(PlayerAction.HIT, playerAction);
    }

    @Test
    public void simpleSpotCheckAcePlayer1() {
        Hand player = new Hand("A", "2");
        Hand dealer = new Hand("6", "5");

        PlayerAction playerAction = strategyService.getPlayerAction(player, dealer);

        Assert.assertEquals(PlayerAction.DOUBLE, playerAction);
    }

    @Test
    public void simpleSpotCheckAcePlayer2() {
        Hand player = new Hand("A", "2");
        Hand dealer = new Hand("7", "5");

        PlayerAction playerAction = strategyService.getPlayerAction(player, dealer);

        Assert.assertEquals(PlayerAction.HIT, playerAction);
    }

}