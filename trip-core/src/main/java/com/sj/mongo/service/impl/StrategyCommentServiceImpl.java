package com.sj.mongo.service.impl;


import com.sj.mongo.domain.StrategyComment;
import com.sj.mongo.query.StrategyCommentQuery;
import com.sj.mongo.repository.StrategyCommentRepository;
import com.sj.mongo.service.IStrategyCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
//@Transactional
public class StrategyCommentServiceImpl implements IStrategyCommentService {

    @Autowired
    private StrategyCommentRepository repository;
    @Autowired
    private MongoTemplate template;



    @Override
    public void save(StrategyComment comment) {
        //明确设置为null， 因为spring-data-mongodb操作特点，如果id为 空串 ""
        //save成功之后，不会将自动生成id设置会comment对象
        comment.setId(null);
        comment.setCreateTime(new Date());
        repository.save(comment);
    }

    @Override
    public Page<StrategyComment> queryPage(StrategyCommentQuery qo) {
        //spring-data-mongodb 没有现成的条件分页api，需要自行封装
        //思考：分页对象怎么封装？
        //PageResult：
        //页面传入： currentPage   pageSqize
        //查询： list   totalCount
        //计算出来：totalPage  prePage  nextPage
        //抽象MQL语句工具类:Query
        Query query = new Query();
        //条件
        if (qo.getStrategyId() != null){
            query.addCriteria(Criteria.where("strategyId").is(qo.getStrategyId()));
        }
        //计算总数：
        // MQL db.StrategyComment.find({strategyId:xx}).count();
        // SQL select count(id) from StrategyComment where strategyId = xxx
        long totalCount = template.count(query, StrategyComment.class);
        if(totalCount == 0){
            return Page.empty();
        }
        //MQL: db.StrategyComment.find({strategyId:xx}).skip(?).limit(?)
        //SQL： select * from StrategyComment where strategyId = xxx limit ？，？
        //方案1：
        //query.skip( (qo.getCurrentPage() - 1) * qo.getPageSize()  ).limit(qo.getPageSize());
        //方案2: 分页参数封装对象， 参数1：当前页， 从0， 参数2：每页显示条数
        Pageable pageable = PageRequest.of(qo.getCurrentPage() - 1, qo.getPageSize());
        query.with(pageable);
        List<StrategyComment> list = template.find(query, StrategyComment.class);
        return new PageImpl<StrategyComment>(list, pageable, totalCount);
    }
}
