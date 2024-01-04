package com.edwinkam.blackjack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlackjackTask {
    private Integer number;
    private String trackingUuid;
}
