/**
 * Created by asolodkaya on 03.03.17.
 */
public class LeftRightDeadLock {
    public static final int M = 10000;
    public static final int N = 10000000;
    private static final Object left = new Object();
    private static final Object right = new Object();
    public static void leftRight() {
        synchronized (left) {
            for (int i = 0; i < M; i++) {
                System.out.println("pre-left!");
            }
            synchronized (right) {
                for (int i = 0; i < N; i++) {
                    System.out.println("i = " + i);
                }
            }
        }
    }
    public static void rightLeft() {
        synchronized (right) {
            for (int i = 0; i < M; i++) {
                System.out.println("pre-right!");
            }
            synchronized (left) {
                for (int j = 0; j < N; j++) {
                    System.out.println("j = " + j);
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread t1  = new Thread(new Runnable() {
            @Override
            public void run() {
                rightLeft();
            }
        });

        Thread t2  = new Thread(new Runnable() {
            @Override
            public void run() {
                leftRight();
            }
        });

        t1.start();
        t2.start();
    }
}
