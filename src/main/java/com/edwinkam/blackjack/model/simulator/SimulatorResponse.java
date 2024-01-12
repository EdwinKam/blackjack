package com.edwinkam.blackjack.model.simulator;

import com.edwinkam.blackjack.model.game.GameRecord;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SimulatorResponse {
    private List<GameRecord> gameRecords;
}
