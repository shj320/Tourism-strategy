package com.sj.redis.service.impl;


import com.sj.redis.service.ISecurityRedisService;
import com.sj.redis.util.RedisKeys;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SecurityRedisServiceImpl implements ISecurityRedisService {
    @Autowired
    private StringRedisTemplate template;

    @Override
    public boolean isAllowBrush(String key) {
        return false;
    }
}
