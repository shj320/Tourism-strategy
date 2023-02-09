/**
 * author:sj
 */

package com.sj.util;

import com.sj.exception.LogicException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneUtil {
    /**
     * 正则表达式校验手机号码
     * @return false 则手机号码不合法，true 则手机号码校验通过
     */
    public static void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.length() != 11) {
            throw new LogicException("手机号码长度不对");

        }else{
            String regPattern =  "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
            Pattern pattern = Pattern.compile(regPattern);
            Matcher matcher = pattern.matcher(phoneNumber);
            boolean isMatch = matcher.matches();
            if (!isMatch) {
                throw new LogicException("输入正确手机号码");
            }

        }
}}
