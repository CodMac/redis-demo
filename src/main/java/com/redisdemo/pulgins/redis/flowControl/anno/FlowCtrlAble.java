package com.redisdemo.pulgins.redis.flowControl.anno;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FlowCtrlAble {
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
     * 窗口区间 s
     * @return
     */
    public int section() default 10;
    /**
     * 限制数
     * @return
     */
    public int limmit() default 10;

    /**
     * 类型
     * list zset
     * @return
     */
    public String type() default "zset";
}
