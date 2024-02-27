package com.edwinkam.blackjack.model.strategy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class BetStrategyValidationResponse {
    private int runningCount;
    private double bet;
}
