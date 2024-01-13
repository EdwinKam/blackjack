package com.edwinkam.blackjack.queue;

public interface RequestQueue<T> {
    T take() throws Exception; // poll or wait
    void add(T request);

}
