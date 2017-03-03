/**
 * Created by asolodkaya on 03.03.17.
 */
public class LeftRightDeadlLock1 {
    public static final int M = 10000;
    public static final int N = 10000000;
    private static final Object left = new Object();
    private static final Object right = new Object();

    public static void lock(Object o1, Object o2, String marker) {
        synchronized (o1) {
            for (int i = 0; i < M; i++) {
                System.out.println(marker);
            }
            synchronized (o2) {
                for (int i = 0; i < N; i++) {
                    System.out.println(marker + " i = " + i);
                }
            }
        }
    }


    public static void main(String[] args) {
        Thread t1  = new Thread(new Runnable() {
            @Override
            public void run() {
                lock(right, left, "First");
            }
        });

        Thread t2  = new Thread(new Runnable() {
            @Override
            public void run() {
                lock(left, right, "Second");
            }
        });

        t1.start();
        t2.start();
    }
}
