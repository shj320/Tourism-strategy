package com.sj.mongo.service;


import com.sj.mongo.domain.StrategyComment;
import com.sj.mongo.query.StrategyCommentQuery;
import org.springframework.data.domain.Page;

/**
 * 攻略评论服务接口
 */
public interface IStrategyCommentService  {

    /**
     * 添加
     * @param comment
     */
    void save(StrategyComment comment);

    /**
     * 分页查询
     * @param qo
     * @return
     */
    Page<StrategyComment> queryPage(StrategyCommentQuery qo);
}
