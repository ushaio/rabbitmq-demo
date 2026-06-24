package com.shai.redis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 缓存实战示例
 * 模拟数据库查询缓存场景
 *
 * @Author: shai
 */
@Slf4j
@RestController
@RequestMapping("/cache")
@Tag(name = "缓存实战", description = "模拟数据库查询缓存、缓存过期等场景")
public class CacheController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 模拟数据库数据
     */
    private static final Map<String, String> FAKE_DB = new HashMap<>() {{
        put("1001", "张三");
        put("1002", "李四");
        put("1003", "王五");
    }};

    @Operation(summary = "查询用户 - 缓存优先，未命中则查库并回填缓存")
    @GetMapping("/user/{id}")
    public Map<String, Object> getUser(@PathVariable String id) {
        Map<String, Object> result = new HashMap<>();
        String cacheKey = "user:" + id;

        // 1. 先查缓存
        String cached = stringRedisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            log.info("缓存命中: {} = {}", cacheKey, cached);
            result.put("source", "cache");
            result.put("name", cached);
            return result;
        }

        // 2. 缓存未命中，查数据库
        log.info("缓存未命中，查询数据库: id={}", id);
        String name = FAKE_DB.get(id);
        if (name == null) {
            result.put("source", "db");
            result.put("name", null);
            result.put("message", "用户不存在");
            return result;
        }

        // 3. 回填缓存，设置 60 秒过期
        stringRedisTemplate.opsForValue().set(cacheKey, name, 60, TimeUnit.SECONDS);
        log.info("回填缓存: {} = {}, ttl=60s", cacheKey, name);

        result.put("source", "db");
        result.put("name", name);
        return result;
    }

    @Operation(summary = "清除用户缓存")
    @DeleteMapping("/user/{id}")
    public String evictUser(@PathVariable String id) {
        String cacheKey = "user:" + id;
        stringRedisTemplate.delete(cacheKey);
        log.info("缓存已清除: {}", cacheKey);
        return "缓存已清除: " + cacheKey;
    }

    @Operation(summary = "查看 key 的剩余 TTL（秒）")
    @GetMapping("/ttl")
    public Long ttl(@RequestParam String key) {
        Long ttl = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        log.info("TTL {} = {}s", key, ttl);
        return ttl;
    }
}
