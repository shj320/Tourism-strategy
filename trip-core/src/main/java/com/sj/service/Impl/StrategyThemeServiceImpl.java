package com.sj.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sj.domain.StrategyTheme;
import com.sj.mapper.StrategyThemeMapper;
import com.sj.query.StrategyThemeQuery;
import com.sj.service.IStrategyThemeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* 攻略主题服务接口实现
*/
@Service
@Transactional
public class StrategyThemeServiceImpl extends ServiceImpl<StrategyThemeMapper, StrategyTheme> implements IStrategyThemeService {

    @Override
    public IPage<StrategyTheme> queryPage(StrategyThemeQuery qo) {
        IPage<StrategyTheme> page = new Page<>(qo.getCurrentPage(), qo.getPageSize());
        QueryWrapper<StrategyTheme> wrapper = Wrappers.<StrategyTheme>query();
        return super.page(page, wrapper);
    }
}
