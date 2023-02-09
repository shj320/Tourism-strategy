package com.sj.query;


import lombok.Getter;
import lombok.Setter;

/**
* 游记查询参数封装对象
*/
@Setter
@Getter
public class TravelQuery extends  QueryObject{
    private Long destId;

    private Integer dayType;
    private Integer consumeType;
    private Integer travelTimeType;
}
