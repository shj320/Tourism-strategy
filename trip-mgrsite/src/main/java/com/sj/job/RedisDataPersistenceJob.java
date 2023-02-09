package com.sj.job;


import com.sj.redis.service.IStrategyStatisVORedisService;
import com.sj.redis.util.RedisKeys;
import com.sj.redis.vo.StrategyStatisVO;
import com.sj.service.IStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * redis数据持久化定时任务
 */
//@Component
public class RedisDataPersistenceJob {

    @Autowired
    private IStrategyStatisVORedisService strategyStatisVORedisService;

    @Autowired
    private IStrategyService strategyService;


    @Scheduled(cron = "0/5 * * * * ? ")
    public void doWork(){
        System.out.println("<---------------------攻略统计vo数据持久化--begin---------------------------->");
        //从redis 中查询所有的vo对象
        List<StrategyStatisVO> vos = strategyStatisVORedisService.queryByPattern(RedisKeys.STRATEGY_STATIS_VO.join("*"));
        //将vo对象中统计更新mysql 中strategy表
        for (StrategyStatisVO vo : vos) {
            strategyService.updateStatis(vo);
        }
        System.out.println("<---------------------攻略统计vo数据持久化--end---------------------------->");
    }
}
