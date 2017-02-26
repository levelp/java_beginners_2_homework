package org.levelp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Anastasiya Solodkaya.
 */
public class InvariantAtomicityNonSafe {
    static AtomicInteger value = new AtomicInteger();
    static AtomicInteger division = new AtomicInteger();

    public static void update (int newValue){
        if(Integer.compare(newValue, value.get()) == 0){
            int d = division.get();
            if(d != newValue / 123) {
                System.out.printf("%d / %d = %d%n", value.get(), 123, division.get());
            }
        }else {
            value.set(newValue);
            division.set(newValue / 123);
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
