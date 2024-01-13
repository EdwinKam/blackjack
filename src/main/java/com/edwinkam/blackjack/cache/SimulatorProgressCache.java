package com.edwinkam.blackjack.cache;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class SimulatorProgressCache implements KVCache<String, Integer>{
    private ConcurrentHashMap<String, Integer> concurrentHashMap;

    public SimulatorProgressCache() {
        this.concurrentHashMap = new ConcurrentHashMap<>();
    }
    public void put(String key, Integer value) {
        concurrentHashMap.put(key, value);
    }

    public Integer get(String key) {
        return concurrentHashMap.get(key);
    }
}
