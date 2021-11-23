/*
 *Copyright @2021 Grapefruit. All rights reserved.
 */

package com.grapefruit.redisranking.ranking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * redis 双检查
 *
 * @author 柚子苦瓜茶
 * @version 1.0
 */
@Service
public class GetDataFromRedis {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    public String queryUserCount() {
        String userCount = null;
        //先去缓存中取数据
        userCount = redisTemplate.opsForValue().get("userCount");

        //若是缓存没有数据就去数据库查找
        if (userCount == null) {
            //由于只希望执行一次数据库操作,若是有多个线程,且多个线程都准备操作数据库,那么就把当前对象“锁”住,
            //保证只有一个线程操作数据库

            //b> 当第二或其他线程也准备获取数据时,由于第一个线程已经存了数据到缓存,
            // 那么其他线程就先去缓存取数据。(缓存过期后才操作数据库)

            //a> 当第一个线程完成数据库操作后,缓存中有了数据。
            synchronized (this) {
                userCount = redisTemplate.opsForValue().get("userCount");
                if (userCount == null) {
                    //到数据库查找数据
                    //userCount = UserDao.queryAllUser();
                    userCount = "1";
                    System.out.println("userCount>>>>>>数据库>>>>>>");
                    //把查到的数据存入缓存
                    redisTemplate.opsForValue().set("userCount", userCount, 30, TimeUnit.SECONDS);
                }
            }
        }
        return userCount;
    }
}
