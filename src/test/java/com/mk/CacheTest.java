package com.mk;

import com.mk.config.Code;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.UUID;

@Slf4j
@SpringBootTest
public class CacheTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test1(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("name", "tom");
        valueOperations.set("description", "Hello World!!!");
        log.info(valueOperations.get("name").toString());
        System.out.println(valueOperations.get("*"));
    }

    @Test
    public void test2(){
        for (int i = 0; i < 10; i++) {
            System.out.println(Code.getCode(4));
        }
    }


}
