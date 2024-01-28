package com.edwinkam.blackjack.client;

import com.edwinkam.blackjack.cache.SimulatorProgressCache;
import com.edwinkam.blackjack.cache.SimulatorResultCache;
import com.edwinkam.blackjack.model.simulator.SimulatorRequest;
import com.edwinkam.blackjack.model.simulator.SimulatorResponse;
import com.edwinkam.blackjack.queue.SimulatorRequestQueue;
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

    private List<String> trackingUuids = new ArrayList<>();

    public String submitSimulatorRequest(SimulatorRequest request) {
        String trackingUuid = UUID.randomUUID().toString();
        request.setTrackingUuid(trackingUuid);
        request.setUseRunningCount(true);
        // blackjackService.java will pick up the request when available
        simulatorRequestQueue.add(request);
        trackingUuids.add(trackingUuid);
        simulatorProgressCache.put(trackingUuid, 0);
        return trackingUuid;
    }

    public String checkSimulatorProgress(String trackingUuid) {
        Integer progress = simulatorProgressCache.get(trackingUuid);
        if (progress == null) {
            return "Not found trackingUuid";
        } else {
            if (progress == 10) {
                return "completed";
            }
            return (progress * 10) +"%";
        }
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
        return simulatorResultCache.get(trackingUuid);
    }

    public String[] getAllTrackingUuid() {
        return trackingUuids.toArray(new String[0]);
    }
}
