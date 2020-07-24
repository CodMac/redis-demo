package com.redisdemo.pulgins.redis.test;

import com.redisdemo.pulgins.redis.distributedLock.anno.DistributedLockAble;
import org.springframework.stereotype.Service;

@Service
public class DistributedService {

    // 分布式锁测试
    @DistributedLockAble(key = "DistributedService.someFunc", expire = 10, tryTimeout = 5, type = "luaSetNxLock")
    public void someFunc() throws InterruptedException {

        System.out.println(Thread.currentThread().getName() + " 我在持锁行凶中。。。。。");
        Thread.sleep(8000);
        System.out.println(Thread.currentThread().getName() + " 装逼完成，释放锁");

    }
}
