package com.edwinkam.blackjack.model.simulator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SimulatorRequest {
    private int numOfGame;
    private String trackingUuid;
    private boolean useRunningCount;

    public SimulatorRequest(int numOfGame) {
        this.numOfGame = numOfGame;
    }
}
