package com.sj.controller;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sj.domain.Destination;
import com.sj.query.DestinationQuery;
import com.sj.service.IDestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/destination")
public class DestinationController {

    @Autowired
    private IDestinationService destinationService;



    @RequestMapping("/list")
    public String list(Model model, @ModelAttribute("qo") DestinationQuery qo){

        //page
        Page<Destination> page = destinationService.queryPage(qo);
        model.addAttribute("page", page);

        //toasts
        model.addAttribute("toasts", destinationService.queryToasts(qo.getParentId()));


        return  "/destination/list";
    }








}
