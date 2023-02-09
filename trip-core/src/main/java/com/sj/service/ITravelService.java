package com.sj.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sj.domain.Travel;
import com.sj.domain.TravelContent;
import com.sj.query.TravelQuery;

import java.util.List;


/**
 * 游记服务接口
 */
public interface ITravelService extends IService<Travel>{
    /**
    * 分页
    * @param qo
    * @return
    */
    IPage<Travel> queryPage(TravelQuery qo);

    /**
     * 查询游记内容
     * @param id
     * @return
     */
    TravelContent getContent(Long id);

    /**
     * 审核
     * @param id
     * @param state
     */
    void audit(Long id, int state);


    /**
     * 查询指定目的地下游记点击量前3
     * @param destId
     * @return
     */
    List<Travel> queryViewnumTop3(Long destId);

    /**
     * 通过目的地id查询该目的地下所有游记
     * @param destId
     * @return
     */
    List<Travel> queryByDestId(Long destId);

    IPage<Travel> queryPages();

    IPage<Travel> queryPage1(int orderType);
}
