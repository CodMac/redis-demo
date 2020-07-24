package com.redisdemo.pulgins.redis.distributedLock.impl;

import com.redisdemo.pulgins.redis.distributedLock.DistributedLock;
import com.redisdemo.pulgins.redis.distributedLock.DistributedLockAbst;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 */
@Slf4j
@Data
public class SetNxLock extends DistributedLockAbst {

    public SetNxLock(String key, RedisTemplate<String, Object> redisTemplate, Long expire) {
        this.key = key;
        this.redisTemplate = redisTemplate;
        this.expire = expire;
        this.waitMillisPer = 200L;
    }
    public SetNxLock(String key, RedisTemplate<String, Object> redisTemplate, Long expire, Long safetyTime) {
        this.key = key;
        this.redisTemplate = redisTemplate;
        this.expire = expire;
        this.waitMillisPer = 200L;
    }

    @Override
    public Boolean tryLock(Long timeout) throws InterruptedException {
        Long waitMax = TimeUnit.SECONDS.toMillis(timeout);
        Long waitAlready = 0L;

        //当前线程name
        String threadName = Thread.currentThread().getName();

        log.info("当前线程: " + threadName + " - 尝试请求锁key("+key+") - 最长等待时间("+ waitMax+")ms " );
        //setIfAbsent函数(对应SETNX命令)
        while (redisTemplate.opsForValue().setIfAbsent(key, threadName, expire, TimeUnit.SECONDS) != true && waitAlready < waitMax) {
            log.info("当前线程: " + threadName + " - 锁key("+key+")被占有，等待阻塞中 - 已等待(" + waitAlready + ")ms");
            waitAlready += waitMillisPer;
            Thread.sleep(waitMillisPer);
        }

        //设置有效时间
        if (waitAlready < waitMax) {
            return true;
        }
        return false;
    }

    @Override
    public void unLock(){
        Object object = redisTemplate.opsForValue().get(key);

        //当前线程name
        String value = Thread.currentThread().getName();

        if(object != null){
            //只能释放 当前线程所持有的锁
            if(value.equals(object.toString())){
                redisTemplate.delete(key);
                log.info("当前线程: " + value + " - 锁key(" + key + ")释放");
            }
        }
    }

}
