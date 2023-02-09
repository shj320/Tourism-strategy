package com.sj.controller;



import com.sj.domain.Destination;
import com.sj.domain.Strategy;
import com.sj.domain.Travel;
import com.sj.domain.UserInfo;
import com.sj.search.domain.DestinationEs;
import com.sj.search.domain.StrategyEs;
import com.sj.search.domain.TravelEs;
import com.sj.search.domain.UserInfoEs;
import com.sj.search.service.IDestinationEsService;
import com.sj.search.service.IStrategyEsService;
import com.sj.search.service.ITravelEsService;
import com.sj.search.service.IUserInfoEsService;
import com.sj.service.IDestinationService;
import com.sj.service.IStrategyService;
import com.sj.service.ITravelService;
import com.sj.service.IUserInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class DataController {
    //es服务
    @Autowired
    private IDestinationEsService destinationEsService;
    @Autowired
    private IStrategyEsService strategyEsService;
    @Autowired
    private ITravelEsService travelEsService;
    @Autowired
    private IUserInfoEsService userInfoEsService;

    //mysql服务
    @Autowired
    private IDestinationService destinationService;
    @Autowired
    private IStrategyService strategyService;
    @Autowired
    private ITravelService travelService;
    @Autowired
    private IUserInfoService userInfoService;

    @GetMapping("/dataInit")
    public Object dataInit(){
        //攻略
        List<Strategy> sts = strategyService.list();
        for (Strategy st : sts) {
            StrategyEs es = new StrategyEs();
            BeanUtils.copyProperties(st, es);
            strategyEsService.save(es);
        }
        //游记
        List<Travel> ts = travelService.list();
        for (Travel t : ts) {
            TravelEs es = new TravelEs();
            BeanUtils.copyProperties(t, es);
            travelEsService.save(es);
        }
        List<UserInfo> uf = userInfoService.list();
        for (UserInfo u : uf) {
            UserInfoEs es = new UserInfoEs();
            BeanUtils.copyProperties(u, es);
            userInfoEsService.save(es);
        }
        //目的地
        List<Destination> dests = destinationService.list();
        for (Destination d : dests) {
            DestinationEs es = new DestinationEs();
            BeanUtils.copyProperties(d, es);
            destinationEsService.save(es);
        }
        return "ok";
    }
}
