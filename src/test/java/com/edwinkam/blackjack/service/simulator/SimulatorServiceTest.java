package com.edwinkam.blackjack.service.simulator;

import com.edwinkam.blackjack.model.simulator.SimulatorRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SimulatorServiceTest {
    private SimulatorService simulatorService;

    @BeforeEach
    public void init() {
        simulatorService = new SimulatorService();
    }
    @Test
    public void testSimulate() {
        SimulatorRequest request = new SimulatorRequest(3);

        System.out.println(simulatorService.simulate(request));
    }

}