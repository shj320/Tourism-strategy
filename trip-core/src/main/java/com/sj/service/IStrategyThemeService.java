package com.sj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sj.domain.StrategyTheme;
import com.sj.query.StrategyThemeQuery;


/**
 * 攻略主题服务接口
 */
public interface IStrategyThemeService extends IService<StrategyTheme>{
    /**
    * 分页
    * @param qo
    * @return
    */
    IPage<StrategyTheme> queryPage(StrategyThemeQuery qo);
}
