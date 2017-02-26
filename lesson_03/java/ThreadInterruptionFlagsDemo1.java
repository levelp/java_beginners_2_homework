/**
 * Created by asolodkaya on 03.03.17.
 */
public class ThreadInterruptionFlagsDemo1 {

    public static final int N = 100000;
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

                    // Если вам повезло по таймингу, то среди output вы найдете ровно одну запись с true
                    boolean interrupted = interrupted();
                    if (proceed) {
                        System.out.println("Going to sleep (" + j + "), interrupted: " + interrupted);
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
                if(i == 10000){
                    t.interrupt();
                }
            }
        }
    }
}

