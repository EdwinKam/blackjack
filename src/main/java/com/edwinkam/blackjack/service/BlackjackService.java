package com.edwinkam.blackjack.service;

import com.edwinkam.blackjack.model.BlackjackTask;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class BlackjackService {

    private BlockingQueue<BlackjackTask> requestQueue;
    private ConcurrentHashMap<String, String> concurrentHashMap;

    public BlackjackService(@Qualifier("blackjackExecutor") Executor taskExecutor) {
        this.requestQueue = new LinkedBlockingQueue<>();
        this.concurrentHashMap = new ConcurrentHashMap<>();
        for (int i = 0; i < 2; i++) {
            taskExecutor.execute(this::startProcessing);
        }
    }

    public String addRequest(int i) throws Exception {
        String trackingUuid = UUID.randomUUID().toString();
        requestQueue.put(new BlackjackTask(i, trackingUuid));
        concurrentHashMap.put(trackingUuid, "not yet been picked up");
        return trackingUuid;
    }

    public String checkState(String trackingUuid) {
        return concurrentHashMap.get(trackingUuid);
    }

    public void stopJob(String trackingUuid) {
        concurrentHashMap.put(trackingUuid, "stop");
    }

    public void stopAllJobs() {
        requestQueue.clear();
        for (String key : concurrentHashMap.keySet()) {
            concurrentHashMap.put(key, "stop");
        }

    }
    private void startProcessing() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                BlackjackTask task = requestQueue.take();
                System.out.println("start to handle " + task.getNumber());
                for (int i = 0; i < 100; i++) {
                    if (isStopRequested(task.getTrackingUuid())) {
                        break;
                    }
                    System.out.println("handling " + task.getNumber() + "&" + i);
                    Thread.sleep(1000);
                    if (isStopRequested(task.getTrackingUuid())) {
                        break;
                    }
                    concurrentHashMap.put(task.getTrackingUuid(), String.valueOf(i));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private boolean isStopRequested(String trackingUuid) {
        if (concurrentHashMap.get(trackingUuid).equalsIgnoreCase("stop")) {
            concurrentHashMap.put(trackingUuid, "stopped");
            return true;
        } else {
            return false;
        }
    }
}