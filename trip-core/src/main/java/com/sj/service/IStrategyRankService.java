package com.sj.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sj.domain.StrategyRank;
import com.sj.query.StrategyRankQuery;

import java.util.List;


/**
 * 攻略排行服务接口
 */
public interface IStrategyRankService extends IService<StrategyRank>{
    /**
    * 分页
    * @param qo
    * @return
    */
    IPage<StrategyRank> queryPage(StrategyRankQuery qo);

    /**
     * 根据类型查询攻略推荐排行列表
     * @param type
     * @return
     */
    List<StrategyRank> queryByType(int type);

    /**
     * 攻略推荐排行数据维护
     * @param type
     */
    void rankDataHandle(int type);
}
