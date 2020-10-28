package com.maslke.demo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * not thread safe
 */
public class CheeseLine implements Runnable {

    private int maxCount;
    private CountDownLatch latch;
    private ReentrantLock reentrantLock;
    private ArrayBlockingQueue<Integer> milkQueue;
    private ArrayBlockingQueue<Integer> fermentQueue;
    private ArrayBlockingQueue<Integer> cheeseQueue;
    private int maxCapacity;
    private Condition notEmpty;
    private Condition notFull;

    public CheeseLine(CountDownLatch latch, ReentrantLock reentrantLock, int maxCount, ArrayBlockingQueue<Integer> milkQueue,
                      ArrayBlockingQueue<Integer> fermentQueue, ArrayBlockingQueue<Integer> cheeseQueue, int maxCapacity,
                      Condition notEmpty, Condition notFull) {
        this.latch = latch;
        this.reentrantLock = reentrantLock;
        this.maxCount = maxCount;
        this.milkQueue = milkQueue;
        this.fermentQueue = fermentQueue;
        this.cheeseQueue = cheeseQueue;
        this.maxCapacity = maxCapacity;
        this.notFull = notFull;
        this.notEmpty = notEmpty;
    }

    @Override
    public void run() {
        int count = 0;
        while (true) {
            if (count == maxCount) {
                break;
            }
            reentrantLock.lock();
            try {
                milkQueue.take();
                milkQueue.take();
                fermentQueue.take();
                while (cheeseQueue.size() >= maxCapacity) {
                    notFull.await();
                }
                count++;
                cheeseQueue.put(1);
                notEmpty.signalAll();
                System.out.println("已生产一份奶酪");
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        }
        System.out.println("奶酪已生成完成");
        latch.countDown();
    }
}
