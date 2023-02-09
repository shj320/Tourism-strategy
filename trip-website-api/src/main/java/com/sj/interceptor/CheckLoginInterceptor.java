package com.sj.interceptor;


import com.alibaba.fastjson.JSON;
import com.sj.annotation.RequireLogin;
import com.sj.domain.UserInfo;
import com.sj.redis.service.IUserInfoRedisService;
import com.sj.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CheckLoginInterceptor implements HandlerInterceptor {

    @Autowired
    private IUserInfoRedisService userInfoRedisService;

    //登录拦截器
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //放行跨域预请求
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod hm = (HandlerMethod) handler;


        //判断请求映射方法是否贴有：RequireLogin注解
        if(hm.hasMethodAnnotation(RequireLogin.class)){
            //进行登录检查
            String token = request.getHeader("token");
            UserInfo userInfo = userInfoRedisService.getUserByToken(token);
            if(userInfo == null){
                //没登录
                //响应没登录结果提示
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(JSON.toJSONString(JsonResult.noLogin()));

                return false;
            }
        }
        //放行
        return true;
    }
}
