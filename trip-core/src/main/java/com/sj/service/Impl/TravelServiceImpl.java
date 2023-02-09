package com.sj.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sj.domain.Travel;
import com.sj.domain.TravelContent;
import com.sj.exception.LogicException;
import com.sj.mapper.TravelContentMapper;
import com.sj.mapper.TravelMapper;
import com.sj.query.TravelCondition;
import com.sj.query.TravelQuery;
import com.sj.service.ITravelService;
import com.sj.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
* 游记服务接口实现
*/
@Service
@Transactional
public class TravelServiceImpl extends ServiceImpl<TravelMapper, Travel> implements ITravelService {


    @Autowired
    private TravelContentMapper travelContentMapper;
    @Autowired
    private IUserInfoService userInfoService;
    @Override
    public IPage<Travel> queryPage(TravelQuery qo) {
        IPage<Travel> page = new Page<>(qo.getCurrentPage(), qo.getPageSize());
        QueryWrapper<Travel> wrapper = Wrappers.<Travel>query();
        wrapper.eq(qo.getDestId() != null, "dest_id", qo.getDestId());


        //出行天数
        TravelCondition day = TravelCondition.DAY_MAP.get(qo.getDayType());
        if(day != null){
            wrapper.between("day", day.getMin(), day.getMax());
        }

        //人均消费
        TravelCondition consume = TravelCondition.CONSUME_MAP.get(qo.getConsumeType());
        if(consume != null){
            wrapper.between("avg_consume", consume.getMin(), consume.getMax());
        }


        //旅游时间
        TravelCondition time = TravelCondition.TIME_MAP.get(qo.getTravelTimeType());
        if(time != null){
            wrapper.between("MONTH(travel_time)", time.getMin(), time.getMax());
        }


        IPage<Travel> page1 = super.page(page, wrapper);

        for (Travel record : page1.getRecords()) {
            record.setAuthor(userInfoService.getById(record.getAuthorId()));
        }
        return page1;
    }

    @Override
    public TravelContent getContent(Long id) {
        return travelContentMapper.selectById(id);
    }

    @Override
    public void audit(Long id, int state) {
        //满足审条件
        Travel travel = super.getById(id);
        if(travel == null || travel.getState() !=Travel.STATE_WAITING){
            throw new LogicException("参数异常");
        }
        if(state == Travel.STATE_RELEASE){
            //审核通过
            //审核状态-审核通过
            travel.setState(Travel.STATE_RELEASE);
            //审核时间
            travel.setReleaseTime(new Date());
            //最后更新时间
            travel.setLastUpdateTime(new Date());
            //审核记录--审核人，审核状态，审核意见，审核记录，审核时间.....
            super.updateById(travel);
        }else{
            //审核拒绝
            //审核状态-审核拒绝
            travel.setState(Travel.STATE_REJECT);
            //审核时间
            travel.setReleaseTime(null);
            //最后更新时间
            travel.setLastUpdateTime(new Date());
            //审核记录--审核人，审核状态，审核意见，审核记录，审核时间.....
            super.updateById(travel);
        }







    }

    @Override
    public List<Travel> queryViewnumTop3(Long destId) {
        QueryWrapper<Travel> wrapper = new QueryWrapper<>();
        wrapper.eq("dest_id", destId);
        wrapper.orderByDesc("viewnum");
        wrapper.last(" limit 3" );
        return super.list(wrapper);
    }


    @Override
    public List<Travel> queryByDestId(Long destId) {
        List<Travel> list = super.list(new QueryWrapper<Travel>().eq("dest_id", destId));
        for (Travel travel : list) {
            travel.setAuthor(userInfoService.getById(travel.getAuthorId()));
        }
        return list;
    }

    @Override
    public IPage<Travel> queryPages() {
        Page<Travel> travelPage = new Page<>(1,10);

        return super.page(travelPage);
    }

    @Override
    public IPage<Travel> queryPage1(int orderType) {
        QueryWrapper<Travel> travelQueryWrapper = new QueryWrapper<>();
        travelQueryWrapper.orderByAsc("viewnum");
        Page<Travel> objectPage = new Page<>(1, 10);
        return super.page(objectPage,travelQueryWrapper);
    }
}
