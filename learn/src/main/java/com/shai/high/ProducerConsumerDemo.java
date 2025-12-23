package com.shai.high;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 高并发场景下的生产者-消费者模式演示
 * 使用ReentrantLock和Condition实现线程间通信
 */
public class ProducerConsumerDemo {
    private static final int BUFFER_SIZE = 10;
    private static final Queue<Integer> buffer = new LinkedList<>();
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition bufferNotFull = lock.newCondition();
    private static final Condition bufferNotEmpty = lock.newCondition();

    // 生产者
    static class Producer implements Runnable {
        private final String name;

        public Producer(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    produce(i);
                    Thread.sleep((long) (Math.random() * 100)); // 模拟生产时间
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        private void produce(int item) throws InterruptedException {
            lock.lock();
            try {
                // 等待缓冲区不满
                while (buffer.size() == BUFFER_SIZE) {
                    System.out.println(name + " 等待缓冲区有空间...");
                    bufferNotFull.await();
                }

                buffer.offer(item);
                System.out.println(name + " 生产了: " + item + ", 缓冲区大小: " + buffer.size());

                // 通知消费者
                bufferNotEmpty.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    // 消费者
    static class Consumer implements Runnable {
        private final String name;

        public Consumer(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Integer item = consume();
                    Thread.sleep((long) (Math.random() * 150)); // 模拟消费时间
                    System.out.println(name + " 消费了: " + item);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        private Integer consume() throws InterruptedException {
            lock.lock();
            try {
                // 等待缓冲区不空
                while (buffer.size() == 0) {
                    System.out.println(name + " 等待缓冲区有数据...");
                    bufferNotEmpty.await();
                }

                Integer item = buffer.poll();
                
                // 通知生产者
                bufferNotFull.signalAll();
                System.out.println(name + " 消费后，缓冲区大小: " + buffer.size());
                
                return item;
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        Thread producer1 = new Thread(new Producer("生产者-1"));
        Thread producer2 = new Thread(new Producer("生产者-2"));
        Thread consumer1 = new Thread(new Consumer("消费者-1"));
        Thread consumer2 = new Thread(new Consumer("消费者-2"));

        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();

        try {
            producer1.join();
            producer2.join();
            consumer1.join();
            consumer2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("所有线程执行完毕，缓冲区大小: " + buffer.size());
    }
}