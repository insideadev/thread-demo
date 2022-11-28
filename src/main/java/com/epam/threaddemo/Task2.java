package com.epam.threaddemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Task2 {

    private static final Random random = new Random();
    private static final List<Integer> list = new ArrayList<>();
    private static final String THREAD_1 = "Thread-1";
    private static final String THREAD_2 = "Thread-2";
    private static final String THREAD_3 = "Thread-3";
    private static String threadAllowedToPrint = THREAD_1;

    private static class Printer implements Runnable {

        @Override
        public void run() {
            while (true) {
                String currThread = Thread.currentThread().getName();
                synchronized (this) {
                    if (threadAllowedToPrint.equals(currThread)) {
                        switch (threadAllowedToPrint) {
                            case THREAD_1:
                                addNumberToList();
                                break;

                            case THREAD_2:
                                calculateSum();
                                break;

                            case THREAD_3:
                                calculateSquareRoot();
                                break;
                        }
                        determineNextThread(currThread);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        this.notifyAll();
                    } else {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    private static void addNumberToList() {
        int number = random.nextInt(10) + 1;
        list.add(number);
        System.out.println("Added number: " + number);
    }

    private static void calculateSum() {
        int sum = list.stream().reduce(0, Integer::sum);
        System.out.println("Sum: " + sum);
    }

    private static void calculateSquareRoot() {
        int sum = list.stream().reduce(0, (a, b) -> a + b * b);
        System.out.println("Square root: " + Math.sqrt(sum));
    }

    private static void determineNextThread(String currThread) {
        switch (currThread) {
            case THREAD_1:
                threadAllowedToPrint = THREAD_2;
                break;

            case THREAD_2:
                threadAllowedToPrint = THREAD_3;
                break;

            case THREAD_3:
                threadAllowedToPrint = THREAD_1;
                break;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable printer = new Printer();
        Thread t1 = new Thread(printer);
        t1.setName(THREAD_1);
        t1.start();
        Thread t2 = new Thread(printer);
        t2.setName(THREAD_2);
        t2.start();
        Thread t3 = new Thread(printer);
        t3.setName(THREAD_3);
        t3.start();

        t1.join();
        t2.join();
        t3.join();
    }
}
