package com.shai.thread;

public class SleepWaitDemo {

    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== sleep() 与 wait() 区别演示 ===\n");

        // --- 场景1: sleep 不释放锁 ---
        Thread sleepThread = new Thread(() -> {
            synchronized (lock) {
                System.out.println("[sleep线程] 获取锁，sleep 3秒...");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("[sleep线程] sleep 结束，释放锁");
            }
        }, "sleep-thread");
        sleepThread.start();

        Thread.sleep(200); // 确保 sleep 线程先拿到锁

        Thread tryLockDuringSleep = new Thread(() -> {
            System.out.println("[争锁线程] 尝试获取锁...");
            synchronized (lock) {
                System.out.println("[争锁线程] 拿到锁！（说明 sleep 不释放锁，等 sleep 结束才轮到）");
            }
        }, "try-lock-during-sleep");
        tryLockDuringSleep.start();

        sleepThread.join();
        tryLockDuringSleep.join();

        System.out.println();

        // --- 场景2: wait 释放锁 ---
        Thread waitThread = new Thread(() -> {
            synchronized (lock) {
                System.out.println("[wait线程] 获取锁，调用 wait()，释放锁进入等待...");
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("[wait线程] 被唤醒，重新获取锁，继续执行");
            }
        }, "wait-thread");
        waitThread.start();

        Thread.sleep(200); // 确保 wait 线程先拿到锁

        Thread tryLockDuringWait = new Thread(() -> {
            synchronized (lock) {
                System.out.println("[争锁线程] 拿到锁！（说明 wait 释放了锁）");
                System.out.println("[争锁线程] 调用 notify() 唤醒 wait 线程...");
                lock.notify();
            }
        }, "notify-thread");
        tryLockDuringWait.start();

        waitThread.join();
        tryLockDuringWait.join();

        System.out.println("\n=== 总结 ===");
        System.out.println("1. sleep() 是 Thread 的静态方法，不释放锁，抱着锁睡。");
        System.out.println("2. wait()  是 Object 的实例方法，必须持有锁才能调，调用后释放锁进入等待。");
        System.out.println("3. sleep() 到时间自动醒；wait() 需要 notify()/notifyAll() 唤醒。");
    }
}
