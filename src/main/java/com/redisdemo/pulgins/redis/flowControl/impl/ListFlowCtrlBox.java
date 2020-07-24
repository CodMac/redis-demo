package com.redisdemo.pulgins.redis.flowControl.impl;

import com.redisdemo.pulgins.redis.flowControl.FlowCtrlBox;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;

/**
 * 流量控制窗口(List)
 */
public class ListFlowCtrlBox implements FlowCtrlBox {

    private RedisTemplate<String, Object> redisTemplate;
    private String key;
    private Long expire;
    private Integer section;//窗口区间 s
    private Integer limmit;//限制数

    public ListFlowCtrlBox(RedisTemplate<String, Object> redisTemplate, String key, Long expire, Integer section, Integer limmit) {
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.expire = expire;
        this.section = section;
        this.limmit = limmit;
    }

    @Override
    public Boolean flowIn() {
        String threadName = Thread.currentThread().getName();
        Long nowTime = new Date().getTime();

        //1. 判断当前窗口是否已经满格
        Long size = redisTemplate.opsForList().size(key);
        if(size < limmit){
            //添加最新访问时间
            redisTemplate.opsForList().leftPush(key, nowTime);
            return true;
        }
        //2. 已经满格，判断最早访问的格子是否过时
        Long lastTime = (Long) redisTemplate.opsForList().index(key, limmit-1);
        long throughTime = nowTime - lastTime;
        if(throughTime < section*1000){
            return false;
        }else{
            redisTemplate.opsForList().rightPop(key);
            redisTemplate.opsForList().leftPush(key, nowTime);
            return true;
        }
    }

}
