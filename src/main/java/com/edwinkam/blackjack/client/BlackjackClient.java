package com.edwinkam.blackjack.client;

import com.edwinkam.blackjack.cache.SimulatorProgressCache;
import com.edwinkam.blackjack.cache.SimulatorRequestCache;
import com.edwinkam.blackjack.cache.SimulatorResultCache;
import com.edwinkam.blackjack.model.simulator.SimulatorRequest;
import com.edwinkam.blackjack.model.simulator.SimulatorResponse;
import com.edwinkam.blackjack.queue.SimulatorRequestQueue;
import com.edwinkam.blackjack.repository.SimulateRequestRepository;
import com.edwinkam.blackjack.repository.SimulateResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BlackjackClient {
    @Autowired
    SimulatorRequestQueue simulatorRequestQueue;

    @Autowired
    SimulatorProgressCache simulatorProgressCache;

    @Autowired
    SimulatorResultCache simulatorResultCache;

    @Autowired
    SimulatorRequestCache simulatorRequestCache;

    @Autowired
    SimulateRequestRepository simulateRequestRepository;

    @Autowired
    SimulateResponseRepository simulateResponseRepository;

    private List<String> trackingUuids = new ArrayList<>();

    public String submitSimulatorRequest(SimulatorRequest request) {
        String trackingUuid = UUID.randomUUID().toString();
        request.setTrackingUuid(trackingUuid);
        request.setUseRunningCount(true);
        // blackjackService.java will pick up the request when available
        trackingUuids.add(trackingUuid);
        simulatorRequestCache.put(trackingUuid, request);
        simulateRequestRepository.save(request);
        simulatorRequestQueue.add(request);

        simulatorProgressCache.put(trackingUuid, 0);

        return trackingUuid;
    }

    public String checkSimulatorProgress(String trackingUuid) {
        Integer progress = simulatorProgressCache.get(trackingUuid);
        if (progress == null) {
            // if found in result db, this simulation was completed
            progress = simulateResponseRepository.findByTrackingUuid(trackingUuid) == null ? null : 10;
        }

        if (progress == null) {
            // didn't find in both cache and db
            return "Not found trackingUuid";
        }

        if (progress == 10) {
            return StatusMessage.COMPLETED.name();
        } else if (progress == -1) {
            return StatusMessage.FAILURE.name();
        }
        return (progress * 10) +"%";
    }

    public Map<String, String> checkBatchSimulatorProgress(String[] trackingUuids) {
        Map<String, String> progressMap = new HashMap<>();
        for (String trackingUuid: trackingUuids) {
            String progress = checkSimulatorProgress(trackingUuid);
            progressMap.put(trackingUuid, progress);
        }
        return progressMap;
    }

    public SimulatorResponse checkSimulatorResult(String trackingUuid) {
        return simulateResponseRepository.findByTrackingUuid(trackingUuid);
    }

    public Map<String, SimulatorRequest> getAllTrackingUuid() {
        Map<String, SimulatorRequest> results = new HashMap<>();
        for (String trackingUuid: trackingUuids) {
            results.put(trackingUuid, simulatorRequestCache.get(trackingUuid));
        }
        return results;
    }
}
