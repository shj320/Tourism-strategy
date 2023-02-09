package com.sj.mongo.service;


import com.sj.mongo.domain.TravelComment;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 游记评论服务接口
 */
public interface ITravelCommentService {

    /**
     * 添加
     * @param comment
     */
    void save(TravelComment comment);


    /**
     * 查询指定游记下的评论类表
     * @param travelId
     * @return
     */
    List<TravelComment> findByTravelId(Long travelId);
}
