package com.maslke.demo;

public class Main {

    private static final int MAX_COUNT = 100000;
    private static final int MAX_CAPACITY = 1000;
    private static final int BATCH = 100;

    public static void main(String[] args) throws Exception {

        CheeseFactory factory = new CheeseFactory(MAX_COUNT, MAX_CAPACITY, BATCH);
        factory.start();
    }
}
