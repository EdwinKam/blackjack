package com.edwinkam.blackjack.cache;

import com.edwinkam.blackjack.model.simulator.SimulatorResponse;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class SimulatorResultCache implements KVCache<String, SimulatorResponse>{
    private ConcurrentHashMap<String, SimulatorResponse> concurrentHashMap;

    public SimulatorResultCache() {
        this.concurrentHashMap = new ConcurrentHashMap<>();
    }
    public void put(String key, SimulatorResponse value) {
        concurrentHashMap.put(key, value);
    }

    public SimulatorResponse get(String key) {
        return concurrentHashMap.get(key);
    }
}
