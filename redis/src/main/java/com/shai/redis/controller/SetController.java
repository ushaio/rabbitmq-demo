package com.shai.redis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Redis Set 类型操作示例
 *
 * @Author: shai
 */
@Slf4j
@RestController
@RequestMapping("/set")
@Tag(name = "Set 类型", description = "Redis Set 数据类型操作")
public class SetController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Operation(summary = "SADD - 添加成员")
    @PostMapping("/sadd")
    public Long sadd(@RequestParam String key, @RequestParam String value) {
        Long result = redisTemplate.opsForSet().add(key, value);
        log.info("SADD {} {} = {}", key, value, result);
        return result;
    }

    @Operation(summary = "SMEMBERS - 获取所有成员")
    @GetMapping("/smembers")
    public Set<Object> smembers(@RequestParam String key) {
        Set<Object> result = redisTemplate.opsForSet().members(key);
        log.info("SMEMBERS {} = {}", key, result);
        return result;
    }

    @Operation(summary = "SISMEMBER - 判断是否为成员")
    @GetMapping("/sismember")
    public Boolean sismember(@RequestParam String key, @RequestParam String value) {
        Boolean result = redisTemplate.opsForSet().isMember(key, value);
        log.info("SISMEMBER {} {} = {}", key, value, result);
        return result;
    }

    @Operation(summary = "SCARD - 获取集合大小")
    @GetMapping("/scard")
    public Long scard(@RequestParam String key) {
        Long result = redisTemplate.opsForSet().size(key);
        log.info("SCARD {} = {}", key, result);
        return result;
    }

    @Operation(summary = "SREM - 移除成员")
    @DeleteMapping("/srem")
    public Long srem(@RequestParam String key, @RequestParam String value) {
        Long result = redisTemplate.opsForSet().remove(key, value);
        log.info("SREM {} {} = {}", key, value, result);
        return result;
    }

    @Operation(summary = "SINTER - 交集")
    @GetMapping("/sinter")
    public Set<Object> sinter(@RequestParam String key1, @RequestParam String key2) {
        Set<Object> result = redisTemplate.opsForSet().intersect(key1, key2);
        log.info("SINTER {} {} = {}", key1, key2, result);
        return result;
    }

    @Operation(summary = "SUNION - 并集")
    @GetMapping("/sunion")
    public Set<Object> sunion(@RequestParam String key1, @RequestParam String key2) {
        Set<Object> result = redisTemplate.opsForSet().union(key1, key2);
        log.info("SUNION {} {} = {}", key1, key2, result);
        return result;
    }

    @Operation(summary = "SDIFF - 差集")
    @GetMapping("/sdiff")
    public Set<Object> sdiff(@RequestParam String key1, @RequestParam String key2) {
        Set<Object> result = redisTemplate.opsForSet().difference(key1, key2);
        log.info("SDIFF {} {} = {}", key1, key2, result);
        return result;
    }
}
