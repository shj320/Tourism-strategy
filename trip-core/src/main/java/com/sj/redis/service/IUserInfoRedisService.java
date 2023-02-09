package com.sj.redis.service;


import com.sj.domain.UserInfo;

/**
 * 用户缓存服务层接口
 */
public interface IUserInfoRedisService {
    /**
     * 缓存短信验证码
     * @param phone
     * @param code
     */
    void setVerifyCode(String phone, String code);

    /**
     * 获取验证
     * @param phone
     * @return
     */
    String getVerifyCode(String phone);

    /**
     * 创建token并缓存token
     * @param user
     * @return
     */
    String createAndSaveToken(UserInfo user);

    /**
     * 使用token换取用户对象
     * @param token
     * @return
     */

    UserInfo getUserByToken(String key);
}
