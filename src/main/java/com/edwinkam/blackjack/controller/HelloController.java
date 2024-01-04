package com.edwinkam.blackjack.controller;

import com.edwinkam.blackjack.service.BlackjackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private BlackjackService blackjackService;

    @CrossOrigin
    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello, World from springboot!";
    }

    @PostMapping("/add-to-queue")
    public String addToQueue(@RequestParam Integer number) throws InterruptedException {
        try {
            return String.valueOf(blackjackService.addRequest(number));
        } catch (Exception e) {
            return "error";
        }
    }

    @PostMapping("/check")
    public String check(@RequestParam String trackingUuid) throws InterruptedException {
        try {
            return String.valueOf(blackjackService.checkState(trackingUuid));
        } catch (Exception e) {
            return "error";
        }
    }

    @PostMapping("/stop")
    public String stop(@RequestParam String trackingUuid) throws InterruptedException {
        try {
            blackjackService.stopJob(trackingUuid);
            return "stop done";
        } catch (Exception e) {
            return "error";
        }
    }

    @PostMapping("/stopall")
    public String stopAll() throws InterruptedException {
        try {
            blackjackService.stopAllJobs();
            return "stop done";
        } catch (Exception e) {
            return "error";
        }
    }

}