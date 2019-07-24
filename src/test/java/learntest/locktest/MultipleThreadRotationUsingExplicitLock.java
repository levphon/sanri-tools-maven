package learntest.locktest;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MultipleThreadRotationUsingExplicitLock {
    public static void main(String[] args) {
        final PrintABCUsingReentrantLock printABCUsingReentrantLock = new PrintABCUsingReentrantLock();
        new Thread(){
            @Override
            public void run() {
                printABCUsingReentrantLock.printA();
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                printABCUsingReentrantLock.printB();
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                printABCUsingReentrantLock.printC();
            }
        }.start();



    }

    static class PrintABCUsingReentrantLock{
        private int state = 0;
        private Lock lock = new ReentrantLock();

        public void printA(){
            print("A",0);
        }

        public void printB(){
            print("B",1);
        }

        public void printC(){
            print("C",2);
        }

        private void print(String name, int currentState) {
            for (int i = 0; i <10 ; ) {
                lock.lock();
                try{
                    while(state % 3 == currentState){
                        System.out.println("state:"+state+" | "+Thread.currentThread().getName() + " print "+name);
                        state ++;
                        i++;
                    }
                }finally {
                    lock.unlock();
                }
            }

        }
    }

}
