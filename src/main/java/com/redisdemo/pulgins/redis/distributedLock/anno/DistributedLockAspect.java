package com.redisdemo.pulgins.redis.distributedLock.anno;

import com.redisdemo.pulgins.redis.distributedLock.DistributedLock;
import com.redisdemo.pulgins.redis.distributedLock.impl.LuaSetNxLock;
import com.redisdemo.pulgins.redis.distributedLock.impl.SetNxLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Order(3)
@Aspect
@Component
public class DistributedLockAspect {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Before("@annotation(lock)")
    public void before(JoinPoint point, DistributedLockAble lock) {
    }

    @After("@annotation(lock)")
    public void after(JoinPoint point, DistributedLockAble lock) {

    }

    @Around("@annotation(lock)")
    public Object around(ProceedingJoinPoint pjp, DistributedLockAble lock) throws Throwable {
        Object proceed = null;

        String key = lock.key();
        long expire = lock.expire();
        long tryTimeout = lock.tryTimeout();
        String type = lock.type();

        DistributedLock distributedLock = buildLock(type, key, expire);
        if (distributedLock == null) {
            log.error("锁类型不存在");
            return proceed;
        }

        //获取锁
        Boolean tryLock = distributedLock.tryLock(tryTimeout);


        if (tryLock == true) {
            log.info("成功获得同步锁 - key(" + key + ") - 当前线程(" + Thread.currentThread().getName() + ")");
            // 继续执行
            Object[] args = pjp.getArgs();
            proceed = pjp.proceed(args);

            //释放锁
            distributedLock.unLock();
        } else {
            log.error("无法获得同步锁 - key(" + key + ") - 当前线程(" + Thread.currentThread().getName() + ")");
        }
        return proceed;
    }

    private DistributedLock buildLock(String lockType, String key, Long expire) {
        DistributedLock distributedLock = null;
        //setNxLock锁
        if (lockType.toLowerCase().equals("setNxLock".toLowerCase())) {
            distributedLock = new SetNxLock(key, redisTemplate, expire);
        }
        //luaSetNxLock锁
        else if (lockType.toLowerCase().equals("luaSetNxLock".toLowerCase())) {
            distributedLock = new LuaSetNxLock(key, redisTemplate, expire);
        }

        return distributedLock;
    }
}
