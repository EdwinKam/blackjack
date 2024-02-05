package com.edwinkam.blackjack.controller;

import com.edwinkam.blackjack.model.hello.HelloTask;
import com.edwinkam.blackjack.model.simulator.SimulatorRequest;
import com.edwinkam.blackjack.repository.HelloRepository;
import com.edwinkam.blackjack.service.HelloService;
import com.edwinkam.blackjack.service.simulator.SimulatorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private HelloService helloService;

    @Autowired
    private SimulatorService simulatorService;

    @Autowired
    private HelloRepository helloRepository;

    @CrossOrigin
    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello, World from springboot!";
    }

    @CrossOrigin
    @PostMapping("/add-to-queue")
    public String addToQueue(@RequestParam Integer number) throws InterruptedException {
        try {
            return String.valueOf(helloService.addRequest(number));
        } catch (Exception e) {
            return "error";
        }
    }

    @CrossOrigin
    @PostMapping("/check")
    public String check(@RequestParam String trackingUuid) throws InterruptedException {
        try {
            return String.valueOf(helloService.checkState(trackingUuid));
        } catch (Exception e) {
            return "error";
        }
    }

    @CrossOrigin
    @PostMapping("/stop")
    public String stop(@RequestParam String trackingUuid) throws InterruptedException {
        try {
            helloService.stopJob(trackingUuid);
            return "stop done";
        } catch (Exception e) {
            return "error";
        }
    }

    @CrossOrigin
    @PostMapping("/stopall")
    public String stopAll() throws InterruptedException {
        try {
            helloService.stopAllJobs();
            return "stop done";
        } catch (Exception e) {
            return "error";
        }
    }

    @CrossOrigin
    @PostMapping("/tests")
    public String tests() throws Exception {
        SimulatorRequest reqest = new SimulatorRequest(100);
        reqest.setTrackingUuid("test");
        return simulatorService.simulate(reqest).toString();
    }

    @CrossOrigin
    @PostMapping("/addHello")
    public String addHello(@RequestBody HelloTask helloTask) {
        try {
            helloRepository.save(helloTask);
            return "done";
        } catch (Exception e) {
            return e.getMessage();

        }

    }

}