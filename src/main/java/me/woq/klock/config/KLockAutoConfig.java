package me.woq.klock.config;

import me.woq.klock.aop.KLockAop;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * description: KLockAutoConfig
 * date: 2022/3/30 16:52
 * author: YangTao
 */
@Configuration
//@AutoConfigureAfter(RedisTemplate.class)
//@ConditionalOnBean(RedisTemplate.class)
public class KLockAutoConfig {

    @Bean
    public KLockAop kLockAop(){
        return new KLockAop();
    }

}
