package com.redisdemo.pulgins.redis.delayQueen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * 延时任务
 */
@Component
public class DelayTaskQueen {
    private final String key = "DelayTaskQueen";
    private final String taskDecollator = ":";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 添加延时任务
     *
     * @param taskName
     * @return
     */
    public boolean add(String taskName) {
        Long score = new Date().getTime();
        if (taskName.contains(taskDecollator)) {
            System.out.println("延时任务参数不能包含 '" + taskDecollator + "' ");
            return false;
        }
        String task = taskName + taskDecollator + UUID.randomUUID().toString().replaceAll("-", "");
        Boolean r = redisTemplate.opsForZSet().add(key, task, score);
        return r;
    }

    public boolean add(String taskName, Long score) {
        if (taskName.contains(taskDecollator)) {
            System.out.println("延时任务参数不能包含 '" + taskDecollator + "' ");
            return false;
        }
        String task = taskName + taskDecollator + UUID.randomUUID().toString().replaceAll("-", "");
        Boolean r = redisTemplate.opsForZSet().add(key, task, score + new Date().getTime());
        return r;
    }

    /**
     * 弹出任务
     *
     * @return
     */
    public String popTaskName() {
        Long max = new Date().getTime();
        Set<Object> taskSets = redisTemplate.opsForZSet().rangeByScore(key, 0, max);
        if (CollectionUtils.isEmpty(taskSets)) {
            return null;
        } else {
            String task = taskSets.toArray()[0].toString();
            redisTemplate.opsForZSet().remove(key, task);
            return task.split(taskDecollator)[0];

        }
    }
}
