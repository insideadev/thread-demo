package com.epam.threaddemo;

public class Print1To9Demo {

    private static final String THREAD_1 = "Thread-1";
    private static final String THREAD_2 = "Thread-2";
    private static final String THREAD_3 = "Thread-3";
    private static int count = 1;
    private static String threadAllowedToPrint = THREAD_1;

    private static class Printer implements Runnable {

        @Override
        public void run() {
            while (count <= 9) {
                String currThread = Thread.currentThread().getName();
                if (threadAllowedToPrint.equals(currThread)) {
                    System.out.println(currThread + " prints " + count++);
                    determineNextThread(currThread);
                } else {
                    System.out.println(currThread + " trying again");
                }
            }
        }


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
        Runnable printer = new OptimizedPrinter();
        long start = System.currentTimeMillis();

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
        long stop = System.currentTimeMillis();
        System.out.println("Execution time: " + (stop - start));
    }

    private static class OptimizedPrinter implements Runnable {

        private static final Object lock = new Object();

        @Override
        public void run() {
            while (count <= 9) {
                String currThread = Thread.currentThread().getName();
                synchronized (lock) {
                    if (threadAllowedToPrint.equals(currThread)) {
                        System.out.println(currThread + " prints " + count++);
                        determineNextThread(currThread);
                        lock.notifyAll();
                    } else {
                        System.out.println(currThread + " waiting for its turn");
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }
}
