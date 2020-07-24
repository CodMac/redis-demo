package com.redisdemo.pulgins.redis.distributedLock.anno;

import java.lang.annotation.*;

/**
 * 分布式锁
 * @author mac
 * @see
 * {@value key - redis KEY}
 * {@value expire - redis KEY 过期时间 /s}
 * {@value tryTimeout - 获取锁最长等待时间 /s}
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLockAble {
    /**
     * redis KEY
     * @return
     */
    public String key();
    /**
     * redis KEY 过期时间 /s
     * @return
     */
    public long expire() default 10;
    /**
     * 获取锁最长等待时间 /s
     * @return
     */
    public long tryTimeout() default 10;

    /**
     * 锁实现类型
     * setNxLock
     * luaSetNxLock
     * @return
     */
    public String type() default "setNxLock";;
}
