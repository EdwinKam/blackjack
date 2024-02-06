package com.edwinkam.blackjack.model.simulator;

import com.edwinkam.blackjack.model.strategy.CustomPlayerBetStrategy;
import com.edwinkam.blackjack.model.strategy.GetPlayerBetRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "simulate-request")
public class SimulatorRequest {
    @Id
    private String trackingUuid;
    private int numOfGame;
    private boolean useRunningCount;
    private List<CustomPlayerBetStrategy> customPlayerBetStrategies = new ArrayList<>();
    private long creationTimeStamp = System.currentTimeMillis();
    private int requesterId;

    public SimulatorRequest(int numOfGame) {
        this.numOfGame = numOfGame;
    }

    public void addCustomerPlayerBetStrategy(CustomPlayerBetStrategy customPlayerBetStrategy) {
        this.customPlayerBetStrategies.add(customPlayerBetStrategy);
    }
}
