package com.edwinkam.blackjack.model.simulator;

import com.edwinkam.blackjack.model.strategy.GetPlayerBetRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SimulatorRequest {
    private int numOfGame;
    private String trackingUuid;
    private boolean useRunningCount;
    private GetPlayerBetRequest getPlayerBetRequest;

    public SimulatorRequest(int numOfGame) {
        this.numOfGame = numOfGame;
    }
}
