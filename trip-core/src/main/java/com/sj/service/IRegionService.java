package com.sj.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sj.domain.Region;
import com.sj.query.RegionQuery;

import java.util.List;

/**
 * 区域服务层接口
 */
public interface IRegionService extends IService<Region> {



    /**
     * 修改热门状态
     * @param id
     * @param hot
     */
    void changeHotValue(Long id, int hot);

    /**
     * 查询热门区域集合
     * @return
     */
    List<Region> queryHot(int ishot);

    /**
     * 分页luoji
     * @param qo
     * @return
     */
    Page<Region> queryPage(RegionQuery qo);
}
