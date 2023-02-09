package com.sj.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sj.domain.Banner;
import com.sj.query.BannerQuery;

import java.util.List;


/**
 * Banner服务接口
 */
public interface IBannerService extends IService<Banner>{
    /**
    * 分页
    * @param qo
    * @return
    */
    IPage<Banner> queryPage(BannerQuery qo);

    /**
     * 根据类型查询游记banner
     * @param type
     * @return
     */
    List<Banner> queryByType(int type);
}
