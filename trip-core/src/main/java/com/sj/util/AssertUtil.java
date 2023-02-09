package com.sj.util;


import com.sj.exception.LogicException;

import org.springframework.util.StringUtils;

/**
 * 断言工具类
 */
public class AssertUtil {
    private AssertUtil(){ }
    /**
     * 判断text 是否为null 或者 ""
     * @param text
     * @param message
     */
    public static void hasText(String text, String message) {
        if (!StringUtils.hasText(text)) {
            throw new LogicException(message);
        }
    }

    /**
     * 判断v1 v2 是否相等
     * @param v1
     * @param v2
     * @param msg
     */
    public static void isEquals(String v1, String v2, String msg) {
        if(v1 == null || v2 == null){
            throw new RuntimeException("参数不能为null");
        }
        if(!v1.equals(v2)){
            throw new LogicException(msg);
        }
    }
}
