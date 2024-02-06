package com.edwinkam.blackjack.repository;

import com.edwinkam.blackjack.model.simulator.SimulatorRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimulateRequestRepository extends MongoRepository<SimulatorRequest, String> {
    List<SimulatorRequest> findAllByRequesterId(int requesterId);

    List<SimulatorRequest> findAll();
}
