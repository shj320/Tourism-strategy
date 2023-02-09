package com.sj.redis.service;




import com.sj.redis.vo.StrategyStatisVO;

import java.util.List;

/**
 * 攻略统计vo对象缓存服务层
 */
public interface IStrategyStatisVORedisService {

    /**
     * 阅读数 + 1
     * @param sid
     */
    void veiwnumIncr(Long sid);

    /**
     * 获取vo对象
     * @param sid
     * @return
     */
    StrategyStatisVO getStatisVO(Long sid);

    /**
     * 设置vo对象
     * @param vo
     */
    void setStatisVO(StrategyStatisVO vo);

    /**
     * 评论数 + 1
     * @param sid
     */
    void replynumIncr(Long sid);

    /**
     * 攻略收藏
     * @param sid
     * @param uid
     * @return true：收藏操作， false：取消收藏
     */
    boolean favor(Long sid, Long uid);

    /**
     * 判断攻略是否在被用户收藏
     * @param sid
     * @param userId
     * @return
     */
    boolean isSidExist(Long sid, Long userId);

    /**
     * 用户点赞操作
     * @param sid
     * @param uid
     * @return true：顶成功， false：今天顶过
     */
    boolean strategyThumbup(Long sid, Long uid);

    /**
     * 判断指定攻略id vo对象是否存在
     * @param id
     * @return
     */
    boolean isVOExist(Long id);

    /**
     * 通过指定key规则， 查询vo对象列表
     * @param join
     * @return
     */
    List<StrategyStatisVO> queryByPattern(String join);
}
