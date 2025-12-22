package com.shai.synchronizeddemo;

/**
 * synchronized关键字演示主类
 */
public class SynchronizedDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("========== 1. 演示线程不安全的计数器 ==========");
        testUnsafeCounter();

        System.out.println("\n========== 2. 演示synchronized方法 ==========");
        testSynchronizedMethod();

        System.out.println("\n========== 3. 演示synchronized代码块 ==========");
        testSynchronizedBlock();

        System.out.println("\n========== 4. 演示银行账户场景 ==========");
        testBankAccount();

        System.out.println("\n========== 5. 演示synchronized可重入性 ==========");
        testReentrant();
    }

    /**
     * 测试线程不安全的计数器
     */
    private static void testUnsafeCounter() throws InterruptedException {
        Counter counter = new Counter();
        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter.increment();
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("期望结果: 10000");
        System.out.println("实际结果: " + counter.getCount());
        System.out.println("结果分析: " + (counter.getCount() == 10000 ? "正确（运气好）" : "错误（存在线程安全问题）"));
    }

    /**
     * 测试synchronized方法
     */
    private static void testSynchronizedMethod() throws InterruptedException {
        SynchronizedCounter counter = new SynchronizedCounter();
        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter.increment();
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("期望结果: 10000");
        System.out.println("实际结果: " + counter.getCount());
        System.out.println("结果分析: " + (counter.getCount() == 10000 ? "正确（synchronized保证了线程安全）" : "错误"));
    }

    /**
     * 测试synchronized代码块
     */
    private static void testSynchronizedBlock() throws InterruptedException {
        SynchronizedBlockDemo demo = new SynchronizedBlockDemo();
        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    demo.incrementWithThis();
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("期望结果: 10000");
        System.out.println("实际结果: " + demo.getCount());
        System.out.println("结果分析: " + (demo.getCount() == 10000 ? "正确（synchronized代码块保证了线程安全）" : "错误"));
    }

    /**
     * 测试银行账户场景
     */
    private static void testBankAccount() throws InterruptedException {
        BankAccount account = new BankAccount(1000);

        Thread depositThread1 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                account.deposit(100);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "存款线程1");

        Thread depositThread2 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                account.deposit(200);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "存款线程2");

        Thread withdrawThread1 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                account.withdraw(150);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "取款线程1");

        depositThread1.start();
        depositThread2.start();
        withdrawThread1.start();

        depositThread1.join();
        depositThread2.join();
        withdrawThread1.join();

        System.out.println("\n最终余额: " + account.getBalance());
        System.out.println("期望余额: " + (1000 + 3*100 + 3*200 - 3*150) + " (初始1000 + 存入900 - 取出450)");
    }

    /**
     * 测试synchronized可重入性
     */
    private static void testReentrant() {
        ReentrantDemo demo = new ReentrantDemo();
        demo.method1();
        System.out.println("成功演示synchronized的可重入性");
    }

    /**
     * 可重入性演示类
     */
    static class ReentrantDemo {
        public synchronized void method1() {
            System.out.println("method1 获得锁");
            method2(); // 同一个线程可以再次获得同一个锁
        }

        public synchronized void method2() {
            System.out.println("method2 获得锁（可重入）");
            method3();
        }

        public synchronized void method3() {
            System.out.println("method3 获得锁（可重入）");
        }
    }
}
