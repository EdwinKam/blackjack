package com.edwinkam.blackjack.cache;

public interface KVCache<K, V> {
    void put(K key, V value);
    V get(K key);
}
