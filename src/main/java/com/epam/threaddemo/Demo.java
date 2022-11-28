package com.epam.threaddemo;

public class Demo {

    private static final Object lock = new Object();
    private static final String NOTIFIER = "notifier";

    private static class MyThread extends Thread {

        private void oneThreadAtATime() throws InterruptedException {
            if (Thread.currentThread().getName().equals(NOTIFIER)) {
                Thread.sleep(5000);
                synchronized (lock) {
                    lock.notifyAll();
                }
            } else {
                synchronized (lock) {
                    String threadName = Thread.currentThread().getName();
                    System.out.println(threadName + " started");
                    Thread.sleep(2000);
                    System.out.println(threadName + " is waiting");
                    lock.wait();
                    System.out.println(threadName + " awake");
                    System.out.println(threadName + " finished");
                }
            }
        }

        public void run() {
            try {
                oneThreadAtATime();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new MyThread();
        Thread t2 = new MyThread();
        Thread t3 = new MyThread();
        t1.start();
        t2.start();
        t3.setName(NOTIFIER);
        t3.start();
    }
}
