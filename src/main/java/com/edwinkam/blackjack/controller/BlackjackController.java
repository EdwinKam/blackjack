package com.edwinkam.blackjack.controller;

import com.edwinkam.blackjack.client.BlackjackClient;
import com.edwinkam.blackjack.model.simulator.SimulatorRequest;
import com.edwinkam.blackjack.model.simulator.SimulatorResponse;
import com.edwinkam.blackjack.model.strategy.BetStrategyValidationResponse;
import com.edwinkam.blackjack.model.strategy.CustomPlayerBetStrategy;
import com.edwinkam.blackjack.model.strategy.GetPlayerBetRequest;
import com.edwinkam.blackjack.repository.SimulateRequestRepository;
import com.edwinkam.blackjack.service.strategy.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/blackjack")
public class BlackjackController {
    @Autowired
    BlackjackClient blackjackClient;

    @Autowired
    SimulateRequestRepository simulateRequestRepository;

    @Autowired
    StrategyService strategyService;

    @CrossOrigin
    @PostMapping("/simulateRequest")
    public String simulate(@RequestParam Integer numOfGame, @RequestBody String[][] betStrategies, @RequestParam Integer numOfDecks, @RequestParam Double cutOff) throws Exception {
        SimulatorRequest request = new SimulatorRequest(numOfGame);
        GetPlayerBetRequest betRequest = new GetPlayerBetRequest();
        for (String[] betStrategy: betStrategies) {
            if (betStrategy.length != 4) {
                throw new Exception("expect each betStrategy to be size of 4");
            }
            request.addCustomerPlayerBetStrategy(new CustomPlayerBetStrategy(betStrategy[0], betStrategy[1], betStrategy[2], betStrategy[3]));
        }
        request.setNumOfDecks(numOfDecks);
        request.setCutOff(cutOff);
        String trackingUuid = blackjackClient.submitSimulatorRequest(request);
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
    public SimulatorResponse checkResult(@RequestParam String trackingUuid) {
        return blackjackClient.checkSimulatorResult(trackingUuid);
    }

    @CrossOrigin
    @GetMapping("/getAllSimulateRequest")
    public List<SimulatorRequest> getAllTrackingUuid() {
        return simulateRequestRepository.findAll();
    }

    @CrossOrigin
    @PostMapping("/validateBetStrategy")
    public List<BetStrategyValidationResponse> validateBetStrategy(@RequestBody String[][] betStrategies) throws Exception{
        GetPlayerBetRequest betRequest = new GetPlayerBetRequest();
        for (String[] betStrategy: betStrategies) {
            if (betStrategy.length != 4) {
                throw new Exception("expect each betStrategy to be size of 4");
            }
            betRequest.addStrategy(new CustomPlayerBetStrategy(betStrategy[0], betStrategy[1], betStrategy[2], betStrategy[3]));
        }

        List<BetStrategyValidationResponse> response = new ArrayList<>();
        for (int i = -10; i <= 10; i++) {
            betRequest.setCurrentRunningCount(i);
            response.add(new BetStrategyValidationResponse(i, strategyService.getPlayerBet(betRequest)));
        }
        return response;
    }
}
