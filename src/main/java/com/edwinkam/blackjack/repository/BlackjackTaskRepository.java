package com.edwinkam.blackjack.repository;

import com.edwinkam.blackjack.model.BlackjackTask;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlackjackTaskRepository extends MongoRepository<BlackjackTask, String> {

    // public BlackjackTask findByUuid(String trackingUuid);

}
