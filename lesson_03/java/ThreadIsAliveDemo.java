/**
 * Created by asolodkaya on 03.03.17.
 */
public class ThreadIsAliveDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(){
            @Override
            public void run() {
                System.out.println("Alive? " + isAlive());
            }
        };
        t.start();
        t.join();
        System.out.println("Alive? " + t.isAlive());
    }
}
