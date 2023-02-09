package com.sj.query;


import lombok.Getter;
import lombok.Setter;

/**
* 攻略文章查询参数封装对象
*/
@Setter
@Getter
public class StrategyQuery extends  QueryObject{

    private Long destId;
    private Long themeId;

    private Long refid;
    private Integer type;

    private String orderBy;
}
