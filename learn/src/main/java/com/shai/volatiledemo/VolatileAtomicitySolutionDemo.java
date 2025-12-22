package com.shai.volatiledemo;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 演示如何解决 volatile 不保证原子性的问题
 * 使用 AtomicInteger 来替代 volatile，保证复合操作的原子性
 */
public class VolatileAtomicitySolutionDemo {
    private static AtomicInteger atomicCount = new AtomicInteger(0);

    public static void increment() {
        // 使用 AtomicInteger 的原子递增方法
        atomicCount.incrementAndGet();
    }

    public static void main(String[] args) throws InterruptedException {
        int threadsCount = 10;
        int iterations = 1000;
        Thread[] threads = new Thread[threadsCount];

        /**
         * 创建10个线程，每个线程执行 1000 次 count++ 操作
         */
        for (int i = 0; i < threadsCount; i++) {
            // 创建线程
            threads[i] = new Thread(() -> {
                for (int j = 0; j < iterations; j++) {
                    increment();
                }
            });
            // 启动线程
            threads[i].start();
        }

        // 等待所有线程执行完毕
        for (Thread t : threads) {
            t.join();
        }

        // 预期结果应该是 threadsCount * iterations (10000)
        // 使用 AtomicInteger 后，结果应该总是正确的
        System.out.println("最终 atomicCount 值: " + atomicCount.get());
        if (atomicCount.get() == threadsCount * iterations) {
            System.out.println("结果证明 AtomicInteger 保证了原子性。");
        } else {
            System.out.println("出现了意外情况，请重新运行。");
        }
    }
}