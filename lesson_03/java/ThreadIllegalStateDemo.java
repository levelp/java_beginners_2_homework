/**
 * Created by asolodkaya on 03.03.17.
 */
public class ThreadIllegalStateDemo {
    public static void main(String[] args) {
        Thread t = new Thread(){
            @Override
            public void run() {
                System.out.println("running");
            }
        };

        t.start();
        t.start();
    }
}
