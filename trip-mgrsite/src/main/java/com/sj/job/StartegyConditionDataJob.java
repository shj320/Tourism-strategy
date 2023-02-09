package com.sj.job;


import com.sj.domain.StrategyCondition;
import com.sj.service.IStrategyConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 攻略条件数据维护定时任务作业
 */
//@Component
public class StartegyConditionDataJob {

    @Autowired
    private IStrategyConditionService strategyConditionService;
   
  
    @Scheduled(cron = "0/5 * * * * ? ")
    public void doWork(){
        System.out.println("<---------------------攻略条件数据维护--begin---------------------------->");
        strategyConditionService.conditionDataHandle(StrategyCondition.TYPE_ABROAD);  //国外
        strategyConditionService.conditionDataHandle(StrategyCondition.TYPE_CHINA);  //国内
        strategyConditionService.conditionDataHandle(StrategyCondition.TYPE_THEME);  //主题
        System.out.println("<---------------------攻略条件数据维护--end---------------------------->");
    }
}
