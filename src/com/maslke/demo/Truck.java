package com.maslke.demo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Truck implements Runnable {

    private CountDownLatch latch;
    private ReentrantLock reentrantLock;
    private Condition notEmpty;
    private Condition notFull;
    private ArrayBlockingQueue<Integer> cheeseQueue;
    private int maxCount;
    private int batch;

    public Truck(CountDownLatch latch, ReentrantLock reentrantLock, Condition notEmpty,
                 Condition notFull, ArrayBlockingQueue<Integer> cheeseQueue, int maxCount, int batch) {
        this.latch = latch;
        this.reentrantLock = reentrantLock;
        this.notEmpty = notEmpty;
        this.notFull = notFull;
        this.cheeseQueue = cheeseQueue;
        this.maxCount = maxCount;
        this.batch = batch;
    }

    @Override
    public void run() {
        int count = 0;
        while (true) {
            if(count == maxCount) {
                break;
            }
            reentrantLock.lock();
            try {
                while (cheeseQueue.size() < batch) {
                    notEmpty.await();
                }
                for (int i = 0; i < batch; i++) {
                    cheeseQueue.take();
                }
                System.out.println("已运输完成一批次奶酪: " + count / batch);
                count += batch;
                notFull.signalAll();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        }
        System.out.println("奶酪已全部运输完成");
        latch.countDown();
    }
}
