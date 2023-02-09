package com.sj.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sj.annotation.RequireLogin;
import com.sj.annotation.UserParam;
import com.sj.domain.Travel;
import com.sj.domain.TravelContent;
import com.sj.domain.UserInfo;
import com.sj.mongo.domain.TravelComment;
import com.sj.mongo.service.ITravelCommentService;
import com.sj.query.TravelQuery;
import com.sj.redis.service.IUserInfoRedisService;
import com.sj.service.ITravelService;
import com.sj.service.IUserInfoService;
import com.sj.util.JsonResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("travels")
public class TravelController {



    @Autowired
    private ITravelService travelService;
    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private ITravelCommentService travelCommentService;
    @Autowired
    private IUserInfoRedisService userInfoRedisService;



    @GetMapping("/content")
    public JsonResult content(Long id){
        TravelContent content = travelService.getContent(id);
        return JsonResult.success(content);
    }

    @GetMapping("/detail")
    public JsonResult detail(Long id){
        TravelContent content = travelService.getContent(id);
        Travel travel = travelService.getById(id);
        travel.setContent(content);
        travel.setAuthor(userInfoService.getById(travel.getAuthorId()));
        return JsonResult.success(travel);
    }


    @GetMapping("/query")
    public JsonResult query(TravelQuery qo){
        IPage<Travel> page = travelService.queryPage(qo);
        return JsonResult.success(page);
    }

    @GetMapping("/query1")
    public JsonResult querys(int orderType){
        IPage<Travel> page = travelService.queryPage1(orderType);
        return JsonResult.success(page);
    }




//    @GetMapping("/queryS")
//    public JsonResult query(){
//        IPage<Travel> page = travelService.queryPages();
//        return JsonResult.success(page);
//    }

    @GetMapping("/comments")
    public JsonResult comments(Long travelId){

        return JsonResult.success(travelCommentService.findByTravelId(travelId));
    }



    @GetMapping("/info")
    public JsonResult info(@UserParam UserInfo userInfo) {
        return JsonResult.success(userInfo);
    }


    @GetMapping("/updateInfo")
    public JsonResult updateInfo(UserInfo userInfo) {
        return JsonResult.success(userInfo);
    }



    @RequireLogin
    @PostMapping("/commentAdd")
    public JsonResult commentAdd(TravelComment comment, HttpServletRequest request){
        String token = request.getHeader("token");
        UserInfo user = userInfoRedisService.getUserByToken(token);
        //bean属性复制
        //参数1：源对象  ctrl + c  参数2：目标对象， ctrl + v
        //底层实现：javabean内省
        //操作注意点：2个对象的属性名称与类型要一致
        BeanUtils.copyProperties(user, comment);
        comment.setUserId(user.getId());
        travelCommentService.save(comment);
        return JsonResult.success();
    }


}
