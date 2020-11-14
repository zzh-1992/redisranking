package com.grapefruit.redisranking.ranking;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.Jedis;

//@SpringBootConfiguration
class MyJedis{

    //实例连接地址，从控制台获取
    String host = "47.115.42.52";

    //Redis端口
    int port = 6380;
    private Jedis jedis;

    {
        jedis = new Jedis(host,port);
        jedis.auth("123456");
    }

    //@Bean
    public Jedis getJedis(){
        return jedis;
    }
}
