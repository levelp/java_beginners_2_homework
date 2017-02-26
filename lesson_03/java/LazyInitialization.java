package org.levelp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author Anastasiya Solodkaya.
 */
public class LazyInitialization {
    public static class Singleton {
        private static Singleton instance;
        public static Singleton get(){
            if(instance == null) {
                instance = new Singleton();
                return instance;
            }
            return instance;
        }
    }

    public static void main(String[] args) {
        HashSet<String> addresses = new HashSet<>();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    addresses.add(Singleton.get().toString());
                }
            });
            thread.start();
            threads.add(thread);
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (String address : addresses) {
            System.out.println(address);
        }

    }
}
