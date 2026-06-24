package com.shai.thread;

public class SynchronizedDemo {

    private int count = 0;

    // 1. synchronized 修饰实例方法 —— 锁的是 this（当前实例）
    public synchronized void increment() {
        count++;
    }

    // 等价于
    public void incrementBlock() {
        synchronized (this) {
            count++;
        }
    }

    // 2. synchronized 修饰静态方法 —— 锁的是 类对象 (SynchronizedDemo.class)
    public static synchronized void staticMethod() {
        System.out.println("[" + Thread.currentThread().getName() + "] 进入静态同步方法");
        try { Thread.sleep(1000); } catch (InterruptedException e) { }
        System.out.println("[" + Thread.currentThread().getName() + "] 离开静态同步方法");
    }

    // 多线程不安全的计数器演示
    private int unsafeCount = 0;
    public void unsafeIncrement() {
        unsafeCount++;
    }

    private int safeCount = 0;
    public synchronized void safeIncrement() {
        safeCount++;
    }

    public static void main(String[] args) throws InterruptedException {
        // ========== 场景1：不加 synchronized 的后果 ==========
        System.out.println("=== 1. 多线程不安全计数器（无synchronized）===");
        SynchronizedDemo demo1 = new SynchronizedDemo();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) demo1.unsafeIncrement();
        }, "unsafe-1");
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) demo1.unsafeIncrement();
        }, "unsafe-2");
        t1.start(); t2.start();
        t1.join(); t2.join();
        System.out.println("期望 200000，实际 " + demo1.unsafeCount + "（结果不一致 -> 丢失了更新）\n");

        // ========== 场景2：加了 synchronized 之后 ==========
        System.out.println("=== 2. 线程安全计数器（有synchronized）===");
        SynchronizedDemo demo2 = new SynchronizedDemo();
        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) demo2.safeIncrement();
        }, "safe-1");
        Thread t4 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) demo2.safeIncrement();
        }, "safe-2");
        t3.start(); t4.start();
        t3.join(); t4.join();
        System.out.println("期望 200000，实际 " + demo2.safeCount + "（完全一致）\n");

        // ========== 场景3：不同实例不互斥（实例锁互不影响）==========
        System.out.println("=== 3. 不同对象上的实例锁互不影响 ===");
        SynchronizedDemo objA = new SynchronizedDemo();
        SynchronizedDemo objB = new SynchronizedDemo();

        Thread lockA = new Thread(() -> {
            synchronized (objA) {
                System.out.println("[A线程] 持有 objA 锁，sleep 2秒");
                try { Thread.sleep(2000); } catch (InterruptedException e) { }
                System.out.println("[A线程] 释放 objA 锁");
            }
        }, "lock-A");

        Thread lockB = new Thread(() -> {
            try { Thread.sleep(100); } catch (InterruptedException e) { }
            synchronized (objB) {
                System.out.println("[B线程] 持有 objB 锁（没被 A 阻塞，因为不同对象）");
            }
        }, "lock-B");

        lockA.start(); lockB.start();
        lockA.join(); lockB.join();

        // ========== 场景4：同一个对象上的同步互斥 ==========
        System.out.println("\n=== 4. 同一对象上的同步互斥 ===");
        Object sharedLock = new Object();

        Thread shared1 = new Thread(() -> {
            synchronized (sharedLock) {
                System.out.println("[线程1] 获取锁，sleep 2秒");
                try { Thread.sleep(2000); } catch (InterruptedException e) { }
                System.out.println("[线程1] 释放锁");
            }
        }, "shared-1");

        Thread shared2 = new Thread(() -> {
            try { Thread.sleep(100); } catch (InterruptedException e) { }
            System.out.println("[线程2] 等待锁中...");
            synchronized (sharedLock) {
                System.out.println("[线程2] 终于拿到锁！");
            }
        }, "shared-2");

        shared1.start(); shared2.start();
        shared1.join(); shared2.join();

        System.out.println("\n=== 总结 ===");
        System.out.println("synchronized 本质：对 对象监视器（monitor） 的互斥访问。");
        System.out.println(" - 实例方法：锁的是 this");
        System.out.println(" - 静态方法：锁的是 类对象 (Xxx.class)");
        System.out.println(" - 同步块：锁的是小括号里的对象");
        System.out.println(" - 同一时间，同一个锁只能被一个线程持有，其他线程排队等。");
    }
}
