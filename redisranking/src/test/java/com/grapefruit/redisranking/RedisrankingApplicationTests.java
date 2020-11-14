package com.grapefruit.redisranking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.*;

@SpringBootTest
class RedisrankingApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();

        //把数据存入缓存
        zSetOperations.incrementScore("ranking", 789, 2);
        zSetOperations.incrementScore("ranking", 456, 1);
        zSetOperations.incrementScore("ranking", 123, 3);

        //从缓存取数据
        Long ranking = zSetOperations.size("ranking");

        System.out.println("ranking:" + ranking);

        Set<ZSetOperations.TypedTuple<Object>> typedTuples = zSetOperations.reverseRangeWithScores("ranking", 0, 2);
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = typedTuples.iterator();
        List<User> list = new ArrayList<>();
        while (iterator.hasNext()) {
            ZSetOperations.TypedTuple<Object> typedTuple = iterator.next();
            Object value = typedTuple.getValue();
            Double score = typedTuple.getScore();
            User user = new User(value.toString(), score);
            list.add(user);
        }

        for (User user : list) {
            System.out.println("==>" + user);
        }

        System.out.println("=======================");
        Collections.reverse(list);
        for (User user : list) {
            System.out.println("==>" + user);
        }
    }
}

@Data
@NoArgsConstructor
class User {
    String phone;
    Double money;

    public User(String phone, Double money) {
        this.phone = phone;
        this.money = money;
    }
}

