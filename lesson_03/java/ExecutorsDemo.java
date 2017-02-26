package org.levelp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Anastasiya Solodkaya.
 */
public class ExecutorsDemo {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

    }
}
