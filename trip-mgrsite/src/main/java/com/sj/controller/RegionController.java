package com.sj.controller;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sj.domain.Destination;
import com.sj.domain.Region;
import com.sj.query.RegionQuery;
import com.sj.service.IDestinationService;
import com.sj.service.IRegionService;
import com.sj.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/region")
public class RegionController {

    @Autowired
    private IDestinationService destinationService;

    @Autowired
    private IRegionService regionService;

    @RequestMapping("/list")
    public String list(Model model, @ModelAttribute("qo") RegionQuery qo){

        //page
        Page<Region> page = regionService.queryPage(qo);


        model.addAttribute("page", page);
        //dests
        model.addAttribute("dests", destinationService.list());
        return  "/region/list";
    }


    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    public JsonResult saveOrUpdate(Region region){
        regionService.saveOrUpdate(region);
        return  JsonResult.success();
    }

    @RequestMapping("/changeHotValue")
    @ResponseBody
    public JsonResult changeHotValue(Long id, int hot){
        regionService.changeHotValue(id, hot);
        return  JsonResult.success();
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JsonResult delete(Long id){
        regionService.removeById(id);
        return  JsonResult.success();
    }

    @RequestMapping("/getDestByRegionId")
    @ResponseBody
    public List<Destination> getDestByRegionId(Long rid){
        List<Destination> list = destinationService.queryByRegionId(rid);
        return  list;
    }
}
