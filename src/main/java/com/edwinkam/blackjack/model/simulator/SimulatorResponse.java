package com.edwinkam.blackjack.model.simulator;

import com.edwinkam.blackjack.model.game.GameRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@Document(collection = "simulate-result")
public class SimulatorResponse {
    @Id
    private String trackingUuid;
    private List<GameRecord> gameRecords;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (GameRecord record: gameRecords) {
            sb.append(record);
        }
        return sb.toString();
    }
}
