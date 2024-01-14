package com.edwinkam.blackjack.model.simulator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SimulatorRequest {
    private int numOfGame;
    private String trackingUuid;

    public SimulatorRequest(int numOfGame) {
        this.numOfGame = numOfGame;
    }
}
