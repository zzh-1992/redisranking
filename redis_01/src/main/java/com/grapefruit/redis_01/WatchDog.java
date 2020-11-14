package com.grapefruit.redis_01;

import org.springframework.data.redis.core.RedisTemplate;
import java.util.concurrent.TimeUnit;

/**
 * @author xujp
 */
public class WatchDog extends Thread {

    private RedisTemplate redisTemplate;

    private String uuid;

    public WatchDog(RedisTemplate redisTemplate, String uuid){
        this.redisTemplate = redisTemplate;
        this.uuid = uuid;
    }

    public void run(){
        // 续命逻辑
        while (true){
            try {
                // 获取锁的value
                Object redisUUID = redisTemplate.opsForValue().get("lock");
                // 判断当前父线程是否已经释放锁，如果父线程已释放，则跳出线程
                if(redisUUID==null || !redisUUID.toString().equals(uuid)){
                    break;
                }
                // 续命
                redisTemplate.expire("lock", 3l, TimeUnit.SECONDS);
                // 没隔1s续命一次
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }

        }
    }
}