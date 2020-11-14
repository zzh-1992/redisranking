package com.grapefruit.redisranking.ranking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author 柚子苦瓜茶  Grapefruit
 * @version 1.0
 * @ModifyTime 2020/9/5 09:35:14
 */

@RestController
public class RedisRanking_value_score {

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 测试url
     * http://127.0.0.1/ranking
     */
    @GetMapping("/ranking")
    public Object ranking() {
        System.out.println("==========80============");
        ZSetOperations<String,Integer> zSetOperations = redisTemplate.opsForZSet();

        //先清除缓存的key
        Boolean isKeyExist = redisTemplate.delete("ranking");
        System.out.println("isKeyExist:" + isKeyExist);

        //把数据存入缓存
        zSetOperations.incrementScore("ranking", 20, 2);
        zSetOperations.incrementScore("ranking", 10, 1);
        zSetOperations.incrementScore("ranking", 30, 3);
        zSetOperations.incrementScore("ranking", 80, 8);
        zSetOperations.incrementScore("ranking", 60, 6);

        //从缓存取数据
        Long size = zSetOperations.size("ranking");

        System.out.println("=====================================================");

        //取元素(score取值范围 min < score < max)
        Set<ZSetOperations.TypedTuple<Integer>> ranking1 = zSetOperations.rangeByScoreWithScores("ranking", 0, 4);
        for (ZSetOperations.TypedTuple t : ranking1
        ) {
            System.out.println(t.getValue() + "===>" + t.getScore());
        }

        //取元素(下标从0开始)
        System.out.println("=====================================");
        Set<ZSetOperations.TypedTuple<Integer>> ranking2 = zSetOperations.reverseRangeWithScores("ranking", 0, 4);
        for (ZSetOperations.TypedTuple t : ranking2
        ) {
            System.out.println(t.getValue() + "===>" + t.getScore());
        }
        return ranking2;
    }

    @GetMapping("/")
    public String s(){
        return "----80-----";
    }
}
