package com.shai.high;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 高并发场景下的性能优化综合演示
 * 展示多种并发优化策略和性能对比
 */
public class PerformanceOptimizationDemo {
    
    // 使用原子变量统计请求数
    private static final AtomicLong requestCount = new AtomicLong(0);
    private static final AtomicLong errorCount = new AtomicLong(0);
    
    // 模拟数据库连接池
    private static final Semaphore dbConnectionPool = new Semaphore(5);
    
    // 使用ConcurrentHashMap替代普通HashMap
    private static final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 高并发性能优化综合演示 ===\n");

        // 1. 基础性能测试
        basicPerformanceTest();
        
        // 2. 连接池限制演示
        connectionPoolDemo();
        
        // 3. 缓存优化演示
        cacheOptimizationDemo();
        
        // 4. 并发集合使用演示
        concurrentCollectionDemo();
    }
    
    /**
     * 基础性能测试 - 对比不同同步机制的性能
     */
    private static void basicPerformanceTest() throws InterruptedException {
        System.out.println("--- 基础性能测试 ---");
        
        // 测试无同步机制的性能（但结果不正确）
        long startTime = System.currentTimeMillis();
        testWithoutSync();
        long endTime = System.currentTimeMillis();
        System.out.println("无同步机制耗时: " + (endTime - startTime) + "ms, 最终计数: " + requestCount.get());
        
        // 重置计数器
        requestCount.set(0);
        
        // 测试使用synchronized的性能
        startTime = System.currentTimeMillis();
        testWithSynchronized();
        endTime = System.currentTimeMillis();
        System.out.println("synchronized同步耗时: " + (endTime - startTime) + "ms, 最终计数: " + requestCount.get());
        
        // 重置计数器
        requestCount.set(0);
        
        // 测试使用原子变量的性能
        startTime = System.currentTimeMillis();
        testWithAtomic();
        endTime = System.currentTimeMillis();
        System.out.println("原子变量同步耗时: " + (endTime - startTime) + "ms, 最终计数: " + requestCount.get());
        
        System.out.println();
    }
    
    private static void testWithoutSync() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10000; i++) {
            executor.submit(() -> {
                requestCount.incrementAndGet(); // 无同步，性能最快但结果不准确
            });
        }
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }
    
    private static void testWithSynchronized() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10000; i++) {
            executor.submit(() -> {
                synchronized (PerformanceOptimizationDemo.class) {
                    requestCount.incrementAndGet(); // 使用同步，性能较慢但结果准确
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }
    
    private static void testWithAtomic() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10000; i++) {
            executor.submit(() -> {
                requestCount.incrementAndGet(); // 原子操作，性能和准确性兼顾
            });
        }
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }
    
    /**
     * 连接池限制演示 - 使用信号量控制资源访问
     */
    private static void connectionPoolDemo() throws InterruptedException {
        System.out.println("--- 连接池限制演示 ---");
        
        ExecutorService executor = Executors.newFixedThreadPool(20);
        CountDownLatch latch = new CountDownLatch(20);
        
        for (int i = 0; i < 20; i++) {
            final int clientId = i;
            executor.submit(() -> {
                try {
                    // 尝试获取数据库连接
                    if (dbConnectionPool.tryAcquire(1, TimeUnit.SECONDS)) {
                        try {
                            // 模拟数据库操作
                            System.out.println("客户端 " + clientId + " 获取数据库连接成功");
                            Thread.sleep(500); // 模拟数据库操作时间
                        } finally {
                            dbConnectionPool.release(); // 释放连接
                            System.out.println("客户端 " + clientId + " 释放数据库连接");
                        }
                    } else {
                        System.out.println("客户端 " + clientId + " 获取数据库连接失败，连接池已满");
                        errorCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        System.out.println("总错误数: " + errorCount.get());
        System.out.println("可用连接数: " + dbConnectionPool.availablePermits());
        System.out.println();
        
        executor.shutdown();
    }
    
    /**
     * 缓存优化演示 - 使用ConcurrentHashMap进行高效缓存
     */
    private static void cacheOptimizationDemo() throws InterruptedException {
        System.out.println("--- 缓存优化演示 ---");
        
        // 预热缓存
        for (int i = 0; i < 100; i++) {
            cache.put("key" + i, "value" + i);
        }
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(1000);
        
        long startTime = System.currentTimeMillis();
        
        // 多线程并发访问缓存
        for (int i = 0; i < 1000; i++) {
            final int keyIndex = i % 100;
            executor.submit(() -> {
                String key = "key" + keyIndex;
                String value = cache.get(key);
                
                // 模拟缓存未命中时的加载
                if (value == null) {
                    cache.put(key, "computed_value_" + keyIndex);
                }
                
                // 模拟写操作
                if (Math.random() > 0.8) { // 20%的概率更新缓存
                    cache.put(key, "updated_value_" + keyIndex);
                }
                
                latch.countDown();
            });
        }
        
        latch.await();
        long endTime = System.currentTimeMillis();
        
        System.out.println("缓存操作耗时: " + (endTime - startTime) + "ms");
        System.out.println("缓存大小: " + cache.size());
        System.out.println();
        
        executor.shutdown();
    }
    
    /**
     * 并发集合使用演示
     */
    private static void concurrentCollectionDemo() throws InterruptedException {
        System.out.println("--- 并发集合使用演示 ---");
        
        // 使用阻塞队列实现生产者-消费者模式
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(10);
        
        // 启动生产者
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    String item = "item-" + i;
                    queue.put(item); // 阻塞式添加
                    System.out.println("生产: " + item + ", 队列大小: " + queue.size());
                    Thread.sleep(50); // 模拟生产时间
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        
        // 启动消费者
        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    String item = queue.take(); // 阻塞式获取
                    System.out.println("消费: " + item + ", 队列大小: " + queue.size());
                    Thread.sleep(100); // 模拟消费时间
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        
        producer.start();
        consumer.start();
        
        producer.join();
        consumer.join();
        
        System.out.println("队列最终大小: " + queue.size());
        System.out.println();
    }
}