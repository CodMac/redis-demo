package com.redisdemo;

import com.redisdemo.pulgins.redis.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
class RedisDemoApplicationTests {

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    @Qualifier("zqitRdsTemp")
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void setValue() {
        redisUtil.set("strKey1", "我是value");
        redisUtil.set("strKey2", 2);

        redisUtil.hset("hashKey1", "flied1",1);
        redisUtil.hset("hashKey1", "flied2","哈哈哈");

        redisUtil.lSet("listKey1", "我你太");
        redisUtil.lSet("listKey1", 2);

        redisUtil.sSet("setKey1", 1);
        redisUtil.sSet("setKey1", 1);

        redisUtil.zsSet("zsetKey1", 99.0, "内容啊");
        redisUtil.zsSet("zsetKey1", 1.0, 2);
    }

    @Test
    void removeAll(){
        redisUtil.flushAll();
    }

    @Test
    void getValue(){
        Object strKey1 = redisUtil.get("strKey1");
        Object strKey2 = redisUtil.get("strKey2");
        System.out.println(strKey1.toString() + " -- " + strKey2.toString());

        Object hashKey1_flied1 = redisUtil.hget("hashKey1", "flied1");
        Object hashKey1_flied2 = redisUtil.hget("hashKey1", "flied2");
        System.out.println(hashKey1_flied1.toString() + " -- " + hashKey1_flied2.toString());

    }

    @Test
    void mutiTest() throws InterruptedException {
        redisTemplate.multi();
        redisTemplate.opsForValue().increment("strKey2", 2);
        redisTemplate.exec();
    }
}
