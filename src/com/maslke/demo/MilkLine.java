package com.maslke.demo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 *  not thread safe
 */
public class MilkLine implements Runnable {

    private CountDownLatch latch;
    private int maxCount;
    private ArrayBlockingQueue<Integer> milkQueue;

    public MilkLine(CountDownLatch latch, int maxCount, ArrayBlockingQueue<Integer> milkQueue) {
        this.latch = latch;
        this.maxCount = maxCount;
        this.milkQueue = milkQueue;
    }

    @Override
    public void run() {
        int count = 0;
        while (true) {
            if (count == maxCount * 2) {
                break;
            }
            try {
                milkQueue.add(1);
                System.out.println("已生产一份牛奶");
                count++;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("牛奶已生成完成");
        latch.countDown();
    }
}
