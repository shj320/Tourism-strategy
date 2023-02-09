package com.sj.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sj.domain.Banner;
import com.sj.service.IBannerService;
import com.sj.util.JsonResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("banners")
public class BannerController {



    @Autowired
    private IBannerService bannerService;


    @GetMapping("/travel")
    public JsonResult travel(){
        List<Banner> list = bannerService.queryByType(Banner.TYPE_TRAVEL);
        return JsonResult.success(list);
    }
    @GetMapping("/strategy")
    public JsonResult strategy(){
        List<Banner> list = bannerService.queryByType(Banner.TYPE_STRATEGY);
        return JsonResult.success(list);
    }
}
