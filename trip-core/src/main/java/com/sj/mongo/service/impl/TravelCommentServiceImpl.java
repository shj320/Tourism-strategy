package com.sj.mongo.service.impl;


import com.sj.mongo.domain.TravelComment;
import com.sj.mongo.repository.TravelCommentRepository;
import com.sj.mongo.service.ITravelCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
//@Transactional
public class TravelCommentServiceImpl implements ITravelCommentService {

    @Autowired
    private TravelCommentRepository repository;
    @Autowired
    private MongoTemplate template;



    @Override
    public void save(TravelComment comment) {
        //明确设置为null， 因为spring-data-mongodb操作特点，如果id为 空串 ""
        //save成功之后，不会将自动生成id设置会comment对象
        comment.setId(null);
        comment.setCreateTime(new Date());

        //关联的评论
        String refid = comment.getRefComment().getId();
        if(StringUtils.hasText(refid)){
            //评论的评论
            TravelComment refComment = repository.findById(refid).get();
            comment.setRefComment(refComment);
            comment.setType(TravelComment.TRAVLE_COMMENT_TYPE);
        }else{

            //普通评论
            comment.setType(TravelComment.TRAVLE_COMMENT_TYPE_COMMENT);
        }


        repository.save(comment);
    }

    @Override
    public List<TravelComment> findByTravelId(Long travelId) {
        return repository.findByTravelId(travelId);
    }
}
