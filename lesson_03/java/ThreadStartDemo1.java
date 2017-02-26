/**
 * Created by asolodkaya on 03.03.17.
 */
public class ThreadStartDemo1 {

    public static void main(String[] args) {
        Thread t = new Thread() {
            @Override
            public void run() {
                System.out.println("Hello, I'm a thread!");
            }
        };
        t.start();
    }
}
