package com.sj.controller;


import com.sj.domain.Strategy;
import com.sj.domain.StrategyContent;
import com.sj.query.StrategyQuery;
import com.sj.service.IStrategyCatalogService;
import com.sj.service.IStrategyService;
import com.sj.service.IStrategyThemeService;
import com.sj.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
* 攻略文章控制层
*/
@Controller
@RequestMapping("strategy")
public class StrategyController {

    @Autowired
    private IStrategyService strategyService;

    @Autowired
    private IStrategyCatalogService strategyCatalogService;

    @Autowired
    private IStrategyThemeService strategyThemeService;

    @RequestMapping("/list")
    public String list(Model model, @ModelAttribute("qo") StrategyQuery qo){
        IPage<Strategy> page = strategyService.queryPage(qo);
        model.addAttribute("page", page);
        return "strategy/list";
    }

    @RequestMapping("/input")
    public String input(Model model, Long id){
        //strategy攻略详情
        if(id  != null){
            Strategy strategy = strategyService.getById(id);
            StrategyContent content =  strategyService.getContent(id);

            strategy.setContent(content);
            model.addAttribute("strategy", strategy);
        }

        //catalogs分类
        model.addAttribute("catalogs", strategyCatalogService.queryGroup());
        //themes主题
        model.addAttribute("themes", strategyThemeService.list());

        return "strategy/input";
    }


    @RequestMapping("/get")
    @ResponseBody
    public Object get(Long id){
        return JsonResult.success(strategyService.getById(id));
    }

    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    public Object saveOrUpdate(Strategy strategy){
        strategyService.saveOrUpdate(strategy);
        return JsonResult.success();
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(Long id){
        strategyService.removeById(id);
        return JsonResult.success();
    }
}
