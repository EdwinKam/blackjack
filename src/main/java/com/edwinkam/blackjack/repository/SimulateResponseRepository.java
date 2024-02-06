package com.edwinkam.blackjack.repository;

import com.edwinkam.blackjack.model.simulator.SimulatorResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SimulateResponseRepository extends MongoRepository<SimulatorResponse, String> {
    public SimulatorResponse findByTrackingUuid(String trackingUuid);

}
