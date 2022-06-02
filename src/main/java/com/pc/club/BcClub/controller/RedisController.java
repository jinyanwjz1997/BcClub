package com.pc.club.BcClub.controller;

import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description RedisController
 * @Author jyWei
 * @Version V1.0.0
 * @Created by jinYan
 * @Date 2021/7/12
 */
@RestController
@Slf4j
@RequestMapping("/pc/club/bc_club/redis/")
public class RedisController {
//    @Resource
//    private StringRedisTemplate stringRedisTemplate; 
//
//    @Resource
//    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("setCache")
    public void setCache(String key, String value) {
        log.info(String.format("redis key【%s】value【%s】", key, value));
//        redisTemplate.opsForValue().set(key,value);
//        stringRedisTemplate.opsForValue().set(key,value);
    }
}
