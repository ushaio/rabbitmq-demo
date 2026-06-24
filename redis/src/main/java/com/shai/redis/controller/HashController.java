package com.shai.redis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Redis Hash 类型操作示例
 *
 * @Author: shai
 */
@Slf4j
@RestController
@RequestMapping("/hash")
@Tag(name = "Hash 类型", description = "Redis Hash 数据类型操作")
public class HashController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Operation(summary = "HSET - 设置哈希字段")
    @PostMapping("/hset")
    public String hset(@RequestParam String key, @RequestParam String field, @RequestParam String value) {
        redisTemplate.opsForHash().put(key, field, value);
        log.info("HSET {} {} = {}", key, field, value);
        return "OK";
    }

    @Operation(summary = "HGET - 获取哈希字段值")
    @GetMapping("/hget")
    public Object hget(@RequestParam String key, @RequestParam String field) {
        Object result = redisTemplate.opsForHash().get(key, field);
        log.info("HGET {} {} = {}", key, field, result);
        return result;
    }

    @Operation(summary = "HGETALL - 获取所有字段和值")
    @GetMapping("/hgetall")
    public Map<Object, Object> hgetall(@RequestParam String key) {
        Map<Object, Object> result = redisTemplate.opsForHash().entries(key);
        log.info("HGETALL {} = {}", key, result);
        return result;
    }

    @Operation(summary = "HDEL - 删除哈希字段")
    @DeleteMapping("/hdel")
    public Long hdel(@RequestParam String key, @RequestParam String field) {
        Long result = redisTemplate.opsForHash().delete(key, field);
        log.info("HDEL {} {} = {}", key, field, result);
        return result;
    }

    @Operation(summary = "HEXISTS - 判断字段是否存在")
    @GetMapping("/hexists")
    public Boolean hexists(@RequestParam String key, @RequestParam String field) {
        Boolean result = redisTemplate.opsForHash().hasKey(key, field);
        log.info("HEXISTS {} {} = {}", key, field, result);
        return result;
    }

    @Operation(summary = "HLEN - 获取哈希字段数量")
    @GetMapping("/hlen")
    public Long hlen(@RequestParam String key) {
        Long result = redisTemplate.opsForHash().size(key);
        log.info("HLEN {} = {}", key, result);
        return result;
    }
}
