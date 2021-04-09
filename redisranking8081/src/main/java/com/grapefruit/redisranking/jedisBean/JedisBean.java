package com.grapefruit.redisranking.jedisBean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

/**
 * @author Grapefruit
 * @version 1.0
 * @date 2021/4/9
 */
@Configuration
public class JedisBean {

    @Bean
    public Jedis jedis(){
        String host = "127.0.0.1";
        int port = 6379;
        Jedis jedis = new Jedis(host, port);
        jedis.auth("123456");
        return jedis;
    }
}
