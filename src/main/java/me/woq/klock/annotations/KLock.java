package me.woq.klock.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * description: KLock
 * date: 2022/3/30 14:38
 * author: YangTao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface KLock {

    /**
     *  存储在redis里面的key SPEL 规则
     */
    String key() default "";


    /**
     * 过期时间，单位分钟
     */
    int timeOut() default 3;
}
