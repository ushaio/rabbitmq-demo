package com.shai.high;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 高并发场景下的线程安全解决方案演示
 * 展示使用synchronized关键字和Lock接口解决线程安全问题
 */
public class ThreadSafeDemo {
    private static int counter = 0;
    private static final Object lock = new Object();
    private static final ReentrantLock reentrantLock = new ReentrantLock();
    private static final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    // 使用synchronized关键字的解决方案
    public static void synchronizedCounter() {
        synchronized (lock) {
            counter++;
        }
    }

    // 使用ReentrantLock的解决方案
    public static void reentrantCounter() {
        reentrantLock.lock();
        try {
            counter++;
        } finally {
            reentrantLock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 重置计数器
        counter = 0;
        
        // 创建10个线程使用synchronized方法
        Thread[] threads = new Thread[10];
        
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    synchronizedCounter(); // 使用同步方法
                }
            });
            threads[i].start();
        }

        // 等待所有线程执行完毕
        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("使用synchronized后，计数值: " + counter);
        
        // 重置计数器并使用ReentrantLock
        counter = 0;
        
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    reentrantCounter(); // 使用Lock
                }
            });
            threads[i].start();
        }

        // 等待所有线程执行完毕
        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("使用ReentrantLock后，计数值: " + counter);
    }
}