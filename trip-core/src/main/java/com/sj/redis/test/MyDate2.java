package com.sj.redis.test;

import lombok.Getter;
import lombok.Setter;

@Getter
public class MyDate2 {
    public static final MyDate2  DATE1 = new MyDate2("date1", 1L);
    public static final MyDate2  DATE2 = new MyDate2("date2", 2L);

    @Setter
    private String prefix;
    @Setter
    private Long time;

    private MyDate2(String prefix, Long time){
        this.prefix = prefix;
        this.time = time;
    }

    public String join(String value){
        return this.prefix + ":" + value;
    }
}
