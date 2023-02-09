package com.sj.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sj.domain.StrategyCatalog;
import com.sj.query.StrategyCatalogQuery;
import com.sj.vo.CatalogVO;

import java.util.List;


/**
 * 攻略分类服务接口
 */
public interface IStrategyCatalogService extends IService<StrategyCatalog>{
    /**
    * 分页
    * @param qo
    * @return
    */
    IPage<StrategyCatalog> queryPage(StrategyCatalogQuery qo);

    /**
     * 分组下拉框
     * @return
     */
    List<CatalogVO> queryGroup();

    /**
     * 查找指定目的地下攻略分类集合
     * @param destId
     * @return
     */
    List<StrategyCatalog> queryByDestId(Long destId);
}
