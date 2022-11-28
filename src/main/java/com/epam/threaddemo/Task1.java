package com.epam.threaddemo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Thread.MAX_PRIORITY;
import static java.lang.Thread.MIN_PRIORITY;

public class Task1 {

    private final Map<Integer, Integer> map;

    public Task1(Map<Integer, Integer> map) {
        this.map = map;
    }

    public static void main(String[] args) throws InterruptedException {
        Map<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();
        Map<Integer, Integer> hashMap = new HashMap<>();
        Map<Integer, Integer> synchronizedMap = Collections.synchronizedMap(hashMap);
        Map<Integer, Integer> synchronizedHashMap = new SynchronizedHashMap<>();

        Task1 app = new Task1(synchronizedHashMap);

        Thread t1 = new Thread(app::putValue);
        Thread t2 = new Thread(app::calculateSum);

        t1.setPriority(MAX_PRIORITY);
        t2.setPriority(MIN_PRIORITY);

        long start = System.currentTimeMillis();

        t1.start();
        t2.start();
        t2.join();

        System.out.println("Execution time: " + (System.currentTimeMillis() - start) + " ms");
    }

    private void calculateSum() {
        synchronized (map) {
            long sum = map.values().stream().reduce(0, Integer::sum);
            System.out.println("Sum value: " + sum);
        }
    }

    private void putValue() {
        synchronized (map) {
            for (int i = 0; i < 10_000; i++) {
                map.put(i, i);
            }
        }
    }
}
