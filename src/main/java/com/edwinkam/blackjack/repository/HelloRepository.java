package com.edwinkam.blackjack.repository;

import com.edwinkam.blackjack.model.hello.HelloTask;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HelloRepository extends MongoRepository<HelloTask, Integer> {
}
