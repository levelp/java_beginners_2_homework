package org.levelp;

/**
 * @author Anastasiya Solodkaya.
 */
public class LostAtomicity {
    static int counter = 0;
    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            run();
        }
    }

    private static void run() {
        counter = 0;
        int N = 1000000;

        Thread increment = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < N; i++) {
                    counter++;
                }
            }
        });

        Thread decrement = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < N; i++) {
                    counter--;
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
