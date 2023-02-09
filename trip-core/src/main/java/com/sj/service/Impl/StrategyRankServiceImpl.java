package com.sj.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sj.domain.Strategy;
import com.sj.domain.StrategyRank;
import com.sj.mapper.StrategyRankMapper;
import com.sj.query.StrategyRankQuery;
import com.sj.service.IStrategyRankService;
import com.sj.service.IStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* 攻略排行服务接口实现
*/
@Service
@Transactional
public class StrategyRankServiceImpl extends ServiceImpl<StrategyRankMapper, StrategyRank> implements IStrategyRankService {

    @Autowired
    private IStrategyService strategyService;

    @Override
    public IPage<StrategyRank> queryPage(StrategyRankQuery qo) {
        IPage<StrategyRank> page = new Page<>(qo.getCurrentPage(), qo.getPageSize());
        QueryWrapper<StrategyRank> wrapper = Wrappers.<StrategyRank>query();
        return super.page(page, wrapper);
    }

    @Override
    public List<StrategyRank> queryByType(int type) {

        //sql
//
//        select * from strategy_rank
//        where type = 3
//        and statis_time = (select max(statis_time) from strategy_rank where type = 3)
//        order by statisnum desc
//        limit 10

        QueryWrapper<StrategyRank> wrapper = new QueryWrapper<>();
        wrapper.eq("type", type);
        wrapper.inSql("statis_time", "(select max(statis_time) from strategy_rank where type = "+type+")");
        wrapper.orderByDesc("statisnum");
        wrapper.last("limit 10");

        return super.list(wrapper);
    }

    @Override
    public void rankDataHandle(int type) {
        QueryWrapper<Strategy> wrapper = new QueryWrapper<>();
        if(type == StrategyRank.TYPE_ABROAD){
            wrapper.eq("isabroad", 1);
            wrapper.orderByDesc("favornum + thumbsupnum");
        }else if(type == StrategyRank.TYPE_CHINA){
            wrapper.eq("isabroad", 0);
            wrapper.orderByDesc("favornum + thumbsupnum");
        }else if(type == StrategyRank.TYPE_HOT){
            wrapper.orderByDesc("viewnum + replynum");
        }
        wrapper.last("limit 10");
        List<Strategy> list = strategyService.list(wrapper);
        Date now = new Date();
        //添加到小表(strategy_rank)
        List<StrategyRank> ranks = new ArrayList<>();
        for (Strategy strategy : list) {
            StrategyRank rank = new StrategyRank();
            rank.setDestId(strategy.getDestId());
            rank.setDestName(strategy.getDestName());
            rank.setStrategyId(strategy.getId());
            rank.setStrategyTitle(strategy.getTitle());
            if(type == StrategyRank.TYPE_HOT){
                rank.setStatisnum(strategy.getViewnum() + strategy.getReplynum() + 0L);
            }else{
                rank.setStatisnum(strategy.getFavornum() + strategy.getThumbsupnum() + 0L);
            }
            rank.setType(type);
            rank.setStatisTime(now);
            ranks.add(rank);
        }
        super.saveBatch(ranks);
    }
}
