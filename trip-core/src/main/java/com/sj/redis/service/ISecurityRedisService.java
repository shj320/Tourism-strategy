package com.sj.redis.service;

/**
 * 安全访问缓存服务接口
 */
public interface ISecurityRedisService {
    /**
     * 是否允许执行
     * @param key
     * @return
     */
    boolean isAllowBrush(String key);
}
