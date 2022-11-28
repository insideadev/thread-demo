package com.epam.threaddemo;

import java.util.Random;

public class TicketDemo {

    private static int stockOfTicket = 10;
    private static final Object lock = new Object();
    private static final Random random = new Random();

    private static class Customer implements Runnable {

        String name;

        public Customer(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            int ticketNeeded = random.nextInt(5)  + 1;
            System.out.println(name + " needs " + ticketNeeded + " tickets");
            try {
                Thread.sleep(ticketNeeded * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            synchronized (lock) {
                if (ticketNeeded <= stockOfTicket) {
                    stockOfTicket -= ticketNeeded;
                    System.out.println(ticketNeeded + " tickets sold to " + name);
                } else {
                    System.out.println("Could not sale ticket to " + name + ". Available tickets: " + stockOfTicket);
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new Customer("1")); t1.start();
        Thread t2 = new Thread(new Customer("2")); t2.start();
        Thread t3 = new Thread(new Customer("3")); t3.start();
        Thread t4 = new Thread(new Customer("4")); t4.start();
        Thread t5 = new Thread(new Customer("5")); t5.start();
    }
}
