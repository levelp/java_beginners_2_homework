package org.levelp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Anastasiya Solodkaya.
 */
public class InvariantAtomicitySafe {

    static final Object lock = new Object();
    static int value = 0;
    static int division = 0;

    public static void update(int newValue) {
        synchronized (lock) {
            if (newValue == value) {
                int d = division;
                if (d != newValue / 123) {
                    System.out.printf("%d / %d = %d%n", value, 123, division);
                }
            } else {
                value = newValue;
                division = newValue / 123;
            }
        }
    }


    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            test();
        }
    }

    private static void test() {
        List<Thread> threads = new ArrayList<>();
        int N = 1000;
        Random random = new Random();

        for (int i = 0; i < N; i++) {
            threads.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 100; j++) {
                        update(random.nextInt(10000));
                    }
                }
            }));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
