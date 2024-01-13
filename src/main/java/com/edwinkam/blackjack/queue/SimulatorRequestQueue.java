package com.edwinkam.blackjack.queue;

import com.edwinkam.blackjack.model.BlackjackTask;
import com.edwinkam.blackjack.model.simulator.SimulatorRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class SimulatorRequestQueue implements RequestQueue<SimulatorRequest> {
    private BlockingQueue<SimulatorRequest> requestQueue;

    public SimulatorRequestQueue() {
        this.requestQueue = new LinkedBlockingQueue<>();
    }

    public SimulatorRequest take() throws Exception {
        return requestQueue.take();
    }

    public void add(SimulatorRequest request) {
        requestQueue.add(request);
    }
}
