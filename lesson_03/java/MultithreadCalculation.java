
import java.util.Arrays;
import java.util.Random;

/**
 * Created by asolodkaya on 09.03.17.
 */
public class MultithreadCalculation {
    private static class SumCalculator extends Thread {
        private int[] arr;
        private int start;
        private int end;
        private long total;

        public SumCalculator(int[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            for (int i = start; i < end; i++) {
                total += arr[i];
            }
        }

        public long getTotal() {
            return total;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            runCalculation();
        }

    }

    private static void runCalculation() throws InterruptedException {
        int THREADS_COUNT = 8;
        Random r = new Random();
        int[] array = new int[8 * 1024];
        for (int i = 0; i < array.length; i++) {
            array[i] = r.nextInt(100);
        }

        SumCalculator[] threads = new SumCalculator[THREADS_COUNT];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new SumCalculator(array, i * array.length / THREADS_COUNT, (i + 1) * array.length / THREADS_COUNT);
            threads[i].start();
        }

        long total = 0;
        for (SumCalculator thread : threads) {
            thread.join();
            total += thread.getTotal();
        }

        System.out.println("TOTAL: " + total);

//        check!
        int expected = Arrays.stream(array).sum();
        if (expected != total) {
            System.out.println("[FAILED]: expected " + expected + ", found: " + total);
        } else {
            System.out.println("[OK]");
        }
    }
}
