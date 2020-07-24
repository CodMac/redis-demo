package com.redisdemo.pulgins.redis.distributedLock.impl;

import com.redisdemo.pulgins.redis.distributedLock.DistributedLock;
import com.redisdemo.pulgins.redis.distributedLock.DistributedLockAbst;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Collections;
import java.util.concurrent.TimeUnit;


/**
 * 分布式锁
 */
@Slf4j
@Data
public class LuaSetNxLock extends DistributedLockAbst {

    public LuaSetNxLock(String key, RedisTemplate<String, Object> redisTemplate, Long expire) {
        this.key = key;
        this.redisTemplate = redisTemplate;
        this.expire = expire;
        this.waitMillisPer = 200L;
    }
    public LuaSetNxLock(String key, RedisTemplate<String, Object> redisTemplate, Long expire, Long safetyTime) {
        this.key = key;
        this.redisTemplate = redisTemplate;
        this.expire = expire;
        this.waitMillisPer = 200L;
    }


    @Override
    public Boolean tryLock(Long timeout) throws InterruptedException {
        Long waitMax = TimeUnit.SECONDS.toMillis(timeout);
        Long waitAlready = 0L;
        String threadName = Thread.currentThread().getName();

        String script = "if redis.call('setNx',KEYS[1],ARGV[1]) then if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('expire',KEYS[1],ARGV[2]) else return 0 end end";
        RedisScript<Long> tRedisScript = new DefaultRedisScript<>(script, Long.class);

        log.info("当前线程: " + threadName + " - 尝试请求锁key(" + key + ") - 最长等待时间(" + waitMax + ")ms ");

        while (redisTemplate.execute(tRedisScript, Collections.singletonList(key), threadName, expire) != 1 && waitAlready < waitMax) {
            log.info("当前线程: " + threadName + " - 锁key(" + key + ")被占有，等待阻塞中 - 已等待(" + waitAlready + ")ms");
            waitAlready += waitMillisPer;
            Thread.sleep(waitMillisPer);
        }

        if (waitAlready < waitMax) {
            return true;
        }
        return false;

    }

    @Override
    public void unLock() {
        String threadName = Thread.currentThread().getName();

        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Long object = redisTemplate.execute(redisScript, Collections.singletonList(key), threadName);
        System.out.println(object);
        if (object == 1) {
            log.info("当前线程: " + threadName + " - 锁key(" + key + ")释放");
        }
    }
}
