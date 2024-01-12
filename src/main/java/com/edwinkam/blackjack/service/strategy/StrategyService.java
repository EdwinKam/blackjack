package com.edwinkam.blackjack.service.strategy;

import com.edwinkam.blackjack.model.game.PlayerAction;
import com.edwinkam.blackjack.model.poker.Hand;
import org.springframework.stereotype.Service;

@Service
public class StrategyService {

    public PlayerAction getPlayerAction(Hand player, Hand dealer) {
        return PlayerAction.STAND;
    }
}
