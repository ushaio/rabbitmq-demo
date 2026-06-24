package com.shai.redis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import HashMap;
import Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis 分布式锁示例
 * 基于 SETNX 实现简易分布式锁
 *
 * @Author: shai
 */
@Slf4j
@RestController
@RequestMapping("/lock")
@Tag(name = "分布式锁", description = "基于 Redis SETNX 的简易分布式锁示例")
public class LockController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Operation(summary = "尝试获取锁")
    @PostMapping("/acquire")
    public Map<String, Object> acquireLock(@RequestParam(defaultValue = "my_lock") String lockKey,
                                           @RequestParam(defaultValue = "10") long expireSeconds) {
        String requestId = Thread.currentThread().getName() + "-" + System.currentTimeMillis();
        Boolean acquired = stringRedisTemplate.opsForValue()
                .setIfAbsent(lockKey, requestId, expireSeconds, TimeUnit.SECONDS);

        Map<String, Object> result = new HashMap<>();
        result.put("lockKey", lockKey);
        result.put("requestId", requestId);
        result.put("acquired", Boolean.TRUE.equals(acquired));
        result.put("expireSeconds", expireSeconds);

        if (Boolean.TRUE.equals(acquired)) {
            log.info("获取锁成功: key={}, requestId={}, expire={}s", lockKey, requestId, expireSeconds);
        } else {
            log.info("获取锁失败（锁已被持有）: key={}", lockKey);
        }
        return result;
    }

    @Operation(summary = "释放锁（仅持有者可释放）")
    @PostMapping("/release")
    public Map<String, Object> releaseLock(@RequestParam(defaultValue = "my_lock") String lockKey,
                                           @RequestParam String requestId) {
        Map<String, Object> result = new HashMap<>();
        result.put("lockKey", lockKey);
        result.put("requestId", requestId);

        // 检查是否为锁的持有者
        String currentHolder = stringRedisTemplate.opsForValue().get(lockKey);
        if (requestId.equals(currentHolder)) {
            stringRedisTemplate.delete(lockKey);
            result.put("released", true);
            log.info("释放锁成功: key={}, requestId={}", lockKey, requestId);
        } else {
            result.put("released", false);
            result.put("message", "非锁持有者，无法释放");
            log.warn("释放锁失败（非持有者）: key={}, requestId={}, currentHolder={}", lockKey, requestId, currentHolder);
        }
        return result;
    }

    @Operation(summary = "模拟业务场景 - 扣减库存（带锁保护）")
    @PostMapping("/deduct-stock")
    public Map<String, Object> deductStock(@RequestParam(defaultValue = "product:1001") String stockKey) {
        String lockKey = "lock:" + stockKey;
        String requestId = Thread.currentThread().getName() + "-" + System.currentTimeMillis();
        Map<String, Object> result = new HashMap<>();

        // 1. 尝试获取锁
        Boolean acquired = stringRedisTemplate.opsForValue()
                .setIfAbsent(lockKey, requestId, 10, TimeUnit.SECONDS);

        if (!Boolean.TRUE.equals(acquired)) {
            result.put("success", false);
            result.put("message", "系统繁忙，请稍后重试（获取锁失败）");
            return result;
        }

        try {
            // 2. 查询库存
            String stockStr = stringRedisTemplate.opsForValue().get(stockKey);
            int stock = stockStr == null ? 0 : Integer.parseInt(stockStr);

            if (stock <= 0) {
                result.put("success", false);
                result.put("message", "库存不足");
                result.put("stock", stock);
                return result;
            }

            // 3. 扣减库存
            stock--;
            stringRedisTemplate.opsForValue().set(stockKey, String.valueOf(stock));
            log.info("扣减库存成功: {} = {}", stockKey, stock);

            result.put("success", true);
            result.put("message", "扣减成功");
            result.put("stock", stock);
        } finally {
            // 4. 释放锁
            if (requestId.equals(stringRedisTemplate.opsForValue().get(lockKey))) {
                stringRedisTemplate.delete(lockKey);
            }
        }
        return result;
    }

    @Operation(summary = "初始化库存（用于测试）")
    @PostMapping("/init-stock")
    public String initStock(@RequestParam(defaultValue = "product:1001") String stockKey,
                            @RequestParam(defaultValue = "100") int amount) {
        stringRedisTemplate.opsForValue().set(stockKey, String.valueOf(amount));
        log.info("初始化库存: {} = {}", stockKey, amount);
        return "库存已初始化: " + stockKey + " = " + amount;
    }
}
