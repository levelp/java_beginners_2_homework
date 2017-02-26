public class ThreadInterruptionFlagsDemo {

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

                    boolean interrupted = isInterrupted();
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
                if(i == 100000){
                    t.interrupt();
                }
            }
        }
    }
}

