package com.sj.redis.service.impl;


import com.alibaba.fastjson.JSON;
import com.sj.domain.Strategy;
import com.sj.redis.service.IStrategyStatisVORedisService;
import com.sj.redis.util.RedisKeys;
import com.sj.redis.vo.StrategyStatisVO;
import com.sj.service.IStrategyService;
import com.sj.util.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class StrategyStatisVORedisServiceImpl implements IStrategyStatisVORedisService {


    @Autowired
    private IStrategyService strategyService;
    @Autowired
    private StringRedisTemplate template;
    @Override
    public void veiwnumIncr(Long sid) {
        StrategyStatisVO vo = this.getStatisVO(sid);
        //veiwnum + 1
        vo.setViewnum(vo.getViewnum() + 1);
        //更新vo对象
        this.setStatisVO(vo);

    }
    @Override
    public void replynumIncr(Long sid) {
        StrategyStatisVO vo = this.getStatisVO(sid);
        //replynum + 1
        vo.setReplynum(vo.getReplynum() + 1);
        //更新vo对象
        this.setStatisVO(vo);
    }

    @Override
    public StrategyStatisVO getStatisVO(Long sid) {
        //拼接vo key
        String key = RedisKeys.STRATEGY_STATIS_VO.join(sid.toString());
        StrategyStatisVO vo = null;
        //判断key是否存在
        if(template.hasKey(key)){
            //存在， 获取
            String voStr = template.opsForValue().get(key);
            vo = JSON.parseObject(voStr, StrategyStatisVO.class);

            //hash
            //Map<Object, Object> entries = template.opsForHash().entries(key);
        }else{
            //不存在， 创建， 初始化， 缓存
            vo = new StrategyStatisVO();
            //初始化
            Strategy strategy = strategyService.getById(sid);
            BeanUtils.copyProperties(strategy, vo);
            vo.setStrategyId(strategy.getId());
            //缓存
            template.opsForValue().set(key, JSON.toJSONString(vo));
            //hash
            //template.opsForHash().putAll(key, map);
        }
        return vo;
    }


    @Override
    public void setStatisVO(StrategyStatisVO vo) {
        //拼接vo key
        String key = RedisKeys.STRATEGY_STATIS_VO.join(vo.getStrategyId().toString());
        template.opsForValue().set(key, JSON.toJSONString(vo));
    }

    @Override
    public boolean favor(Long sid, Long uid) {
        //拼接收藏列表的key
        String key = RedisKeys.USER_STRATEGY_FAVOR.join(uid.toString());
        //判断key是否存在， 不存在， 创建， 初始化， 缓存
        if(!template.hasKey(key)){
            //set--当前使用set
            Set<String> set = new HashSet<>();  //查询mysql 攻略收藏列表--当前用户收藏的所有攻略id
            //redis 默认命令如果set为null，会自动删除该key，所以设置一个-1，不让redis删除
            template.opsForSet().add(key, "-1");
            //list
            //template.opsForList().rightPush(key, .....)

        }
        //使用命令判断sid是否在列表中
        StrategyStatisVO vo = this.getStatisVO(sid);
        boolean flag = false;

        //if(template.opsForList().indexOf(key, sid.toString()) != -1){
        //}else{
        // }

        if(template.opsForSet().isMember(key, sid.toString())){
            //如果在，取消操作， 收藏数-1， 将sid移除
            vo.setFavornum(vo.getFavornum() -1);
            template.opsForSet().remove(key, sid.toString());
        }else{
            //如果不在，收藏操作， 收藏数+1， 将sid添加
            vo.setFavornum(vo.getFavornum() + 1);
            template.opsForSet().add(key, sid.toString());
            flag = true;
        }
        //更新vo对象
        this.setStatisVO(vo);
        //return flag;
        return template.opsForSet().isMember(key, sid.toString());
    }

    @Override
    public boolean isSidExist(Long sid, Long userId) {
        String key = RedisKeys.USER_STRATEGY_FAVOR.join(userId.toString());
        return template.opsForSet().isMember(key, sid.toString());
    }

    @Override
    public boolean strategyThumbup(Long sid, Long uid) {

        //拼接出标记key
        String key = RedisKeys.USER_STRATEGY_THUMB.join(sid.toString(), uid.toString());
        //判断key 是否存在
        if(!template.hasKey(key)){
            //不存在， 缓存key， value任意， 有效时间：当前时间到今天最后一秒
            Date now = new Date();
            Date end = DateUtil.getEndDate(now);
            Long time = DateUtil.getDateBetween(now, end);
            template.opsForValue().set(key, "1", time, TimeUnit.SECONDS);
            //点赞数+1
            StrategyStatisVO vo = this.getStatisVO(sid);
            vo.setThumbsupnum(vo.getThumbsupnum() + 1);
            //vo更新
            this.setStatisVO(vo);
            return true;
        }
        return false;
    }

    @Override
    public boolean isVOExist(Long id) {
        String key = RedisKeys.STRATEGY_STATIS_VO.join(id.toString());
        return template.hasKey(key);
    }

    @Override
    public List<StrategyStatisVO> queryByPattern(String pattern) {
        List<StrategyStatisVO> vos = new ArrayList<>();
        //pattern : strategy_statis_vo:*
        //keys strategy_statis_vo:*
        Set<String> keys = template.keys(pattern);
        if(keys != null && keys.size() > 0){
            for (String key : keys) {
                String voStr = template.opsForValue().get(key);
                vos.add(JSON.parseObject(voStr, StrategyStatisVO.class));
            }
        }
        return vos;
    }
}
