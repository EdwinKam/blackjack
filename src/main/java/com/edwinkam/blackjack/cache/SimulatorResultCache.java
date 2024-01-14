package com.edwinkam.blackjack.cache;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class SimulatorResultCache implements KVCache<String, String>{
    private ConcurrentHashMap<String, String> concurrentHashMap;

    public SimulatorResultCache() {
        this.concurrentHashMap = new ConcurrentHashMap<>();
    }
    public void put(String key, String value) {
        concurrentHashMap.put(key, value);
    }

    public String get(String key) {
        return concurrentHashMap.get(key);
    }
}
