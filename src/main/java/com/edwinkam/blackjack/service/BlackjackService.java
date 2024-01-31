package com.edwinkam.blackjack.service;

import com.edwinkam.blackjack.cache.SimulatorProgressCache;
import com.edwinkam.blackjack.cache.SimulatorResultCache;
import com.edwinkam.blackjack.client.StatusMessage;
import com.edwinkam.blackjack.model.simulator.SimulatorRequest;
import com.edwinkam.blackjack.model.simulator.SimulatorResponse;
import com.edwinkam.blackjack.queue.SimulatorRequestQueue;
import com.edwinkam.blackjack.service.simulator.SimulatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;

@Service
public class BlackjackService {
    private int simulatorMaxThreads = 8;

    @Autowired
    SimulatorRequestQueue simulatorRequestQueue;

    @Autowired
    SimulatorService simulatorService;

    @Autowired
    SimulatorResultCache simulatorResultCache;

    @Autowired
    private SimulatorProgressCache simulatorProgressCache;

    @Autowired
    @Qualifier("simulatorExecutor")
    private Executor taskExecutor;

    @PostConstruct
    public void init() {
        for (int i = 0; i < simulatorMaxThreads; i++) {
            taskExecutor.execute(this::listenToSimulatorRequests);
        }
    }

    private void listenToSimulatorRequests() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // blackjackClient.java put in the request
                SimulatorRequest request = simulatorRequestQueue.take();
                try {
                    long startTime = System.currentTimeMillis();
                    System.out.printf("started %d\n", request.getNumOfGame());
                    SimulatorResponse response = simulatorService.simulate(request);
                    System.out.printf("game %d took %fs\n", request.getNumOfGame(), (double) (System.currentTimeMillis() - startTime) / 1000);
                    simulatorResultCache.put(request.getTrackingUuid(), response);
                    simulatorProgressCache.put(request.getTrackingUuid(), 10);
                } catch (Exception e) {
                    simulatorProgressCache.put(request.getTrackingUuid(), -1);
                    System.err.printf(Thread.currentThread().getName() + e.getMessage());
                }
            } catch (Exception e) {
                System.err.printf("Exception while polling request queue");
            }
        }
    }
}
