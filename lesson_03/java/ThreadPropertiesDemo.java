/**
 * Created by asolodkaya on 03.03.17.
 */
public class ThreadPropertiesDemo {
    public static void main(String[] args) throws InterruptedException {


        class MyThread extends Thread {

            public MyThread(ThreadGroup group, String name) {
                super(group, name);
            }

            @Override
            public void run() {
                System.out.println("Current thread: " + Thread.currentThread().getName());
                System.out.println("Me: " + MyThread.this.getName());
                System.out.println("My ID #: " + MyThread.this.getId());
                System.out.println("My priority: " + MyThread.this.getPriority());
                System.out.println("I'm daemon: " + MyThread.this.isDaemon());
                System.out.println("I'm in group: " + MyThread.this.getThreadGroup());
                System.out.println("I'm in alive: " + MyThread.this.isAlive());
                System.out.println("My state: " + MyThread.this.getState());
            }
        } ;

        ThreadGroup f = new ThreadGroup("MyGroup");
        Thread t = new MyThread(f, "MyThread");
        System.out.println("Thread state: " + t.getState());
        t.setDaemon(true);
        t.setPriority(6);
        t.start();
        t.join();
    }
}
