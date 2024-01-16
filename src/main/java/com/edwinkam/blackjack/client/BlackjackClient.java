package com.edwinkam.blackjack.client;

import com.edwinkam.blackjack.cache.SimulatorProgressCache;
import com.edwinkam.blackjack.cache.SimulatorResultCache;
import com.edwinkam.blackjack.model.simulator.SimulatorRequest;
import com.edwinkam.blackjack.queue.SimulatorRequestQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BlackjackClient {
    @Autowired
    SimulatorRequestQueue simulatorRequestQueue;

    @Autowired
    SimulatorProgressCache simulatorProgressCache;

    @Autowired
    SimulatorResultCache simulatorResultCache;

    public String submitSimulatorRequest(SimulatorRequest request) {
        String trackingUuid = UUID.randomUUID().toString();
        request.setTrackingUuid(trackingUuid);
        request.setUseRunningCount(true);
        // blackjackService.java will pick up the request when available
        simulatorRequestQueue.add(request);
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

    public String checkSimulatorResult(String trackingUuid) {
        return simulatorResultCache.get(trackingUuid);
    }
}
