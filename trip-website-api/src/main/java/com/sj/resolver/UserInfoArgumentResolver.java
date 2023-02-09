package com.sj.resolver;


import com.sj.annotation.UserParam;
import com.sj.domain.UserInfo;
import com.sj.redis.service.IUserInfoRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义参数解析器：用户参数
 * 作用：将请求映射方法中UserInfo类型的参数，解析成当前登录用户对象
 */
public class UserInfoArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private IUserInfoRedisService userInfoRedisService;

    //指定当前解析器能解析类型
    //此处为UserInfo类型
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == UserInfo.class
                && parameter.hasParameterAnnotation(UserParam.class);
    }

    //参数解析逻辑
    //当前操作逻辑：将当前登录用户对象获取并返回
    //当supportsParameter 为true时，才执行resolveArgument 逻辑
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = request.getHeader("token");
        //放回要注入对象即可，注入逻辑mvc帮忙实现
        return userInfoRedisService.getUserByToken(token);
    }
}
