package com.sj.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sj.domain.Strategy;
import com.sj.domain.StrategyCatalog;
import com.sj.mapper.StrategyCatalogMapper;
import com.sj.query.StrategyCatalogQuery;
import com.sj.service.IStrategyCatalogService;
import com.sj.service.IStrategyService;
import com.sj.vo.CatalogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
* 攻略分类服务接口实现
*/
@Service
@Transactional
public class StrategyCatalogServiceImpl extends ServiceImpl<StrategyCatalogMapper, StrategyCatalog> implements IStrategyCatalogService {

    @Autowired
    private IStrategyService strategyService;
    @Override
    public IPage<StrategyCatalog> queryPage(StrategyCatalogQuery qo) {
        IPage<StrategyCatalog> page = new Page<>(qo.getCurrentPage(), qo.getPageSize());
        QueryWrapper<StrategyCatalog> wrapper = Wrappers.<StrategyCatalog>query();
        return super.page(page, wrapper);
    }

    @Override
    public List<CatalogVO> queryGroup() {
        List<CatalogVO> vos = new ArrayList<>();
        QueryWrapper<StrategyCatalog> wrapper = new QueryWrapper<>();
        wrapper.select("dest_name, GROUP_CONCAT(id) ids, GROUP_CONCAT(name) names");
        wrapper.groupBy("dest_name");
        List<Map<String, Object>> maps = super.listMaps(wrapper);
        for (Map<String, Object> map : maps) {
            String destName = map.get("dest_name").toString();

            String ids = map.get("ids").toString();
            String names = map.get("names").toString();
            List<StrategyCatalog> catalogList = this.parseCatalogList(ids, names);
            CatalogVO vo = new CatalogVO();
            vo.setDestName(destName);
            vo.setCatalogList(catalogList);
            vos.add(vo);
        }
        return vos;
    }

    private List<StrategyCatalog> parseCatalogList(String ids, String names) {
        List<StrategyCatalog> catalogList = new ArrayList<>();
        String[] idsA = ids.split(",");
        String[] namesA = names.split(",");
        for (int i = 0; i < idsA.length ; i++) {
            StrategyCatalog  sc = new StrategyCatalog();
            sc.setId( Long.parseLong(idsA[i]));
            sc.setName(namesA[i]);
            catalogList.add(sc);
        }
        return catalogList;

    }

    @Override
    public List<StrategyCatalog> queryByDestId(Long destId) {
        QueryWrapper<StrategyCatalog> wrapper = new QueryWrapper<>();
        wrapper.eq("dest_id", destId);
        List<StrategyCatalog> list = super.list(wrapper);

        for (StrategyCatalog sc : list) {
            List<Strategy> sts = strategyService.queryByCatatlogId(sc.getId());
            sc.setStrategies(sts);
        }
        return list;
    }
}
