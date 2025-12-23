package com.shai.high;

/**
 * 高并发场景下的线程安全问题演示
 * 展示非线程安全计数器在多线程环境下的问题
 */
public class ThreadUnsafeDemo {
    private static int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        // 创建10个线程同时对counter进行累加操作
        Thread[] threads = new Thread[10];
        
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter++; // 非原子操作，存在竞态条件
                }
            });
            threads[i].start();
        }

        // 等待所有线程执行完毕
        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("期望值: 10000, 实际值: " + counter);
        System.out.println("由于线程安全问题，实际值通常小于期望值");
    }
}