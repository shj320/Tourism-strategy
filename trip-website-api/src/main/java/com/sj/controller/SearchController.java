package com.sj.controller;


import com.sj.domain.Destination;
import com.sj.domain.Strategy;
import com.sj.domain.Travel;
import com.sj.domain.UserInfo;
import com.sj.search.domain.DestinationEs;
import com.sj.search.domain.StrategyEs;
import com.sj.search.domain.TravelEs;
import com.sj.search.domain.UserInfoEs;
import com.sj.search.query.SearchQueryObject;
import com.sj.search.service.ISearchService;
import com.sj.search.vo.SearchResultVO;
import com.sj.service.IDestinationService;
import com.sj.service.IStrategyService;
import com.sj.service.ITravelService;
import com.sj.service.IUserInfoService;
import com.sj.util.JsonResult;
import com.sj.util.ParamMap;
import org.apache.catalina.User;
import org.apache.ibatis.binding.MapperMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SearchController {
    @Autowired
    private IDestinationService destinationService;

    @Autowired
    private IStrategyService strategyService;

    @Autowired
    private ITravelService travelService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private ISearchService searchService;


    @GetMapping("/q")
    public JsonResult search(SearchQueryObject qo) throws UnsupportedEncodingException {

        //http://localhost/views/search/searchDest.html?type=0&keyword=%E5%B9%BF%E5%B7%9E
        qo.setKeyword( URLDecoder.decode(qo.getKeyword(), "UTF-8"));

        switch (qo.getType()){
            case SearchQueryObject.TYPE_DEST: return searchDest(qo);
            case SearchQueryObject.TYPE_STRATEGY: return searchStrategy(qo);
            case SearchQueryObject.TYPE_TRAVEL: return searchTravel(qo);
            case SearchQueryObject.TYPE_USER: return searchUser(qo);
            default: return searchAll(qo);
        }
    }
    //目的地查询
    private JsonResult searchDest(SearchQueryObject qo) {

        //根据传入keyword 精确查询目的地
        Destination dest = destinationService.queryByName(qo.getKeyword());
        SearchResultVO vo = new SearchResultVO();

        //如果目的地存在， 查询该目的地下所有攻略， 游记， 用户
        if(dest != null){
            List<Strategy> sts = strategyService.queryByDestId(dest.getId());
            List<Travel> ts = travelService.queryByDestId(dest.getId());
            List<UserInfo> us = userInfoService.queryByCity(dest.getName());

            vo.setStrategies(sts);
            vo.setTravels(ts);
            vo.setUsers(us);

            vo.setTotal(sts.size() + ts.size() + us.size() + 0L);
        }

        //如果存在，提示
        return JsonResult.success(ParamMap.newInstance().put("result", vo).put("dest", dest).put("qo", qo));
    }
    //用户搜索
    private JsonResult searchUser(SearchQueryObject qo) {
        //全文搜索 + 高亮显示 + 分页
        return JsonResult.success(ParamMap.newInstance().put("page", this.createUserPage(qo)).put("qo", qo));
    }
    //攻略搜索
    private JsonResult searchStrategy(SearchQueryObject qo) {
        //全文搜索 + 高亮显示 + 分页
        return JsonResult.success(ParamMap.newInstance().put("page", this.createStrategyPage(qo)).put("qo", qo));
    }
    //游记搜索
    private JsonResult searchTravel(SearchQueryObject qo) {
        //全文搜索 + 高亮显示 + 分页
        return JsonResult.success(ParamMap.newInstance().put("page", this.createTravelPage(qo)).put("qo", qo));
    }
    //全部搜索
    private JsonResult searchAll(SearchQueryObject qo) {

        SearchResultVO vo = new SearchResultVO();
        Page<Destination> destPage = this.createDestPage(qo);
        vo.setDests(destPage.getContent());
        Page<UserInfo> usPage =this.createUserPage(qo);
        vo.setUsers(usPage.getContent());
        Page<Strategy> stPage = this.createStrategyPage(qo);
        vo.setStrategies(stPage.getContent());
        Page<Travel> tsPage = this.createTravelPage(qo);
        vo.setTravels(tsPage.getContent());
        vo.setTotal(destPage.getTotalElements() + usPage.getTotalElements()
                + stPage.getTotalElements() + usPage.getTotalElements());
        return JsonResult.success(ParamMap.newInstance().put("result", vo).put("qo", qo));
    }

    private Page<UserInfo> createUserPage(SearchQueryObject qo){

        Class<UserInfo> clz = UserInfo.class;

        return  searchService.searchWithHighlight(UserInfoEs.INDEX_NAME,
                        UserInfo.class, qo, "info",  "city");
    }
    private Page<Destination> createDestPage(SearchQueryObject qo){
        return  searchService.searchWithHighlight(DestinationEs.INDEX_NAME,
                Destination.class, qo, "name",  "info");
    }
    private Page<Strategy> createStrategyPage(SearchQueryObject qo){
        return  searchService.searchWithHighlight(StrategyEs.INDEX_NAME,Strategy.class, qo, "title", "subTitle", "summary");
    }
    private Page<Travel> createTravelPage(SearchQueryObject qo){
        Page<Travel> tsPage = searchService.searchWithHighlight(TravelEs.INDEX_NAME,
                Travel.class, qo, "title",  "summary");
        for (Travel travel : tsPage) {
            travel.setAuthor(userInfoService.getById(travel.getAuthorId()));
        }
        return tsPage;
    }


}
