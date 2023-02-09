package com.sj.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sj.domain.StrategyCondition;
import com.sj.query.StrategyConditionQuery;

import java.util.List;


/**
 * 攻略条件服务接口
 */
public interface IStrategyConditionService extends IService<StrategyCondition>{
    /**
    * 分页
    * @param qo
    * @return
    */
    IPage<StrategyCondition> queryPage(StrategyConditionQuery qo);

    /**
     * 根据类型查询攻略条件类表
     * @param type
     * @return
     */
    List<StrategyCondition> queryByType(int type);

    /**
     * 攻略条件列表数据维护
     * @param type
     */
    void conditionDataHandle(int type);
}
