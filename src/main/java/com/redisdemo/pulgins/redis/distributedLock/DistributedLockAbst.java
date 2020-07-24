package com.redisdemo.pulgins.redis.distributedLock;

import org.springframework.data.redis.core.RedisTemplate;

public abstract class DistributedLockAbst implements DistributedLock {
    /**
     * key
     */
    public String key = null;
    /**
     * 过期时间
     */
    public Long expire = null;
    /**
     * 获取锁失败时，下次获取的等待阻塞时间 /ms
     */
    public Long waitMillisPer = null;
    public RedisTemplate<String, Object> redisTemplate = null;

}
