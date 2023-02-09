package com.sj.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sj.domain.Strategy;
import com.sj.domain.StrategyCondition;
import com.sj.mapper.StrategyConditionMapper;
import com.sj.query.StrategyConditionQuery;
import com.sj.service.IStrategyConditionService;
import com.sj.service.IStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* 攻略条件服务接口实现
*/
@Service
@Transactional
public class StrategyConditionServiceImpl extends ServiceImpl<StrategyConditionMapper, StrategyCondition> implements IStrategyConditionService {

    @Autowired
    private IStrategyService strategyService;

    @Override
    public IPage<StrategyCondition> queryPage(StrategyConditionQuery qo) {
        IPage<StrategyCondition> page = new Page<>(qo.getCurrentPage(), qo.getPageSize());
        QueryWrapper<StrategyCondition> wrapper = Wrappers.<StrategyCondition>query();
        return super.page(page, wrapper);
    }

    @Override
    public List<StrategyCondition> queryByType(int type) {

        //select * from strategy_condition
        //where  type = 3 and statis_time in (select max(statis_time) from strategy_condition where type = 3)
        //order by count desc

        QueryWrapper<StrategyCondition> wrapper = new QueryWrapper<>();
        wrapper.eq("type", type);
        wrapper.inSql("statis_time","(select max(statis_time) from strategy_condition where type = "+type+")" );
        wrapper.orderByDesc("count");

        return super.list(wrapper);
    }

    @Override
    public void conditionDataHandle(int type) {
        //查询大表数据
        QueryWrapper<Strategy> wrapper = new QueryWrapper<>();
        if(type == StrategyCondition.TYPE_ABROAD){
            //国外
            wrapper.select("dest_id refid, dest_name name, count(id) count");
            wrapper.eq("isabroad", 1);
            wrapper.groupBy("dest_id, dest_name");
        }else if(type == StrategyCondition.TYPE_CHINA){
            //国内
            wrapper.select("dest_id refid, dest_name name, count(id) count");
            wrapper.eq("isabroad", 0);
            wrapper.groupBy("dest_id, dest_name");
        }else if(type == StrategyCondition.TYPE_THEME){
            //主题
            wrapper.select("theme_id refid, theme_name name, count(id) count");
            wrapper.groupBy("theme_id, theme_name");
        }
        wrapper.orderByDesc("count");
        List<Map<String, Object>> maps = strategyService.listMaps(wrapper);
        Date now = new Date();
        List<StrategyCondition> scs = new ArrayList<>();
        //将大表数据添加到小表中
        for (Map<String, Object> map : maps) {
            StrategyCondition sc = new StrategyCondition();
            sc.setRefid(Long.parseLong(map.get("refid").toString()));
            sc.setName(map.get("name").toString());
            sc.setCount(Integer.parseInt(map.get("count").toString()));
            sc.setStatisTime(now);
            sc.setType(type);
            scs.add(sc);
        }
        super.saveBatch(scs);


    }
}
