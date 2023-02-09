package com.sj.mongo.repository;


import com.sj.mongo.domain.StrategyComment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StrategyCommentRepository extends MongoRepository<StrategyComment, String> {
}
