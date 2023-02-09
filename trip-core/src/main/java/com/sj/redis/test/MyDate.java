package com.sj.redis.test;

import lombok.Getter;
import lombok.Setter;

/**
 * 枚举类与普通类核心区别
 * 1>枚举类构造器是私有
 * 2>当枚举类定义完成后，实例个数就固定
 * 3>剩下操作跟普通一样
 */
@Getter
public enum MyDate {
    DATE1("date1", 1L),
    DATE2("date2", 2L);

    @Setter
    private String prefix;
    @Setter
    private Long time;

    private MyDate(String prefix, Long time){
        this.prefix = prefix;
        this.time = time;
    }

    public String join(String value){
        return this.prefix + ":" + value;
    }
}


















