package com.sj.util;

import java.util.HashMap;

/**
 * 参数封装map类
 */
public class ParamMap extends HashMap<String, Object> {

    @Override
    public ParamMap put(String key, Object value) {
        super.put(key, value);
        return this;
    }
    public static ParamMap newInstance(){
        return new ParamMap();
    }
}
