package com.edwinkam.blackjack.controller;

import com.edwinkam.blackjack.client.BlackjackClient;
import com.edwinkam.blackjack.model.simulator.SimulatorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/blackjack")
public class BlackjackController {
    @Autowired
    BlackjackClient blackjackClient;

    @CrossOrigin
    @PostMapping("/simulateRequest")
    public String simulate(@RequestParam Integer numOfGame) {
        String trackingUuid = blackjackClient.submitSimulatorRequest(new SimulatorRequest(numOfGame));
        return trackingUuid;
    }

    @CrossOrigin
    @PostMapping("/checkProgress")
    public String checkProgress(@RequestParam String trackingUuid) {
        return blackjackClient.checkSimulatorProgress(trackingUuid);
    }

    @CrossOrigin
    @PostMapping("/batchCheckProgress")
    public Map<String, String> batchCheckProgress(@RequestBody String[] trackingUuids) {
        return blackjackClient.checkBatchSimulatorProgress(trackingUuids);
    }

    @CrossOrigin
    @PostMapping("/checkResult")
    public String checkResult(@RequestParam String trackingUuid) {
        return blackjackClient.checkSimulatorResult(trackingUuid);
    }
}
