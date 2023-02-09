package com.sj.config;


import com.sj.interceptor.CheckLoginInterceptor;
import com.sj.resolver.UserInfoArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer{

//    @Autowired
//    private CheckLoginInterceptor checkLoginInterceptor;
    //实例方法，将返回对象放置到spring容器管理起来
    @Bean
    public CheckLoginInterceptor checkLoginInterceptor(){
        return new CheckLoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkLoginInterceptor())
                //registry.addInterceptor(new CheckLoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/users/checkPhone")
                .excludePathPatterns("/users/sendVerifyCode")
                .excludePathPatterns("/users/regist")
                .excludePathPatterns("/users/login");
        //防刷
        //registry.addInterceptor(brushProofInterceptor())
        //.addPathPatterns("/**");
        /*//签名
        registry.addInterceptor(signInterceptor())
                .addPathPatterns("/**");*/


    }
    //跨域访问
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            //重写父类提供的跨域请求处理的接口
            public void addCorsMappings(CorsRegistry registry) {
                //添加映射路径
                registry.addMapping("/**")
                        //放行哪些原始域
                        .allowedOriginPatterns("*")
                        //是否发送Cookie信息
                        .allowCredentials(true)
                        //放行哪些原始域(请求方式)
                        .allowedMethods("GET", "POST", "PUT", "DELETE","OPTIONS")
                        //放行哪些原始域(头部信息)
                        .allowedHeaders("*")
                        //暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
                        .exposedHeaders("Header1", "Header2");
            }
        };
    }

    //自定义参数解析器
    @Bean
    public UserInfoArgumentResolver userInfoArgumentResolver(){
        return new UserInfoArgumentResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userInfoArgumentResolver());
    }
}
