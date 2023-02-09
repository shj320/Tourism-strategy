package com.sj.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sj.domain.*;
import com.sj.mapper.StrategyContentMapper;
import com.sj.mapper.StrategyMapper;
import com.sj.query.StrategyQuery;
import com.sj.redis.vo.StrategyStatisVO;
import com.sj.service.IDestinationService;
import com.sj.service.IStrategyCatalogService;
import com.sj.service.IStrategyService;
import com.sj.service.IStrategyThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
* 攻略文章服务接口实现
*/
@Service
@Transactional
public class StrategyServiceImpl extends ServiceImpl<StrategyMapper,Strategy> implements IStrategyService {

    @Autowired
    private IStrategyCatalogService strategyCatalogService;

    @Autowired
    private IStrategyThemeService strategyThemeService;

    @Autowired
    private IDestinationService destinationService;

    @Autowired
    private StrategyContentMapper strategyContentMapper;


    @Override
    public IPage<Strategy> queryPage(StrategyQuery qo) {
        IPage<Strategy> page = new Page<>(qo.getCurrentPage(), qo.getPageSize());
        QueryWrapper<Strategy> wrapper = Wrappers.<Strategy>query();


        wrapper.eq(qo.getDestId() != null, "dest_id", qo.getDestId());
        wrapper.eq(qo.getThemeId() != null, "theme_id", qo.getThemeId());


        Integer type = qo.getType();
        Long refid = qo.getRefid();
        if(type != null && refid != null){
            if(StrategyCondition.TYPE_THEME.equals(type)){
                wrapper.eq("theme_id", refid);
            }else if(StrategyCondition.TYPE_CHINA.equals(type) || StrategyCondition.TYPE_ABROAD.equals(type)){
                wrapper.eq("dest_id", refid);
            }
        }

        wrapper.orderByDesc(StringUtils.hasText(qo.getOrderBy()), qo.getOrderBy());

        return super.page(page, wrapper);
    }

    @Autowired
    private ApplicationContext ctx;

    @Override
    public boolean saveOrUpdate(Strategy entity) {
        //目的地id/名称
        StrategyCatalog catalog = strategyCatalogService.getById(entity.getCatalogId());
        entity.setDestId(catalog.getDestId());
        entity.setDestName(catalog.getDestName());
        //攻略分类名称
        entity.setCatalogName(catalog.getName());
        //攻略主题名称
        StrategyTheme theme = strategyThemeService.getById(entity.getThemeId());
        entity.setThemeName(theme.getName());
        //是否国外
        List<Destination> toasts = destinationService.queryToasts(catalog.getDestId());
        if(toasts != null && toasts.size() > 0){
            Destination dest = toasts.get(0);
            if("中国".equals(dest.getName())){
                entity.setIsabroad(Strategy.ABROAD_NO);
            }else{
                entity.setIsabroad(Strategy.ABROAD_YES);
            }
        }
        boolean flag = false;
        if(entity.getId() == null){
            //创建时间
            entity.setCreateTime(new Date());
            //各种统计数
            entity.setViewnum(0);
            entity.setReplynum(0);
            entity.setSharenum(0);
            entity.setThumbsupnum(0);
            entity.setFavornum(0);
            //文章内容
            flag = super.save(entity);

            StrategyContent content = entity.getContent();
            content.setId(entity.getId());

            strategyContentMapper.insert(content);
        }else{
            //文章内容
            flag = super.updateById(entity);

            StrategyContent content = entity.getContent();
            content.setId(entity.getId());
            strategyContentMapper.updateById(content);
        }
        //执行es数据同步
        //方案1：mysql 数据 crud操作同时执行es的crud操作--不好，违背接口单一职责原则（一个方法做一件事情）
        //strategyEsService.save(es) / update(es) /delete(es);
        //方案2：使用try catch 实现，---不好，存在问题：
        // 1> 违背接口单一职责原则  2>如果es操作非常慢，影响saveOrupdate方法
        //try{
            //strategyEsService.save(es) / update(es) /delete(es);
        //}catch (Ext){}
        //方案3：使用 try catch 实现  + 异步方式
        //try{
          /* new Thread(new Runnable() {
               @Override
               public void run() {
                  // strategyEsService.save(es) / update(es) /delete(es);
               }
           }).start();*/
        //}catch (Ext){}
        //方案4：Spring自定义的监听事件--同步 / 异步  ---不好：数据一旦操作失败，数据无法重现了
        //1>自定义es数据同步事件EsCRUDEvent
        //2>自定义es数据同步事件监听器ESCRUDDataInitListener
        //3>当 mysql 数据实现crud操作时，马上触发EsCRUDEvent事件
        //ctx.publishEvent(new EsCRUDEvent(entity.getId(), "update"));
        //方案5：使用消息中间件的方式---不好点：针对数据比较单一
        //方案6：消息中间件 + canal（通过数据库日志进行监控）








        return flag;
    }

    @Override
    public StrategyContent getContent(Long id) {
        return strategyContentMapper.selectById(id);
    }

    @Override
    public List<Strategy> queryByCatatlogId(Long catalogId) {
        QueryWrapper<Strategy> wrapper = new QueryWrapper<>();
        wrapper.eq("catalog_id", catalogId);
        return super.list(wrapper);
    }

    @Override
    public List<Strategy> queryViewnumTop3(Long destId) {
        QueryWrapper<Strategy> wrapper = new QueryWrapper<>();
        wrapper.eq("dest_id", destId);
        wrapper.orderByDesc("viewnum");
        wrapper.last(" limit 3" );
        return super.list(wrapper);
    }

    @Override
    public void updateStatis(StrategyStatisVO vo) {
        UpdateWrapper<Strategy> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", vo.getStrategyId());
        wrapper.set("viewnum", vo.getViewnum());
        wrapper.set("replynum", vo.getReplynum());
        wrapper.set("favornum", vo.getFavornum());
        wrapper.set("sharenum", vo.getSharenum());
        wrapper.set("thumbsupnum", vo.getThumbsupnum());
        super.update(wrapper);
    }

    @Override
    public List<Strategy> queryByDestId(Long destId) {
        return super.list(new QueryWrapper<Strategy>().eq("dest_id", destId));
    }
}
