package com.shai.high;

import java.util.concurrent.*;

/**
 * 高并发场景下的线程池使用演示
 * 展示不同类型的线程池及其适用场景
 */
public class ThreadPoolDemo {
    
    public static void main(String[] args) {
        System.out.println("=== 高并发场景下的线程池使用演示 ===\n");

        // 1. FixedThreadPool - 固定大小线程池
        fixedThreadPoolDemo();
        
        // 2. CachedThreadPool - 可缓存线程池
        cachedThreadPoolDemo();
        
        // 3. ScheduledThreadPool - 定时任务线程池
        scheduledThreadPoolDemo();
        
        // 4. 自定义线程池 - 更精确的控制
        customThreadPoolDemo();
    }

    /**
     * 固定大小线程池演示
     * 适用于负载较重、长期运行的服务
     */
    private static void fixedThreadPoolDemo() {
        System.out.println("--- FixedThreadPool 演示 ---");
        ExecutorService fixedPool = Executors.newFixedThreadPool(3);
        
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            fixedPool.submit(() -> {
                System.out.println("FixedPool - 任务 " + taskId + 
                    " 执行线程: " + Thread.currentThread().getName());
                try {
                    Thread.sleep(2000); // 模拟任务执行
                    System.out.println("FixedPool - 任务 " + taskId + " 完成");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        fixedPool.shutdown();
        try {
            fixedPool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println();
    }

    /**
     * 可缓存线程池演示
     * 适用于执行很多短期异步任务
     */
    private static void cachedThreadPoolDemo() {
        System.out.println("--- CachedThreadPool 演示 ---");
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            cachedPool.submit(() -> {
                System.out.println("CachedPool - 任务 " + taskId + 
                    " 执行线程: " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000); // 模拟任务执行
                    System.out.println("CachedPool - 任务 " + taskId + " 完成");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        cachedPool.shutdown();
        try {
            cachedPool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println();
    }

    /**
     * 定时任务线程池演示
     * 适用于需要定时或周期性执行任务的场景
     */
    private static void scheduledThreadPoolDemo() {
        System.out.println("--- ScheduledThreadPool 演示 ---");
        ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(2);
        
        // 延迟执行任务
        scheduledPool.schedule(() -> {
            System.out.println("ScheduledPool - 延迟任务执行，线程: " + 
                Thread.currentThread().getName());
        }, 2, TimeUnit.SECONDS);
        
        // 周期性执行任务
        ScheduledFuture<?> future = scheduledPool.scheduleAtFixedRate(() -> {
            System.out.println("ScheduledPool - 周期任务执行，线程: " + 
                Thread.currentThread().getName());
        }, 0, 3, TimeUnit.SECONDS);
        
        // 10秒后取消周期任务
        scheduledPool.schedule(() -> {
            future.cancel(false);
            System.out.println("ScheduledPool - 周期任务已取消");
        }, 10, TimeUnit.SECONDS);
        
        scheduledPool.shutdown();
        try {
            scheduledPool.awaitTermination(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println();
    }

    /**
     * 自定义线程池演示
     * 提供更精确的控制，如队列大小、拒绝策略等
     */
    private static void customThreadPoolDemo() {
        System.out.println("--- Custom ThreadPool 演示 ---");
        
        // 创建自定义线程池
        ThreadPoolExecutor customPool = new ThreadPoolExecutor(
            2,                      // 核心线程数
            4,                      // 最大线程数
            60L,                    // 空闲线程存活时间
            TimeUnit.SECONDS,       // 时间单位
            new LinkedBlockingQueue<>(5), // 工作队列
            new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略
        );
        
        // 提交多个任务以观察线程池行为
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            try {
                customPool.submit(() -> {
                    System.out.println("CustomPool - 任务 " + taskId + 
                        " 执行线程: " + Thread.currentThread().getName() + 
                        ", 活跃线程数: " + customPool.getActiveCount() + 
                        ", 队列大小: " + customPool.getQueue().size());
                    try {
                        Thread.sleep(3000); // 模拟任务执行
                        System.out.println("CustomPool - 任务 " + taskId + " 完成");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            } catch (Exception e) {
                System.out.println("任务 " + taskId + " 被拒绝: " + e.getMessage());
            }
        }
        
        customPool.shutdown();
        try {
            customPool.awaitTermination(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println();
    }
}