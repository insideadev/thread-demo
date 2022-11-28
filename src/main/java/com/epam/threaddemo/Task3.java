package com.epam.threaddemo;

import java.util.LinkedList;
import java.util.Random;

public class Task3 {
    public static void main(String[] args)
            throws InterruptedException {
        // Object of a class that has both produce()
        // and consume() methods
        final MessageQueue messageQueue = new MessageQueue();

        // Create producer thread
        Thread t1 = new Thread(() -> {
            try {
                messageQueue.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Create consumer thread
        Thread t2 = new Thread(() -> {
            try {
                messageQueue.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Start both threads
        t1.start();
        t2.start();

        // t1 finishes before t2
        t1.join();
        t2.join();
    }

    // This class has a list, producer (adds items to list
    // and consumer (removes items).
    public static class MessageQueue {

        // Create a list shared by producer and consumer
        // Size of list is 2.
        LinkedList<Integer> list = new LinkedList<>();
        Random random = new Random();
        int capacity = 2;

        // Function called by producer thread
        public void produce() throws InterruptedException {
            while (true) {
                synchronized (this) {
                    // producer thread waits while list is full
                    while (list.size() == capacity) {
                        wait();
                    }

                    int value = random.nextInt(100) + 1;
                    System.out.println("Producer produced " + value );

                    list.add(value);

                    // notifies the consumer thread that now it can start consuming
                    notify();

                    Thread.sleep(1000);
                }
            }
        }

        // Function called by consumer thread
        public void consume() throws InterruptedException {
            while (true) {
                synchronized (this) {
                    // consumer thread waits while list is empty
                    while (list.size() == 0) {
                        wait();
                    }

                    // to retrieve the first job in the list
                    int val = list.removeFirst();

                    System.out.println("Consumer consumed " + val);

                    // Wake up producer thread
                    notify();

                    // and sleep
                    Thread.sleep(1000);
                }
            }
        }
    }
}