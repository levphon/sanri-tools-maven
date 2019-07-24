package learntest.locktest;

import java.util.concurrent.Semaphore;

public class ABCThreadWithSemaphore {
    private static  Semaphore semaphoreA = new Semaphore(1);
    private static  Semaphore semaphoreB = new Semaphore(0);
    private static  Semaphore semaphoreC = new Semaphore(0);

    static class printDemo {
        public void printA()  {
            print('A',semaphoreA,semaphoreB);
        }

        public void printB(){
            print('B',semaphoreB,semaphoreC);
        }
        public void printC(){
            print('C',semaphoreC,semaphoreA);
        }

        private void print(char name, Semaphore semaphore,Semaphore nextSemaphore) {
            try {
                for (int i = 0; i < 10; i++) {
                    semaphore.acquire();
                    System.out.println(name +" i:"+i);
                    nextSemaphore.release();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        final printDemo printDemo = new printDemo();
        new Thread(){
            @Override
            public void run() {
                printDemo.printA();;
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                printDemo.printB();;
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                printDemo.printC();;
            }
        }.start();
    }

}
