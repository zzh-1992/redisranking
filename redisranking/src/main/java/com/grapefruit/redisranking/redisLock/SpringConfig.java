package com.grapefruit.redisranking.redisLock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author 柚子苦瓜茶
 * @version 1.0
 * @ModifyTime 2020/11/7 06:59:20
 */
@Configuration
public class SpringConfig {

    @Bean
    public JedisPool jedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(101);
        config.setMaxIdle(1000);
        config.setMinIdle(1);
        config.setMaxWaitMillis(2000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        return new JedisPool(config,"47.11542.52",6380);
    }
}
