package com.sj.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sj.domain.Banner;
import com.sj.mapper.BannerMapper;
import com.sj.query.BannerQuery;
import com.sj.service.IBannerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* Banner服务接口实现
*/
@Service
@Transactional
public class BannerServiceImpl extends ServiceImpl<BannerMapper,Banner> implements IBannerService {

    @Override
    public IPage<Banner> queryPage(BannerQuery qo) {
        IPage<Banner> page = new Page<>(qo.getCurrentPage(), qo.getPageSize());
        QueryWrapper<Banner> wrapper = Wrappers.<Banner>query();
        return super.page(page, wrapper);
    }

    @Override
    public List<Banner> queryByType(int type) {
        return super.list(new QueryWrapper<Banner>()
                .eq("type", type)
                .eq("state", Banner.STATE_NORMAL)
                .orderByAsc("seq")
                .last("limit 5")
        );
    }
}
