package com.edwinkam.blackjack.cache;

import com.edwinkam.blackjack.model.simulator.SimulatorRequest;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class SimulatorRequestCache implements KVCache<String, SimulatorRequest> {
    private ConcurrentHashMap<String, SimulatorRequest> concurrentHashMap;

    public SimulatorRequestCache() {
        this.concurrentHashMap = new ConcurrentHashMap<>();
    }
    public void put(String key, SimulatorRequest value) {
        concurrentHashMap.put(key, value);
    }

    public SimulatorRequest get(String key) {
        return concurrentHashMap.get(key);
    }
}
