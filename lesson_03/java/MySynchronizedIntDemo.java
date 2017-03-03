/**
 * Created by asolodkaya on 03.03.17.
 */
public class MySynchronizedIntDemo {
    public static class MySynchronizedInt {
        private int value;

        public MySynchronizedInt() {
            this.value = 0;
        }

        public synchronized void increment(){
            value++;
        }

        public synchronized void decrement(){
            value--;
        }

        public synchronized int getValue() {
            return value;
        }
    }

    public static class MyBrokenSynchronizedInt {
        private int value;

        public MyBrokenSynchronizedInt() {
            this.value = 0;
        }

        public synchronized void increment(){
            value++;
        }

        public void decrement(){
            value--;
        }

        public int getValue() {
            return value;
        }
    }

    public static class MyNonSynchronizedInt {
        private int value;

        public MyNonSynchronizedInt() {
            this.value = 0;
        }

        public void increment(){
            value++;
        }

        public void decrement(){
            value--;
        }

        public int getValue() {
            return value;
        }
    }

    public static final int N = 1000000;
    public static MySynchronizedInt a = new MySynchronizedInt();
    public static MyBrokenSynchronizedInt b = new MyBrokenSynchronizedInt();
    public static MyNonSynchronizedInt c = new MyNonSynchronizedInt();

    public static void main(String[] args) throws InterruptedException {
        Thread incThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < N; i++) {
                    a.increment();
                    b.increment();
                    c.increment();
                }
            }
        });

        Thread decThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < N; i++) {
                    a.decrement();
                    b.decrement();
                    c.decrement();
                }
            }
        });

        incThread.start();
        decThread.start();

        incThread.join();
        decThread.join();

        System.out.println("a = " + a.getValue());
        System.out.println("b = " + b.getValue());
        System.out.println("c = " + c.getValue());
    }

}
