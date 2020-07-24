package com.redisdemo.pulgins.redis.distributedLock;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 */
public interface DistributedLock {

    /**
     * 尝试获取锁，并至多等待timeout时长
     *
     * @param timeout 超时时长(s)
     *
     * @return 是否获取成功
     * @throws InterruptedException
     */
    public Boolean tryLock(Long timeout) throws InterruptedException;

    /**
     * 释放锁
     */
    public void unLock();
}
