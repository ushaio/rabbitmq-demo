package com.shai.redis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Redis List 类型操作示例
 *
 * @Author: shai
 */
@Slf4j
@RestController
@RequestMapping("/list")
@Tag(name = "List 类型", description = "Redis List 数据类型操作")
public class ListController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Operation(summary = "LPUSH - 从左侧插入元素")
    @PostMapping("/lpush")
    public Long lpush(@RequestParam String key, @RequestParam String value) {
        Long result = redisTemplate.opsForList().leftPush(key, value);
        log.info("LPUSH {} {}, size={}", key, value, result);
        return result;
    }

    @Operation(summary = "RPUSH - 从右侧插入元素")
    @PostMapping("/rpush")
    public Long rpush(@RequestParam String key, @RequestParam String value) {
        Long result = redisTemplate.opsForList().rightPush(key, value);
        log.info("RPUSH {} {}, size={}", key, value, result);
        return result;
    }

    @Operation(summary = "LPOP - 从左侧弹出元素")
    @PostMapping("/lpop")
    public Object lpop(@RequestParam String key) {
        Object result = redisTemplate.opsForList().leftPop(key);
        log.info("LPOP {} = {}", key, result);
        return result;
    }

    @Operation(summary = "RPOP - 从右侧弹出元素")
    @PostMapping("/rpop")
    public Object rpop(@RequestParam String key) {
        Object result = redisTemplate.opsForList().rightPop(key);
        log.info("RPOP {} = {}", key, result);
        return result;
    }

    @Operation(summary = "LRANGE - 获取指定范围的元素")
    @GetMapping("/lrange")
    public List<Object> lrange(@RequestParam String key,
                               @RequestParam(defaultValue = "0") long start,
                               @RequestParam(defaultValue = "-1") long end) {
        List<Object> result = redisTemplate.opsForList().range(key, start, end);
        log.info("LRANGE {} {} {} = {}", key, start, end, result);
        return result;
    }

    @Operation(summary = "LLEN - 获取列表长度")
    @GetMapping("/llen")
    public Long llen(@RequestParam String key) {
        Long result = redisTemplate.opsForList().size(key);
        log.info("LLEN {} = {}", key, result);
        return result;
    }
}
