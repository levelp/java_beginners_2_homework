package org.levelp;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Anastasiya Solodkaya.
 */
public class GoodAtomicity {
    static AtomicInteger counter = new AtomicInteger(0);
    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            run();
        }
    }

    private static void run() {
        counter = new AtomicInteger(0);
        int N = 1000000;

        Thread increment = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < N; i++) {
                    counter.incrementAndGet();
                }
            }
        });

        Thread decrement = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < N; i++) {
                    counter.decrementAndGet();
                }
            }
        });

        decrement.start();
        increment.start();

        try {
            decrement.join();
            increment.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(counter);
    }
}
