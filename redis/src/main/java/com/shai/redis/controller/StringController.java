package com.shai.redis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * Redis String 类型操作示例
 *
 * @Author: shai
 */
@Slf4j
@RestController
@RequestMapping("/string")
@Tag(name = "String 类型", description = "Redis String 数据类型操作")
public class StringController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Operation(summary = "SET - 设置键值对")
    @PostMapping("/set")
    public String set(@RequestParam String key, @RequestParam String value) {
        stringRedisTemplate.opsForValue().set(key, value);
        log.info("SET {} = {}", key, value);
        return "OK";
    }

    @Operation(summary = "GET - 获取值")
    @GetMapping("/get")
    public String get(@RequestParam String key) {
        String value = stringRedisTemplate.opsForValue().get(key);
        log.info("GET {} = {}", key, value);
        return value;
    }

    @Operation(summary = "SETEX - 设置键值对并指定过期时间（秒）")
    @PostMapping("/setex")
    public String setex(@RequestParam String key, @RequestParam String value, @RequestParam long seconds) {
        stringRedisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
        log.info("SETEX {} = {}, ttl={}s", key, value, seconds);
        return "OK";
    }

    @Operation(summary = "SETNX - 仅当 key 不存在时设置")
    @PostMapping("/setnx")
    public Boolean setnx(@RequestParam String key, @RequestParam String value) {
        Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(key, value);
        log.info("SETNX {} = {}, result={}", key, value, result);
        return result;
    }

    @Operation(summary = "INCR - 自增 1")
    @PostMapping("/incr")
    public Long incr(@RequestParam String key) {
        Long result = stringRedisTemplate.opsForValue().increment(key);
        log.info("INCR {} = {}", key, result);
        return result;
    }

    @Operation(summary = "DECR - 自减 1")
    @PostMapping("/decr")
    public Long decr(@RequestParam String key) {
        Long result = stringRedisTemplate.opsForValue().decrement(key);
        log.info("DECR {} = {}", key, result);
        return result;
    }

    @Operation(summary = "DEL - 删除键")
    @DeleteMapping("/del")
    public Boolean del(@RequestParam String key) {
        Boolean result = stringRedisTemplate.delete(key);
        log.info("DEL {} = {}", key, result);
        return result;
    }
}
