package minitest;

import org.junit.Test;

public class TestDeadLock {
    private static TestDeadLock ourInstance = new TestDeadLock();

    public static TestDeadLock getInstance() {
        return ourInstance;
    }


    private static Object resourceA = new Object();
    private static Object resourceB = new Object();

    static class RunThreadTwo extends Thread{
        @Override
        public void run() {
            while (true) {
                synchronized (resourceB) {
                    System.out.println(Thread.currentThread().getName() + " two获取到资源B");
                    synchronized (resourceA) {
                        System.out.println(Thread.currentThread().getName() + " two获取到资源A和资源B");
                    }
                }
            }
        }
    }

    static class RunThread extends Thread{
        @Override
        public void run() {
            while (true) {
                synchronized (resourceA) {
                    System.out.println(Thread.currentThread().getName() + " 获取到资源A");
                    synchronized (resourceB) {
                        System.out.println(Thread.currentThread().getName() + " 获取到资源A和资源B");
                    }
                }
            }
        }
    }

    public static void main(String [] abc){
        RunThread threadA = new RunThread();
        RunThreadTwo threadB = new RunThreadTwo();

        threadA.start();threadB.start();
    }
}
