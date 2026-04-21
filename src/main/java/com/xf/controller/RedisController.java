package com.xf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisController
 * @Description TODO
 * @Author tanjunjie
 * @Date 2024/4/19 17:47
 * @Version 1.0
 */
@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/test")
    public void test01(){
        // 设置k v
        redisTemplate.opsForValue().set("name", "小明");
        // 拿到key为name的值
        String city = (String) redisTemplate.opsForValue().get("name"); 
        System.out.println(city);
        // 设置过期时间为三分钟
        redisTemplate.opsForValue().set("code", "1234", 3, TimeUnit.MINUTES);
        // 设置lock为k的唯一值
        redisTemplate.opsForValue().setIfAbsent("lock", "1"); 
        redisTemplate.opsForValue().setIfAbsent("lock", "2");
    }

    @GetMapping("/testList")
    public void testList(){
        redisTemplate.opsForList().rightPush("personIdCards:1", "101010100101010101");
        //缓存十分钟
//        redisTemplate.expire("personIdCards", 1000 * 60 * 10L, TimeUnit.MILLISECONDS);
        List<String> result = redisTemplate.opsForList().range("personIdCards", 0, -1);
        System.out.println(result);
    }

}
