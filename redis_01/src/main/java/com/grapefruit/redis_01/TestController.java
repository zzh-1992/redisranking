package com.grapefruit.redis_01;

//import com.redis.demo1.thread.WatchDog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Grapefruit
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping
    public void lock(){
        String uuid = UUID.randomUUID().toString();
        //System.out.println(uuid);
        WatchDog watchDog;
        try {
            // 自旋
            while (true) {
                // 尝试获取锁
                Boolean hasLock = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 3l, TimeUnit.SECONDS);
                if(hasLock) {
                    // 看门狗“续命“
                    watchDog = new WatchDog(redisTemplate, uuid);
                    watchDog.start();
                    // 业务逻辑start
                    int num = (int) redisTemplate.opsForValue().get("num");
                    //Thread.sleep(4000); // 假设业务需要4s处理时间
                    redisTemplate.opsForValue().set("num", num - 1);
                    System.out.println(num);
                    // 业务逻辑处理 end
                    break;
                }else{
                    // 睡眠100ms再自旋
                    Thread.sleep(100);
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }finally {
            // 关闭锁
            String l = (String) redisTemplate.opsForValue().get("lock");
            if (l.equalsIgnoreCase(uuid)) {
                redisTemplate.delete("lock");
            }
        }
    }
}