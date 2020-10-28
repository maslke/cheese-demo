package com.maslke.demo;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CheeseFactory {
    private int maxCount;
    private int maxCapacity;
    private int batch;

    public CheeseFactory(int maxCount, int maxCapacity, int batch) {
        this.maxCount = maxCount;
        this.maxCapacity = maxCapacity;
        this.batch = batch;
    }

    public void start() throws Exception {
        ArrayBlockingQueue<Integer> milkQueue = new ArrayBlockingQueue<>(maxCount * 2);
        ArrayBlockingQueue<Integer> fermentQueue = new ArrayBlockingQueue<>(maxCount);
        ArrayBlockingQueue<Integer> cheeseQueue = new ArrayBlockingQueue<>(maxCount);

        ReentrantLock reentrantLock = new ReentrantLock();
        Condition notEmpty = reentrantLock.newCondition();
        Condition notFull = reentrantLock.newCondition();
        CountDownLatch latch = new CountDownLatch(4);
        MilkLine milkLine = new MilkLine(latch, maxCount, milkQueue);
        FermentLine fermentLine = new FermentLine(latch, maxCount, fermentQueue);
        CheeseLine cheeseLine = new CheeseLine(latch, reentrantLock, maxCount, milkQueue, fermentQueue, cheeseQueue, maxCapacity, notEmpty, notFull);
        Truck truck = new Truck(latch, reentrantLock, notEmpty, notFull, cheeseQueue, maxCount, batch);

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.submit(milkLine);
        executorService.submit(fermentLine);
        executorService.submit(cheeseLine);
        executorService.submit(truck);
        latch.await();
        System.out.println("生产任务已完成");
    }
}
