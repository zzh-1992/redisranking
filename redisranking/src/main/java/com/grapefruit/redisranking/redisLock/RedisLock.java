package com.grapefruit.redisranking.redisLock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

/**
 * @author 柚子苦瓜茶
 * @version 1.0
 * @ModifyTime 2020/11/7 06:31:04
 */
public class RedisLock {

    public static void main(String[] args) throws InterruptedException {
        String host = "47.115.42.52";
        int port = 6380;
        Jedis jedis = new Jedis(host, port);
        String authMsg = jedis.auth("123456");
        if (!authMsg.equals("OK")) {
            System.out.println("AUTH FAILED: " + authMsg);
        } else {
            System.out.println("Redis Successful");
        }

        Long lock = jedis.setnx("k1", "v1");

        // 方式一
        SetParams params = new SetParams();
        params.nx();
        params.ex(5);
        jedis.set("lock","v",params);

        // 方式二
        jedis.set("key","value");

        System.out.println("lock:" + lock);
        if(lock == 1){
            System.out.println("获取到锁，可以继续执行");
            //5秒后删除key
            Thread.sleep(5);
            jedis.del("k1");
        } else {
            System.out.println("锁获取失败。");
            //jedis.del("k1");
        }

    }
}
