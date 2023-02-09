package com.sj.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.Update;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sj.domain.Destination;
import com.sj.domain.Region;
import com.sj.mapper.RegionMapper;
import com.sj.query.RegionQuery;
import com.sj.service.IRegionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements IRegionService {


    @Override
    public void changeHotValue(Long id, int hot) {
        UpdateWrapper<Region> regionUpdateWrapper = new UpdateWrapper<>();
        regionUpdateWrapper.eq("id",id)
                .set("ishot",hot);

        super.update(regionUpdateWrapper);
    }

    @Override
    public List<Region> queryHot(int ishot) {
        QueryWrapper<Region> destinationQueryWrapper = new QueryWrapper<>();
        destinationQueryWrapper.eq("ishot",ishot)
                .orderByAsc("seq");
        return super.list(destinationQueryWrapper);
    }

    @Override
    public Page<Region> queryPage(RegionQuery qo) {
        QueryWrapper<Region> regionQueryWrapper = new QueryWrapper<>();

        Page<Region> regionPage = new Page<>(qo.getCurrentPage(),qo.getPageSize());

        return super.page(regionPage,regionQueryWrapper);
    }
}
