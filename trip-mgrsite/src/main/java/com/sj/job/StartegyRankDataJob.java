package com.sj.job;


import com.sj.domain.StrategyRank;
import com.sj.service.IStrategyRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 攻略排行数据维护定时任务作业
 */
//@Component
public class StartegyRankDataJob {

    @Autowired
    private IStrategyRankService strategyRankService;
    /**
     * cron表达式详解
     * 　　Cron表达式是一个字符串，字符串以5或6个空格隔开，分为6或7个域，每一个域代表一个含义，Cron有如下两种语法格式：
     *
     * 　　（1） Seconds Minutes Hours DayofMonth Month DayofWeek Year
     *            秒      分     时      几号     月       周几     年
     * 　　（2）Seconds Minutes Hours DayofMonth Month DayofWeek       - ---- spring支持
     *            秒      分     时      几号     月       周几
     *
     *            0       0     2         1      *        ?       *   表示在每月的1日的凌晨2点调整任务
     *            0       15    10        ?      *      MON-FRI       表示周一到周五每天上午10:15执行作业
     *
     */
    //@Scheduled 定时任务注解
    //cron 表达式：任务计划表达式， 指定执行定时任务周期
    @Scheduled(cron = "0/5 * * * * ? ")
    public void doWork(){
        System.out.println("<---------------------攻略推荐排行数据维护--begin---------------------------->");
        strategyRankService.rankDataHandle(StrategyRank.TYPE_ABROAD);  //国外
        strategyRankService.rankDataHandle(StrategyRank.TYPE_CHINA);  //国内
        strategyRankService.rankDataHandle(StrategyRank.TYPE_HOT);  //热门
        System.out.println("<---------------------攻略推荐排行数据维护--end---------------------------->");
    }
}
