package com.edwinkam.blackjack.service.strategy;

import com.edwinkam.blackjack.model.game.PlayerAction;
import com.edwinkam.blackjack.model.poker.Hand;
import org.springframework.stereotype.Service;

@Service
public class StrategyService {
    private static final PlayerAction H = PlayerAction.HIT;
    private static final PlayerAction S = PlayerAction.STAND;
    private static final PlayerAction D = PlayerAction.DOUBLE;
    private static final PlayerAction P = PlayerAction.SPLIT;
    private final PlayerAction[][] decisionTableHard = {
            // Dealer's card:
        //   2  3  4  5  6  7  8  9  10 A
            {H, H, H, H, H, H, H, H, H, H}, // Player sum 5
            {H, H, H, H, H, H, H, H, H, H}, // Player sum 6
            {H, H, H, H, H, H, H, H, H, H}, // Player sum 7
            {H, H, H, D, D, H, H, H, H, H}, // Player sum 8
            {D, D, D, D, D, D, H, H, H, H}, // Player sum 9
            {D, D, D, D, D, D, D, D, H, H}, // Player sum 10
            {D, D, D, D, D, D, D, D, D, D}, // Player sum 11
            {H, H, S, S, S, H, H, H, H, H}, // Player sum 12
            {S, S, S, S, S, H, H, H, H, H}, // Player sum 13
            {S, S, S, S, S, H, H, H, H, H}, // Player sum 14
            {S, S, S, S, S, H, H, H, H, H}, // Player sum 15
            {S, S, S, S, S, H, H, H, H, H}, // Player sum 16
            {S, S, S, S, S, S, S, S, S, S}, // Player sum 17
            {S, S, S, S, S, S, S, S, S, S}, // Player sum 18
            {S, S, S, S, S, S, S, S, S, S}, // Player sum 19
            {S, S, S, S, S, S, S, S, S, S}, // Player sum 20
            {S, S, S, S, S, S, S, S, S, S}, // Player sum 21
    };

    private final PlayerAction[][] decisionTableSplit = {
            // Dealer's card:
        //   2  3  4  5  6  7  8  9  10 A
            {P, P, P, P, P, P, P, P, P, P}, // Player's card A
            {P, P, P, P, P, H, H, H, H, H}, // Player's card 2
            {P, P, P, P, P, H, H, H, H, H}, // Player's card 3
            {H, H, P, P, H, H, H, H, H, H}, // Player's card 4
            {D, D, D, D, D, H, H, H, H, H}, // Player's card 5
            {P, P, P, P, H, H, H, H, H, H}, // Player's card 6
            {P, P, P, P, P, P, H, H, H, H}, // Player's card 7
            {P, P, P, P, P, P, P, P, P, P}, // Player's card 8
            {P, P, P, P, P, P, P, P, S, S}, // Player's card 9
            {S, S, S, S, S, S, S, S, S, S}, // Player's card 10 (including face cards)
    };

    private final PlayerAction[][] decisionTableAce = {
            // Dealer's card:
        //   2  3  4  5  6  7  8  9  10 A
            {H, H, D, D, D, H, H, H, H, H}, // Player's card A2
            {H, H, D, D, D, H, H, H, H, H}, // Player's card A3
            {H, H, D, D, D, H, H, H, H, H}, // Player's card A4
            {H, H, D, D, D, H, H, H, H, H}, // Player's card A5
            {H, D, D, D, D, H, H, H, H, H}, // Player's card A6
            {S, D, D, D, D, S, S, H, H, H}, // Player's card A7
            {S, S, S, S, S, S, S, S, S, S}, // Player's card A8
            {S, S, S, S, S, S, S, S, S, S}, // Player's card A9
            {S, S, S, S, S, S, S, S, S, S}, // Player's card A10 (including face cards)
    };

    public PlayerAction getPlayerAction(Hand player, Hand dealer) {
        int playerSum = player.getSum();
        int dealerHead = dealer.getHand().get(0).getValue();  // Assume we can only see one dealer card
        if (dealerHead == 1) dealerHead = 11;

        // Check for splitting
        if (player.getHand().size() == 2 && player.getHand().get(0).getNumber() == player.getHand().get(1).getNumber()) {
            int playerOneCardValue = player.getHand().get(0).getValue();
            return decisionTableSplit[playerOneCardValue - 1][dealerHead - 2];
        }

        if (player.isHasAce()) {
            int nonAceCard = player.getHand().get(0).getValue() == 1 ?
                    player.getHand().get(1).getValue() : player.getHand().get(0).getValue();
            return decisionTableAce[nonAceCard - 2][dealerHead - 2];
        }

        // Use the hard decision table
        if (playerSum <= 21) {
            return decisionTableHard[playerSum - 5][dealerHead - 2];
        } else {
            return PlayerAction.STAND;  // No choice but to stand when busted
        }
    }

}
