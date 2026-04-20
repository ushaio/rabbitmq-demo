package com.shai.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 面试题：如何保证线程按照某一顺序返回？
 *
 * 核心回答：
 * 1. 如果只是要求“结果按顺序返回”，不要强行控制线程执行顺序，让任务并发执行，
 *    主线程按提交顺序 Future#get 即可。
 * 2. 如果要求“线程执行/结束顺序也必须固定”，可以使用 join、Semaphore、
 *    CountDownLatch、CompletableFuture 编排依赖关系。
 */
public class ThreadOrderReturnDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== 1. 并发执行，结果按顺序返回：Future#get ===");
        returnResultInOrder();

        System.out.println("\n=== 2. 线程按顺序执行/结束：Thread#join ===");
        runInOrderByJoin();

        System.out.println("\n=== 3. 线程按顺序执行/结束：Semaphore ===");
        runInOrderBySemaphore();

        System.out.println("\n=== 4. 编排异步任务顺序：CompletableFuture ===");
        runInOrderByCompletableFuture();
    }

    /**
     * 最推荐用于“返回结果有序”的场景：
     * 任务实际可以乱序完成，但主线程按照 Future 列表顺序 get，最终返回顺序稳定。
     */
    private static void returnResultInOrder() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        List<Future<String>> futures = new ArrayList<>();

        futures.add(executorService.submit(createTask("A", 800)));
        futures.add(executorService.submit(createTask("B", 300)));
        futures.add(executorService.submit(createTask("C", 500)));

        for (Future<String> future : futures) {
            System.out.println("按提交顺序获取结果：" + future.get());
        }

        executorService.shutdown();
        executorService.awaitTermination(3, TimeUnit.SECONDS);
    }

    /**
     * join 的特点：简单直接，但会把并发任务变成串行任务。
     */
    private static void runInOrderByJoin() throws InterruptedException {
        Thread t1 = new Thread(() -> doWork("T1", 300), "thread-1");
        Thread t2 = new Thread(() -> doWork("T2", 300), "thread-2");
        Thread t3 = new Thread(() -> doWork("T3", 300), "thread-3");

        t1.start();
        t1.join();

        t2.start();
        t2.join();

        t3.start();
        t3.join();
    }

    /**
     * Semaphore 的特点：线程可以先都启动，但通过许可证控制真正执行顺序。
     */
    private static void runInOrderBySemaphore() throws InterruptedException {
        Semaphore first = new Semaphore(1);
        Semaphore second = new Semaphore(0);
        Semaphore third = new Semaphore(0);

        Thread t1 = new Thread(() -> runWithSemaphore("T1", first, second), "thread-1");
        Thread t2 = new Thread(() -> runWithSemaphore("T2", second, third), "thread-2");
        Thread t3 = new Thread(() -> runWithSemaphore("T3", third, null), "thread-3");

        // 故意乱序启动，证明执行顺序由 Semaphore 控制。
        t3.start();
        t2.start();
        t1.start();

        t1.join();
        t2.join();
        t3.join();
    }

    /**
     * CompletableFuture 的特点：更适合异步任务之间存在先后依赖的业务流程。
     */
    private static void runInOrderByCompletableFuture() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        CompletableFuture<Void> future = CompletableFuture
                .runAsync(() -> doWork("step-1", 300), executorService)
                .thenRunAsync(() -> doWork("step-2", 300), executorService)
                .thenRunAsync(() -> doWork("step-3", 300), executorService);

        future.join();
        executorService.shutdown();
    }

    private static Callable<String> createTask(String name, long sleepMillis) {
        return () -> {
            doWork(name, sleepMillis);
            return name + " result";
        };
    }

    private static void runWithSemaphore(String name, Semaphore current, Semaphore next) {
        try {
            current.acquire();
            doWork(name, 300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (next != null) {
                next.release();
            }
        }
    }

    private static void doWork(String name, long sleepMillis) {
        try {
            TimeUnit.MILLISECONDS.sleep(sleepMillis);
            System.out.println(name + " 完成，执行线程：" + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
