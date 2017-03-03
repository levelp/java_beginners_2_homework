/**
 * Created by asolodkaya on 03.03.17.
 */
public class ThreadIgnoredInterruptionDemo {

    public static final int N = 100000000;
    public static Object lock = new Object();
    public static int i = N;

    public static void main(String[] args) {

        Thread t = new Thread() {
            @Override
            public void run() {
                while (true) {
                    boolean proceed;
                    int j;
                    synchronized (lock) {
                        proceed = i > 0;
                        j = i;
                    }

                    if (proceed) {
                        System.out.println("Going to sleep (" + j + ")");
                        try {
                            sleep(10);
                        } catch (InterruptedException e) {
                            System.out.println("I'M INTERRUPTED AND IGNORE IT!");
                            System.out.println("isInterrupted() = " + isInterrupted());
                            System.out.println("interrupted() = " + interrupted());
                            // Здесь флаг прерывания уже сброшен (см. документацию метода interrupted())
                            System.out.println("isInterrupted() = " + isInterrupted());
                        }
                    } else {
                        System.out.println("OK (" + j + ")");
                        return;
                    }
                }
            }

        };
        t.start();


        for (int j = 0; j < N; j++) {
            synchronized (lock) {
                i--;
                if(i == 100000){
                    t.interrupt();
                }
            }
        }
    }
}

