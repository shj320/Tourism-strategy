package com.sj.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sj.domain.Destination;
import com.sj.domain.Region;
import com.sj.mapper.DestinationMapper;
import com.sj.query.DestinationQuery;
import com.sj.service.IDestinationService;
import com.sj.service.IRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import sun.security.krb5.internal.crypto.Des;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@Transactional
public class DestinationServiceImpl extends ServiceImpl<DestinationMapper, Destination> implements IDestinationService {

    @Autowired
    private IRegionService regionService;


    @Override
    public List<Destination> queryByRegionId(Long rid) {
        Region byId = regionService.getById(rid);
        List<Long> longs = byId.parseRefIds();


        return super.listByIds(longs);
    }

    @Override
    public List<Destination> queryByRegionIdForApi(Long rid) {
        List<Destination> list=new ArrayList<>();
        if(rid==-1){
            QueryWrapper<Destination> destinationQueryWrapper = new QueryWrapper<>();
            destinationQueryWrapper.eq("parent_id",1);
            list=super.list(destinationQueryWrapper);
        }else {
           list=this.queryByRegionId(rid);
        }
        for (Destination des : list){
            QueryWrapper<Destination> destinationQueryWrapper = new QueryWrapper<>();
            destinationQueryWrapper.eq("parent_id",des.getId());
            destinationQueryWrapper.last("limit 5");
            des.setChildren(super.list(destinationQueryWrapper));
        }
        return list;
    }


    @Override
    public List<Destination> queryToasts(Long destId) {
        List<Destination> list = new ArrayList<>();

        this.createToasts(list, destId);
        Collections.reverse(list);
        return list;
    }

    private void createToasts(List<Destination> list, Long destId){
        if(destId == null){
            return;
        }
        Destination d = super.getById(destId);
        list.add(d);
        if(d.getParentId() != null){
            this.createToasts(list, d.getParentId());
        }
    }

    @Override
    public Destination queryByName(String name) {
        return super.getOne(new QueryWrapper<Destination>().eq("name", name));
    }

    @Override
    public Page<Destination> queryPage(DestinationQuery qo) {
        QueryWrapper<Destination> destinationQueryWrapper = new QueryWrapper<>();
        destinationQueryWrapper.like(StringUtils.hasText(qo.getKeyword()),"name",qo.getKeyword());
        destinationQueryWrapper.isNull(qo.getParentId()==null,"parent_id");
        destinationQueryWrapper.eq(qo.getParentId()!=null,"parent_id",qo.getParentId());
        Page<Destination> destinationPage = new Page<>(qo.getCurrentPage(),qo.getPageSize());
        return super.page(destinationPage,destinationQueryWrapper);
    }


}
