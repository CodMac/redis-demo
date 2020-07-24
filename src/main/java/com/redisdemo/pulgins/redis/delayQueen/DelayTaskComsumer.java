package com.redisdemo.pulgins.redis.delayQueen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class DelayTaskComsumer {

    @Autowired
    private DelayTaskQueen delayTaskQueen;

    @Scheduled(cron = "*/5 * * * * ?")
    public void excute(){
        String task = delayTaskQueen.popTaskName();
        if(StringUtils.isEmpty(task)){
            System.out.println("没有任务");
        }else{
            System.out.println("任务: " + task);
        }
    }

}
