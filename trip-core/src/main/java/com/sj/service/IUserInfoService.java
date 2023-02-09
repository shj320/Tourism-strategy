package com.sj.service;


import com.baomidou.mybatisplus.extension.service.IService;

import com.sj.domain.UserInfo;


import java.util.List;

/**
 * 用户服务层接口
 */
public interface IUserInfoService extends IService<UserInfo> {


    /**
     * 检查手机号码是否存在
     * @param phone
     * @return  true:手机号码存在   false:表示号码不存在
     *
     */
    boolean checkPhone(String phone);

    /**
     * 发送短信验证码
     * @param phone
     */
    void sendVerifyCode(String phone);

    /**
     * 用户注册逻辑
     * @param phone
     * @param nickname
     * @param password
     * @param rpassword
     * @param verifyCode
     */
    void regist(String phone, String nickname, String password, String rpassword, String verifyCode);

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    UserInfo login(String username, String password);

    /**
     * 查询指定城市下的用户
     * @param name
     * @return
     */
    List<UserInfo> queryByCity(String name);
}
