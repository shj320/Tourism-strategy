package com.sj.redis.util;


import com.sj.util.Consts;
import lombok.Getter;
import lombok.Setter;

/**
 * redis key 管理类
 * 约定：该枚举类一个实例，关联一个redis key
 */
@Getter
public enum RedisKeys {
    //接口防刷计数器标记key 实例
    BRUSH_PROOF("brush_proof", 60L),  //约定时间暂时不设置/永久key

    //用户攻略点赞标记key 实例
    USER_STRATEGY_THUMB("user_strategy_thumb", -1L),  //约定时间暂时不设置/永久key
    //用户攻略收藏统计key 实例
    USER_STRATEGY_FAVOR("user_strategy_favor", -1L),  //约定时间暂时不设置/永久key
    //攻略vo统计key 实例
    STRATEGY_STATIS_VO("strategy_statis_vo", -1L),  //约定时间暂时不设置/永久key
    //用户登录token的key 实例
    USER_LOGIN_TOKEN("user_login_token", Consts.USER_INFO_TOKEN_VAI_TIME * 60L),

    //验证码的key 实例
    VERIFY_CODE("verify_code", Consts.VERIFY_CODE_VAI_TIME * 60L);
    private String prefix;  //key前缀
    private Long time;      //有效时间，单位s
    private RedisKeys(String prefix, Long time){
        this.prefix = prefix;
        this.time = time;
    }
    //拼接出最终key
    public String join(String... values){
        StringBuilder sb = new StringBuilder(80);
        sb.append(this.prefix);
        for (String value : values) {
            sb.append(":").append(value);
        }
        return sb.toString();
    }
    public static void main(String[] args) {
        //verify_code:137000000000:xxx:yyy:zzz
        String key = RedisKeys.VERIFY_CODE.join("13700000000", "xxx", "yyy", "zzz");
        System.out.println(key);
    }
}


















