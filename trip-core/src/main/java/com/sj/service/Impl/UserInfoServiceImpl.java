package com.sj.service.Impl;



import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sj.domain.UserInfo;

import com.sj.exception.LogicException;
import com.sj.mapper.UserInfoMapper;
import com.sj.redis.service.IUserInfoRedisService;
import com.sj.service.IUserInfoService;

import com.sj.util.AssertUtil;
import com.sj.util.Consts;
import com.sj.util.JsonResult;
import com.sj.util.PhoneUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import sun.security.provider.MD5;


import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class UserInfoServiceImpl  extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

    @Autowired
    private IUserInfoRedisService userInfoRedisService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public boolean checkPhone(String phone) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone);
        UserInfo one = super.getOne(wrapper);
        return one != null;
    }

    @Override
    public void sendVerifyCode(String phone) {
        //创建验证码
        String code = UUID.randomUUID().toString()
                .replaceAll("-", "")
                .substring(0, 4);
        //拼接短信
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("验证码：").
                append(code).
                append("请在").
                append(Consts.VERIFY_CODE_VAI_TIME).
                append("分钟内完成");
        //发送验证码
        String url="https://api.smsbao.com/sms?u=shj320&p=6c3607e678cb413b87a4f164dc9c32ea&m={0}&c={1}";
        String forObject = restTemplate.getForObject(url, String.class, phone, stringBuilder);
        if(forObject.equals("0")){
            System.out.println(forObject.equals("0"));
            System.out.println("成功");
        }else {
            throw new LogicException("短信发送失败");
        }
        System.out.println(stringBuilder.toString());
        //缓存验证码
        userInfoRedisService.setVerifyCode(phone,code);

    }

    @Override
    public void regist(String phone, String nickname, String password, String rpassword, String verifyCode) {
        //判断参数是否为空
        AssertUtil.hasText(phone,"手机号不能为空");
        AssertUtil.hasText(password,"密码不能为空");
        AssertUtil.hasText(rpassword,"确认密码不能为空");
        AssertUtil.hasText(verifyCode,"验证码不能为空");
        //判断两次密码是否正确
        AssertUtil.isEquals(password,rpassword,"两次密码不一致");
        //判断手机号码是否正确
        PhoneUtil.validatePhoneNumber(phone);
        //判断手机号码是否存在
        boolean b = userInfoService.checkPhone(phone);
        if(b){
            throw new LogicException("手机号码存在");
        }
        //判断验证码是否一致
        String rulcode = userInfoRedisService.getVerifyCode(phone);
        if (!verifyCode.equals(rulcode)){
            throw new LogicException("验证码不一致");
        }
        //注册进去‘
        UserInfo userInfo = new UserInfo();
        userInfo.setNickname(nickname);
        userInfo.setPhone(phone);
        //加密
        String s = SecureUtil.md5(password);
        userInfo.setPassword(s);
        System.out.println("******************"+SecureUtil.hmacMd5(s)+"*******************");
        userInfo.setHeadImgUrl("https://picsum.photos/200");
        userInfo.setState(UserInfo.STATE_NORMAL);
        super.save(userInfo);


    }

    @Override
    public UserInfo login(String username, String password) {
        AssertUtil.hasText(username,"用户名不能为空");
        AssertUtil.hasText(password,"密码不能为空");
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        //加密
        String s = SecureUtil.md5(password);
        userInfoQueryWrapper.eq("phone",username);
        userInfoQueryWrapper.eq("password",s);
        UserInfo one = super.getOne(userInfoQueryWrapper);
        if(one==null){
            throw new LogicException("账号或密码错误");
        }else if (one.getState()==UserInfo.STATE_DISABLE){
            throw new LogicException("账号被冻结");
        }

        return one;
    }

    @Override
    public List<UserInfo> queryByCity(String name) {
        return super.list(new QueryWrapper<UserInfo>().eq("city", name));
    }
}
