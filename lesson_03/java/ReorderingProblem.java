package org.levelp;

/**
 * @author Anastasiya Solodkaya.
 */
public class ReorderingProblem {

    public static class MyThread extends Thread{
        @Override
        public void run() {
            while(!ready){
                // yield current use of processor
                Thread.yield();
            }
            // Потенциально здесь может случиться:
            // 1) напечатается 20
            // 2) напечатается 10
            // 3) мы уйдем в бесконечный цикл
            // И все эти варианты действительно возможны!
            System.out.println(number);
        }
    }

    static int number = 10;
    static boolean ready = false;

    public static void main(String[] args) {
        new MyThread().start();
        number = 20;
        ready = true;
    }

}
