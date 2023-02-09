package com.sj.controller;





import cn.hutool.http.HttpRequest;
import com.sj.annotation.RequireLogin;
import com.sj.domain.UserInfo;
import com.sj.exception.LogicException;
import com.sj.redis.service.IUserInfoRedisService;
import com.sj.service.IUserInfoService;
import com.sj.util.JsonResult;
import javafx.fxml.LoadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.reactive.GenericReactiveTransaction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("users")
public class UserInfoController {

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IUserInfoRedisService userInfoRedisService;

    @GetMapping("/checkPhone")
    public Boolean checkPhone(String phone){
        boolean b = userInfoService.checkPhone(phone);
        return b;
    }

    @GetMapping("sendVerifyCode")
    public JsonResult sendVerifyCode(String phone){
        userInfoService.sendVerifyCode(phone);
        return JsonResult.success();
    }
    @PostMapping("/regist")
    public JsonResult regist(String phone, String nickname, String password, String rpassword, String verifyCode){
        userInfoService.regist(phone, nickname, password, rpassword, verifyCode);
        return JsonResult.success();
    }

    @PostMapping("/login")
    public JsonResult login(String username, String password){

        UserInfo user =  userInfoService.login(username, password);

        //创建token并缓存token
        String token = userInfoRedisService.createAndSaveToken(user);

        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("token", token);

        return JsonResult.success(map);
    }

    @RequireLogin
    @GetMapping("/currentUser")
    public JsonResult currentUser(HttpServletRequest request){
        String key = request.getHeader("token");
        UserInfo userByToken = userInfoRedisService.getUserByToken(key);
        return JsonResult.success(userByToken);
    }

}
