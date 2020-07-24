package com.redisdemo.pulgins.redis.flowControl.impl;

import com.redisdemo.pulgins.redis.flowControl.FlowCtrlBox;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 流量控制窗口
 * 单位时间内允许访问次数(zset)
 */
public class ZsetFlowCtrlBox implements FlowCtrlBox {

    private RedisTemplate<String, Object> redisTemplate;
    private String key;
    private Long expire;
    private Integer section;//窗口区间 s
    private Integer limmit;//限制数

    public ZsetFlowCtrlBox(RedisTemplate<String, Object> redisTemplate, String key, Long expire, Integer section, Integer limmit) {
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.expire = expire;
        this.section = section;
        this.limmit = limmit;
    }

    /**
     * 判断是否超过区间限制数
     * @return
     */
    @Override
    public Boolean flowIn() {
        String threadName = Thread.currentThread().getName();
        Long nowTime = new Date().getTime();

        Boolean result = false;
        /**
         * 1. 移除不在滑动区间内的数据
         * 2. 根据score判断当前滑动区间的访问人数
         * 3. 超过限制则返回false
         * 4. 未超过限制则返回true
         * 5. 添加访问记录到zset, score为当前时间戳
         */
        //1. 移除不在滑动区间内的数据
        double min = nowTime - TimeUnit.SECONDS.toMillis(section);
        double max = nowTime;
        redisTemplate.opsForZSet().removeRangeByScore(key, 0, min);
        //2. 根据score判断当前滑动区间的访问人数
//        Set<Object> objects = redisTemplate.opsForZSet().rangeByScore(key, min, max);
        Long count = redisTemplate.opsForZSet().count(key, min, max);
        //3. 超过限制则返回false
        if (count >= limmit) {
            return result;
        }
        //4. 未超过限制则返回true
        else {
            result = true;
        }
        //5. 添加访问记录到zset, score为当前时间戳
        redisTemplate.opsForZSet().add(key, threadName + ":" + UUID.randomUUID().toString(), nowTime);

        return result;
    }

}
