package com.shai.high;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 高并发场景下的原子操作演示
 * 展示使用AtomicInteger等原子类解决线程安全问题
 */
public class AtomicDemo {
    private static AtomicInteger atomicCounter = new AtomicInteger(0);
    private static int regularCounter = 0;
    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        // 演示非原子操作的问题
        regularCounter = 0;
        Thread[] regularThreads = new Thread[10];
        
        for (int i = 0; i < 10; i++) {
            regularThreads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    synchronized (lock) {
                        regularCounter++;
                    }
                }
            });
            regularThreads[i].start();
        }

        for (Thread thread : regularThreads) {
            thread.join();
        }

        System.out.println("使用synchronized的普通int计数器: " + regularCounter);
        
        // 演示原子操作的优势
        atomicCounter = new AtomicInteger(0);
        Thread[] atomicThreads = new Thread[10];
        
        for (int i = 0; i < 10; i++) {
            atomicThreads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    atomicCounter.incrementAndGet(); // 原子操作
                }
            });
            atomicThreads[i].start();
        }

        for (Thread thread : atomicThreads) {
            thread.join();
        }

        System.out.println("使用AtomicInteger的原子计数器: " + atomicCounter.get());
        
        // 性能比较
        performanceComparison();
    }

    private static void performanceComparison() throws InterruptedException {
        // 原子操作性能测试
        long startTime = System.currentTimeMillis();
        Thread[] atomicThreads = new Thread[10];
        atomicCounter = new AtomicInteger(0);
        
        for (int i = 0; i < 10; i++) {
            atomicThreads[i] = new Thread(() -> {
                for (int j = 0; j < 100000; j++) {
                    atomicCounter.incrementAndGet();
                }
            });
            atomicThreads[i].start();
        }

        for (Thread thread : atomicThreads) {
            thread.join();
        }
        long atomicTime = System.currentTimeMillis() - startTime;

        // synchronized性能测试
        startTime = System.currentTimeMillis();
        Thread[] syncThreads = new Thread[10];
        regularCounter = 0;
        
        for (int i = 0; i < 10; i++) {
            syncThreads[i] = new Thread(() -> {
                for (int j = 0; j < 100000; j++) {
                    synchronized (lock) {
                        regularCounter++;
                    }
                }
            });
            syncThreads[i].start();
        }

        for (Thread thread : syncThreads) {
            thread.join();
        }
        long syncTime = System.currentTimeMillis() - startTime;

        System.out.println("\n性能比较 (100万次操作):");
        System.out.println("原子操作耗时: " + atomicTime + "ms");
        System.out.println("同步块耗时: " + syncTime + "ms");
    }
}