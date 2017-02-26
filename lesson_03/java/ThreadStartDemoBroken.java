/**
 * Created by asolodkaya on 03.03.17.
 */
public class ThreadStartDemoBroken {
    public static void main(String[] args) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Running " + Thread.currentThread().getName());
            }
        });
        t.run();
    }
}

