package com.edwinkam.blackjack.model.simulator;

import com.edwinkam.blackjack.model.strategy.CustomPlayerBetStrategy;
import com.edwinkam.blackjack.model.strategy.GetPlayerBetRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class SimulatorRequest {
    private int numOfGame;
    private String trackingUuid;
    private boolean useRunningCount;
    private List<CustomPlayerBetStrategy> customPlayerBetStrategies = new ArrayList<>();
    private long creationTimeStamp = System.currentTimeMillis();

    public SimulatorRequest(int numOfGame) {
        this.numOfGame = numOfGame;
    }

    public void addCustomerPlayerBetStrategy(CustomPlayerBetStrategy customPlayerBetStrategy) {
        this.customPlayerBetStrategies.add(customPlayerBetStrategy);
    }
}
