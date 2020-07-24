package com.redisdemo.pulgins.redis.flowControl.anno;

import com.redisdemo.pulgins.exceptionAdvice.exception.FlowCtrlExceptoin;
import com.redisdemo.pulgins.redis.flowControl.FlowCtrlBox;
import com.redisdemo.pulgins.redis.flowControl.impl.ListFlowCtrlBox;
import com.redisdemo.pulgins.redis.flowControl.impl.ZsetFlowCtrlBox;
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
@Order(2)
@Aspect
@Component
public class FlowCtrlAspect {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Before("@annotation(ctrl)")
    public void before(JoinPoint point, FlowCtrlAble ctrl) {
    }

    @After("@annotation(ctrl)")
    public void after(JoinPoint point, FlowCtrlAble ctrl) {
    }

    @Around("@annotation(ctrl)")
    public Object around(ProceedingJoinPoint pjp, FlowCtrlAble ctrl) throws Throwable {
        Object proceed = null;

        String key = ctrl.key();
        long expire = ctrl.expire();
        int section = ctrl.section();
        int limmit = ctrl.limmit();
        String type = ctrl.type();

        FlowCtrlBox box = null;
        if(type.toLowerCase().equals("list")){
            box = new ListFlowCtrlBox(redisTemplate, key, expire, section, limmit);
        }else{
            box = new ZsetFlowCtrlBox(redisTemplate, key, expire, section, limmit);
        }

        Boolean flowIn = box.flowIn();

        if (flowIn == true) {
            log.info("接口流量控制 - 访问成功 - key(" + key + ") - 当前线程(" + Thread.currentThread().getName() + ")");
            // 继续执行
            Object[] args = pjp.getArgs();
            proceed = pjp.proceed(args);
        } else {
            log.error("接口流量控制 - 访问限制 - key(" + key + ") - 当前线程(" + Thread.currentThread().getName() + ")");
            throw new FlowCtrlExceptoin();
        }
        return proceed;
    }

}
