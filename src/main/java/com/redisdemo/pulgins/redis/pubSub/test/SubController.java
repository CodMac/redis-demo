package com.redisdemo.pulgins.redis.pubSub.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

@RestController
public class SubController {

    @GetMapping("/pub")
    public String pub2Channel(String channel, String message) {

        Jedis jedis = new Jedis();
        try{
            //发送广播
            jedis.publish(channel, message);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            jedis.close();
        }
        return "SUCCESS";
    }

}
