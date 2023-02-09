package com.sj.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sj.annotation.RequireLogin;
import com.sj.annotation.UserParam;
import com.sj.domain.*;
import com.sj.mongo.domain.StrategyComment;
import com.sj.mongo.query.StrategyCommentQuery;
import com.sj.mongo.service.IStrategyCommentService;
import com.sj.query.StrategyQuery;
import com.sj.redis.service.IStrategyStatisVORedisService;
import com.sj.redis.service.IUserInfoRedisService;
import com.sj.redis.vo.StrategyStatisVO;
import com.sj.service.IStrategyConditionService;
import com.sj.service.IStrategyRankService;
import com.sj.service.IStrategyService;
import com.sj.service.IStrategyThemeService;
import com.sj.util.JsonResult;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("strategies")
public class StrategyController {



    @Autowired
    private IStrategyService strategyService;

    @Autowired
    private IStrategyThemeService strategyThemeService;

    @Autowired
    private IStrategyRankService strategyRankService;

    @Autowired
    private IStrategyConditionService strategyConditionService;

    @Autowired
    private IUserInfoRedisService userInfoRedisService;

    @Autowired
    private IStrategyCommentService strategyCommentService;



    @Autowired
    private IStrategyStatisVORedisService strategyStatisVORedisService;


    @GetMapping("/content")
    public JsonResult content(Long id){
        StrategyContent content = strategyService.getContent(id);
        return JsonResult.success(content);
    }

    @GetMapping("/detail")
    public JsonResult detail(Long id){
        StrategyContent content = strategyService.getContent(id);
        Strategy strategy = strategyService.getById(id);
        strategy.setContent(content);

        //阅读数 + 1
        strategyStatisVORedisService.veiwnumIncr(id);
        return JsonResult.success(strategy);
    }
    @GetMapping("/statisVo")
    public JsonResult statisVo(Long sid){
        StrategyStatisVO vo = strategyStatisVORedisService.getStatisVO(sid);
        return JsonResult.success(vo);
    }


    @GetMapping("/themes")
    public JsonResult themes(){
        return JsonResult.success(strategyThemeService.list());
    }

    @GetMapping("/query")
    public JsonResult query(StrategyQuery qo){
        IPage<Strategy> page = strategyService.queryPage(qo);
        return JsonResult.success(page);
    }

    @GetMapping("/ranks")
    public JsonResult ranks(int type){
        List<StrategyRank> ranks = strategyRankService.queryByType(type);
        return JsonResult.success(ranks);
    }

    @GetMapping("/conditions")
    public JsonResult conditions(int type){
        List<StrategyCondition> conditions = strategyConditionService.queryByType(type);
        return JsonResult.success(conditions);
    }

    @RequireLogin
    @PostMapping("/commentAdd")
    public JsonResult commentAdd(StrategyComment comment, HttpServletRequest request){

        String token = request.getHeader("token");
        UserInfo user = userInfoRedisService.getUserByToken(token);
        //bean属性复制
        //参数1：源对象  ctrl + c  参数2：目标对象， ctrl + v
        //底层实现：javabean内省
        //操作注意点：2个对象的属性名称与类型要一致
        BeanUtils.copyProperties(user, comment);
        comment.setUserId(user.getId());
        strategyCommentService.save(comment);


        //评论数 + 1
        strategyStatisVORedisService.replynumIncr(comment.getStrategyId());

        return JsonResult.success();
    }

    @GetMapping("/comments")
    public JsonResult comments(StrategyCommentQuery qo){
        Page<StrategyComment> page = strategyCommentService.queryPage(qo);
        return JsonResult.success(page);
    }


    @RequireLogin
    @PostMapping("/favor")
    public JsonResult favor(Long sid, @UserParam UserInfo userInfo){
        boolean ret = strategyStatisVORedisService.favor(sid,userInfo.getId());
        return JsonResult.success(ret);
    }

    @RequireLogin
    @PostMapping("/strategyThumbup")
    public JsonResult strategyThumbup(Long sid, @UserParam UserInfo userInfo){
        boolean ret = strategyStatisVORedisService.strategyThumbup(sid,userInfo.getId());
        return JsonResult.success(ret);
    }


}
