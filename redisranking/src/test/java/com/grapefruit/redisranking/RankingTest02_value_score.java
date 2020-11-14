package com.grapefruit.redisranking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author 柚子苦瓜茶
 * @version 1.0
 * @ModifyTime 2020/9/5 08:51:02
 */
@SpringBootTest
public class RankingTest02_value_score {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void main() {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();

        //先清除缓存的key
        Boolean isKeyExist = redisTemplate.delete("ranking");
        System.out.println("isKeyExist:" + isKeyExist);

        //把数据存入缓存
        zSetOperations.incrementScore("ranking", 2, 2);
        zSetOperations.incrementScore("ranking", 1, 1);
        zSetOperations.incrementScore("ranking", 3, 3);
        zSetOperations.incrementScore("ranking", 8, 8);
        zSetOperations.incrementScore("ranking", 6, 6);

        //从缓存取数据
        Long size = zSetOperations.size("ranking");

        Set<Integer> set = zSetOperations.range("ranking", 0, 4);
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            System.out.println("next:" + next);
        }
        System.out.println("=====================================================");

        Set<ZSetOperations.TypedTuple> ranking = zSetOperations.rangeByScoreWithScores("ranking", 0, 4);

        for (ZSetOperations.TypedTuple t : ranking
        ) {
            System.out.println(t.getValue() + "===>" + t.getScore());
        }
    }
}
