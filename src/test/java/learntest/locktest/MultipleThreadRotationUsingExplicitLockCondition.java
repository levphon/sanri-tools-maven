package learntest.locktest;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by SunYanhui on 2017/12/7.
 */
public class MultipleThreadRotationUsingExplicitLockCondition {
    public static void main(String[] args) {
        final PrintABCUsingCondition printABC = new PrintABCUsingCondition();
        new Thread(){
            @Override
            public void run() {
                printABC.printA();
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                printABC.printB();
            }
        }.start();new Thread(){
            @Override
            public void run() {
                printABC.printC();
            }
        }.start();

    }

}

class PrintABCUsingCondition {
    private Lock lock = new ReentrantLock();
    private Condition conditionA = lock.newCondition();
    private Condition conditionB = lock.newCondition();
    private Condition conditionC = lock.newCondition();
    private int state = 0;
    //private int attempts = 0;

    public void printA() {
        print("A", 0, conditionA, conditionB);
    }

    public void printB() {
        print("B", 1, conditionB, conditionC);
    }

    public void printC() {
        print("C", 2, conditionC, conditionA);
    }

    private void print(String name, int currentState, Condition currentCondition, Condition nextCondition) {
        for (int i = 0; i < 10; ) {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName()+" try to print "+name+", attempts : "+(state));
                while (state % 3 != currentState) {
                    currentCondition.await();
                }
                System.out.println(Thread.currentThread().getName() + " print " + name);
                state++;
                i++;
                nextCondition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
