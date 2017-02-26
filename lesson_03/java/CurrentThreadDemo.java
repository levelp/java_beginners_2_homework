/**
 * Created by asolodkaya on 03.03.17.
 */
public class CurrentThreadDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Inside: " + Thread.currentThread().getName());
            }
        }, "MyThread");
        t.start();
        t.join();
        System.out.println("Outside: " + Thread.currentThread().getName());
    }
}
