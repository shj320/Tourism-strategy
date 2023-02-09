package com.sj.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sj.domain.Strategy;
import com.sj.domain.StrategyContent;
import com.sj.query.StrategyQuery;
import com.sj.redis.vo.StrategyStatisVO;

import java.util.List;


/**
 * 攻略文章服务接口
 */
public interface IStrategyService extends IService<Strategy>{
    /**
    * 分页
    * @param qo
    * @return
    */
    IPage<Strategy> queryPage(StrategyQuery qo);

    /**
     * 查看内容对象
     * @param id
     * @return
     */
    StrategyContent getContent(Long id);

    /**
     * 查询指定攻略分类下的攻略文章集合
     * @param id
     * @return
     */
    List<Strategy> queryByCatatlogId(Long id);

    /**
     * 查询指定目的地下攻略点击量前3
     * @param destId
     * @return
     */
    List<Strategy> queryViewnumTop3(Long destId);

    /**
     * 更新统计数
     * @param vo
     */
    void updateStatis(StrategyStatisVO vo);

    /**
     * 通过目的地id查询该目的地下所有攻略
     * @param destId
     * @return
     */
    List<Strategy> queryByDestId(Long destId);
}
