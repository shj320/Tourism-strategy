package com.sj.controller;


import com.sj.domain.*;
import com.sj.service.*;
import com.sj.service.Impl.StrategyCatalogServiceImpl;
import com.sj.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("destinations")
public class DestinationController {

    @Autowired
    private IRegionService regionService;

    @Autowired
    private IDestinationService destinationService;

    @Autowired
    private IStrategyCatalogService strategyCatalogService;

    @Autowired
    private IStrategyService strategyService;

    @Autowired
    private ITravelService travelService;


    @GetMapping("/hotRegion")
    public JsonResult hotRegion(int ishot){
        List<Region> list = regionService.queryHot(ishot);
        return JsonResult.success(list);
    }

    @GetMapping("/search")
    public JsonResult search(Long regionId){
        List<Destination> list = destinationService.queryByRegionIdForApi(regionId);
        return JsonResult.success(list);
    }

    @GetMapping("/toasts")
    public JsonResult toasts(Long destId){
        List<Destination> list = destinationService.queryToasts(destId);
        return JsonResult.success(list);
    }

    @GetMapping("/catalogs")
    public JsonResult catalogs(Long destId){
        List<StrategyCatalog> list = strategyCatalogService.queryByDestId(destId);
        return JsonResult.success(list);
    }

    @GetMapping("/strategies/viewnumTop3")
    public JsonResult stsViewnumTop3(Long destId){
        List<Strategy> list = strategyService.queryViewnumTop3(destId);
        return JsonResult.success(list);
    }

    @GetMapping("/travels/viewnumTop3")
    public JsonResult trViewnumTop3(Long destId){
        List<Travel> list = travelService.queryViewnumTop3(destId);
        return JsonResult.success(list);
    }
    @GetMapping("/list")
    public JsonResult list(){
        return JsonResult.success(travelService.list());
    }

}
