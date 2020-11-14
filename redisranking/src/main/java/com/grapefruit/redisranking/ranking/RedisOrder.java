package com.grapefruit.redisranking.ranking;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author 柚子苦瓜茶
 * @version 1.0
 * @ModifyTime 2020/11/12 19:30:54
 */
@RestController
@RequestMapping("/")
public class RedisOrder {
    @Autowired
    private static final String KEY = "key";

    private static final String LOCK = "lock";

//    @Autowired
//    Jedis jedis;

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    /*@GetMapping("/get")
    public String get(){
        // 先获取值
        int i = Integer.parseInt(jedis.get(KEY));
        // 每次请求减1
        i--;
        // 写入redis
        jedis.set("key", i+"");
        // 返回余量
        String value = jedis.get(KEY);
        System.out.println("8080  余量:" + value);
        return "8080  " + value;
    }*/

    @GetMapping("/get")
    public synchronized String get(){
        while (!lock()){}
        // 先获取值
        int i = Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get(KEY)));
        if(i == 0){
            releaseLock();
            System.out.println("8080  余量:" + "ByBy");
            return "ByBy";
        }
        // 每次请求减1
        i = i - 1;
        // 写入redis
        redisTemplate.opsForValue().set(KEY, i +"");
        //jedis.set("key", i+"");
        // 返回余量
        String value = redisTemplate.opsForValue().get(KEY);
        System.out.println("8080  余量:" + value);
        //任务完成释放锁
        releaseLock();
        return "8080  " + value;
    }

    @GetMapping("/set")
    public String set(){
        System.out.println("setIfAbsent:" + redisTemplate.opsForValue().setIfAbsent(KEY, "100"));
        return "=====";
    }

    public boolean lock(){
        ValueOperations ops = redisTemplate.opsForValue();
        return ops.setIfAbsent(LOCK, LOCK,2, TimeUnit.SECONDS);
    }

    public boolean releaseLock(){
        return redisTemplate.delete(LOCK);
    }
}

