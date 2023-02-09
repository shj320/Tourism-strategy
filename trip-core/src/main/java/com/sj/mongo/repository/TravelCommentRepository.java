package com.sj.mongo.repository;


import com.sj.mongo.domain.TravelComment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TravelCommentRepository extends MongoRepository<TravelComment, String> {

    /**
     * 查询指定游记下的评论类表
     * @param travelId
     * @return
     */
    List<TravelComment> findByTravelId(Long travelId);
}
