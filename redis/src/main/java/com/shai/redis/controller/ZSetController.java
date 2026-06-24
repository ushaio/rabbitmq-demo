package com.shai.redis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Redis ZSet (有序集合) 类型操作示例
 *
 * @Author: shai
 */
@Slf4j
@RestController
@RequestMapping("/zset")
@Tag(name = "ZSet 类型", description = "Redis ZSet 有序集合操作")
public class ZSetController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Operation(summary = "ZADD - 添加成员及分数")
    @PostMapping("/zadd")
    public Boolean zadd(@RequestParam String key, @RequestParam String value, @RequestParam double score) {
        Boolean result = redisTemplate.opsForZSet().add(key, value, score);
        log.info("ZADD {} {} {} = {}", key, value, score, result);
        return result;
    }

    @Operation(summary = "ZSCORE - 获取成员分数")
    @GetMapping("/zscore")
    public Double zscore(@RequestParam String key, @RequestParam String value) {
        Double result = redisTemplate.opsForZSet().score(key, value);
        log.info("ZSCORE {} {} = {}", key, value, result);
        return result;
    }

    @Operation(summary = "ZRANK - 获取成员排名（升序）")
    @GetMapping("/zrank")
    public Long zrank(@RequestParam String key, @RequestParam String value) {
        Long result = redisTemplate.opsForZSet().rank(key, value);
        log.info("ZRANK {} {} = {}", key, value, result);
        return result;
    }

    @Operation(summary = "ZRANGE - 按排名范围获取成员（升序）")
    @GetMapping("/zrange")
    public Set<Object> zrange(@RequestParam String key,
                              @RequestParam(defaultValue = "0") long start,
                              @RequestParam(defaultValue = "-1") long end) {
        Set<Object> result = redisTemplate.opsForZSet().range(key, start, end);
        log.info("ZRANGE {} {} {} = {}", key, start, end, result);
        return result;
    }

    @Operation(summary = "ZRANGEBYSCORE - 按分数范围获取成员")
    @GetMapping("/zrangebyscore")
    public Set<Object> zrangebyscore(@RequestParam String key,
                                     @RequestParam double min,
                                     @RequestParam double max) {
        Set<Object> result = redisTemplate.opsForZSet().rangeByScore(key, min, max);
        log.info("ZRANGEBYSCORE {} {} {} = {}", key, min, max, result);
        return result;
    }

    @Operation(summary = "ZREVRANGE - 按排名范围获取成员（降序）")
    @GetMapping("/zrevrange")
    public Set<Object> zrevrange(@RequestParam String key,
                                 @RequestParam(defaultValue = "0") long start,
                                 @RequestParam(defaultValue = "-1") long end) {
        Set<Object> result = redisTemplate.opsForZSet().reverseRange(key, start, end);
        log.info("ZREVRANGE {} {} {} = {}", key, start, end, result);
        return result;
    }

    @Operation(summary = "ZCARD - 获取有序集合大小")
    @GetMapping("/zcard")
    public Long zcard(@RequestParam String key) {
        Long result = redisTemplate.opsForZSet().zCard(key);
        log.info("ZCARD {} = {}", key, result);
        return result;
    }

    @Operation(summary = "ZREM - 移除成员")
    @DeleteMapping("/zrem")
    public Long zrem(@RequestParam String key, @RequestParam String value) {
        Long result = redisTemplate.opsForZSet().remove(key, value);
        log.info("ZREM {} {} = {}", key, value, result);
        return result;
    }

    @Operation(summary = "ZINCRBY - 增加成员分数")
    @PostMapping("/zincrby")
    public Double zincrby(@RequestParam String key, @RequestParam String value, @RequestParam double delta) {
        Double result = redisTemplate.opsForZSet().incrementScore(key, value, delta);
        log.info("ZINCRBY {} {} {} = {}", key, value, delta, result);
        return result;
    }
}
