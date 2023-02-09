package com.sj.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sj.domain.Destination;
import com.sj.query.DestinationQuery;

import java.util.List;

/**
 * 目的地服务层接口
 */
public interface IDestinationService extends IService<Destination> {


    /**
     * 查询指定区域下的目的地集合
     * @param rid
     * @return
     */
    List<Destination> queryByRegionId(Long rid);
    /**
     * 查询指定区域下的目的地集合
     * @param rid
     * @return
     */
    List<Destination> queryByRegionIdForApi(Long rid);



    /**
     * 查询指定目的地所有老爸(吐司集合)
     * @param destId
     * @return
     */
    List<Destination> queryToasts(Long destId);


    /**
     * 通过name查询对象
     * @param name
     * @return
     */
    Destination queryByName(String name);

    /**
     * 目的地分页
     * @param qo
     * @return
     */
    Page<Destination> queryPage(DestinationQuery qo);
}
