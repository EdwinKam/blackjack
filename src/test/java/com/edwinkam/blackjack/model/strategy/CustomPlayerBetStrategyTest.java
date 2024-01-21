package com.edwinkam.blackjack.model.strategy;

import org.junit.Assert;
import org.junit.Test;

public class CustomPlayerBetStrategyTest {
    @Test
    public void testGetBetNormal() throws Exception {
        CustomPlayerBetStrategy s = new CustomPlayerBetStrategy("1", "<", "3", "4");

        Assert.assertTrue(s.conditionMet(0));
        Assert.assertTrue(s.conditionMet(1));
    }

    @Test
    public void testBetReplaceLeftLessThan() throws Exception {
        CustomPlayerBetStrategy s = new CustomPlayerBetStrategy("runningCount", "<", "3", "5");

        Assert.assertTrue(s.conditionMet(2));
        Assert.assertFalse(s.conditionMet(3));
        Assert.assertFalse(s.conditionMet(4));
    }

    @Test
    public void testBetReplaceLeftLessThanOrEqual() throws Exception {
        CustomPlayerBetStrategy s = new CustomPlayerBetStrategy("runningCount", "<=", "3", "5");

        Assert.assertTrue(s.conditionMet(2));
        Assert.assertTrue(s.conditionMet(3));
        Assert.assertFalse(s.conditionMet(4));
    }

    @Test
    public void testBetReplaceRightLessThan() throws Exception {
        CustomPlayerBetStrategy s = new CustomPlayerBetStrategy("1", "<", "runningCount", "5");

        Assert.assertFalse(s.conditionMet(0));
        Assert.assertFalse(s.conditionMet(1));
        Assert.assertTrue(s.conditionMet(2));
    }

    @Test
    public void testBetReplaceRightLessThanEqual() throws Exception {
        CustomPlayerBetStrategy s = new CustomPlayerBetStrategy("1", "<=", "runningCount", "5");

        Assert.assertFalse(s.conditionMet(0));
        Assert.assertTrue(s.conditionMet(1));
        Assert.assertTrue(s.conditionMet(2));
    }

    @Test
    public void testBetReplaceRightEqual() throws Exception {
        CustomPlayerBetStrategy s = new CustomPlayerBetStrategy("1", "=", "runningCount", "5");

        Assert.assertFalse(s.conditionMet(0));
        Assert.assertTrue(s.conditionMet(1));
        Assert.assertFalse(s.conditionMet(2));
    }

    @Test
    public void testBetReplaceBothEqual() throws Exception {
        CustomPlayerBetStrategy s = new CustomPlayerBetStrategy("runningCount", "=", "runningCount", "5");

        Assert.assertTrue(s.conditionMet(0));
        Assert.assertTrue(s.conditionMet(1));
        Assert.assertTrue(s.conditionMet(2));
    }

    @Test
    public void testEval() throws Exception {
        CustomPlayerBetStrategy s = new CustomPlayerBetStrategy("3", "=", "1 +2", "5");

        Assert.assertTrue(s.conditionMet(0));
    }

    @Test
    public void testEvalReplace() throws Exception {
        CustomPlayerBetStrategy s = new CustomPlayerBetStrategy("3", "<", "runningCount+9", "5");

        Assert.assertTrue(s.conditionMet(0));
        Assert.assertFalse(s.conditionMet(-10));
    }

    @Test
    public void testEvalDivision() throws Exception {
        CustomPlayerBetStrategy s = new CustomPlayerBetStrategy("3", "<", "runningCount/3", "5");

        Assert.assertTrue(s.conditionMet(12));
        Assert.assertFalse(s.conditionMet(1));
    }

}