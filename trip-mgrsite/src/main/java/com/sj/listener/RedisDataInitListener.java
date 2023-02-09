package com.sj.listener;

import com.sj.domain.Strategy;
import com.sj.redis.service.IStrategyStatisVORedisService;
import com.sj.redis.vo.StrategyStatisVO;
import com.sj.service.IStrategyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * redis数据初始化监听器
 */
@Component
public class RedisDataInitListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private IStrategyService strategyService;

    @Autowired
    private IStrategyStatisVORedisService strategyStatisVORedisService;

    //容器启动(初始化)之后执行
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("-----------------攻略的统计vo对象初始化--begin---------------------");

        //从mysql 查询满足条件攻略集合
        List<Strategy> list = strategyService.list();


        //遍历集合，将攻略对象封装成vo对象缓存到redis中
        for (Strategy strategy : list) {
            if(strategyStatisVORedisService.isVOExist(strategy.getId())){
                 continue;
            }
            StrategyStatisVO vo = new StrategyStatisVO();
            BeanUtils.copyProperties(strategy, vo);
            vo.setStrategyId(strategy.getId());

            strategyStatisVORedisService.setStatisVO(vo);
        }


        System.out.println("-----------------攻略的统计vo对象初始化--end---------------------");
    }
}
