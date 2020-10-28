package com.maslke.demo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * not thread safe
 */
public class FermentLine implements Runnable {

    private CountDownLatch latch;
    private int maxCount;
    private ArrayBlockingQueue<Integer> fermentQueue;

    public FermentLine(CountDownLatch latch, int maxCount, ArrayBlockingQueue<Integer> fermentQueue) {
        this.maxCount = maxCount;
        this.latch = latch;
        this.fermentQueue = fermentQueue;
    }

    @Override
    public void run() {
        int count = 0;
        while (true) {
            if (count == maxCount) {
                break;
            }
            try {
                fermentQueue.add(1);
                count++;
                System.out.println("已生产一份发酵剂");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("发酵剂已生成完成");
        latch.countDown();
    }
}
