package com.redisdemo.pulgins.redis.test;

import com.redisdemo.pulgins.redis.delayQueen.DelayTaskQueen;
import com.redisdemo.pulgins.redis.flowControl.anno.FlowCtrlAble;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/redisTest")
public class RedisTestCtrl {

    @Autowired
    DistributedService distributedService;
    @Autowired
    private DelayTaskQueen delayTaskQueen;

    /**
     * 分布式锁 - 注解
     *
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/distributedLock/someFunc")
    @ResponseBody
    public String someFunc() throws InterruptedException {

        distributedService.someFunc();

        return "someFunc";
    }

    /**
     * 分布式锁 - 注解
     *
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/distributedLock/someFunc2")
    @ResponseBody
    public String someFunc2() throws InterruptedException {

        distributedService.someFunc();

        return "someFunc2";
    }

    /**
     * 流量控制测试
     * @return
     */
    @FlowCtrlAble(key="RedisTestCtrl.oneFunc", expire = 10, section = 10, limmit = 2)
    @GetMapping("/flowCtrlBox/tt")
    @ResponseBody
    public String flowCtrlBox(){
        return "flowCtrlBoxTest";
    }

    @GetMapping("/taskAdd")
    @ResponseBody
    public String taskAdd(String taskName){
        delayTaskQueen.add(taskName, 5000L);
        return "taskAdd";
    }
}
