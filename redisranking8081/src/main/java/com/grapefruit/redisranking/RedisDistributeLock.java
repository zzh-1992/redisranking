package com.grapefruit.redisranking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author 柚子苦瓜茶
 * @version 1.0
 * @ModifyTime 2020/11/7 06:31:04
 */
@RestController
public class RedisDistributeLock {

    @Autowired
    private Jedis jedis;

    private static final int LOCK_TTL = 3;

    private static final String LOCK = "lock";


    @RequestMapping("/redis")
    public String redisLock()  {
        long begin = System.currentTimeMillis();

        Timer timer = new Timer();
        // 启动定时器
        myTimer(jedis,LOCK,timer);
        try {
            if(tryLock( LOCK_TTL)){
                // 模拟任务时长
                Thread.sleep(10 * 1000);

                // 任务结束删除琐,取消定时器
                jedis.del(LOCK);
                timer.cancel();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (System.currentTimeMillis() - begin) + "";
    }

    // 尝试获取锁
    private boolean tryLock(int secondsToExpire){
        SetParams params = new SetParams();
        params.nx();
        params.ex(secondsToExpire);
        String set = jedis.set(LOCK, "",params);
        return "OK".equals(set);
    }

    // 定时器
    private void myTimer(Jedis jedis,String lock ,Timer timer){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if(jedis.exists(lock)){
                        jedis.expire(lock,LOCK_TTL);
                        System.out.println("===redis续命====>" + LocalDateTime.now());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, (long) LOCK_TTL * 1000 * 2 / 3, (long) LOCK_TTL * 1000 * 2 / 3);
    }
}
