package com.edwinkam.blackjack.model.strategy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class GetPlayerBetRequest {
    private List<CustomPlayerBetStrategy> strategies = new ArrayList<>();
    private int currentRunningCount;

    public void addStrategy(CustomPlayerBetStrategy newStrategy) {
        this.strategies.add(newStrategy);
    }
}
