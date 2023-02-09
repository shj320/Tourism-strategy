package com.sj.redis.service.impl;


import com.alibaba.fastjson.JSON;
import com.sj.domain.UserInfo;
import com.sj.exception.LogicException;
import com.sj.redis.service.IUserInfoRedisService;
import com.sj.redis.util.RedisKeys;
import com.sj.util.AssertUtil;
import com.sj.util.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserInfoRedisServiceImpl implements IUserInfoRedisService {

    @Autowired
    private StringRedisTemplate template;


    @Override
    public void setVerifyCode(String phone, String code) {
        String key=Consts.VERIFY_CODE+Consts.SPLIT+phone;
        template.opsForValue().set(key,code, Consts.VERIFY_CODE_VAI_TIME*60L,TimeUnit.SECONDS);
    }

    @Override
    public String getVerifyCode(String phone) {
        String key=Consts.VERIFY_CODE+Consts.SPLIT+phone;
        String code = template.opsForValue().get(key);
        return code;
    }

    @Override
    public String createAndSaveToken(UserInfo user) {
        if(user==null){
            throw new LogicException("用户为空");
        }
        String token=UUID.randomUUID().toString();
        String key=RedisKeys.USER_LOGIN_TOKEN.join(token);
        String value= JSON.toJSONString(user);
        template.opsForValue().set(key,value,RedisKeys.USER_LOGIN_TOKEN.getTime(),TimeUnit.SECONDS);
        return token;
    }



    @Override
    public UserInfo getUserByToken(String key) {
        AssertUtil.hasText(key,"token为空");
        String rulKey = RedisKeys.USER_LOGIN_TOKEN.join(key);
        String s = template.opsForValue().get(rulKey);
        UserInfo userInfo = JSON.parseObject(s, UserInfo.class);
        template.expire(rulKey,RedisKeys.USER_LOGIN_TOKEN.getTime(),TimeUnit.SECONDS);
        return userInfo;
    }
}



