package learntest.locktest;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ABCThreadLockCondition {
    static final Lock lock = new ReentrantLock();
    static Condition conditionA = lock.newCondition();
    static Condition conditionB = lock.newCondition();
    static Condition conditionC = lock.newCondition();
    private static char state = 'A';

    public static void nextLetter( ){
        switch (state){
            case 'A':
                state = 'B';
                break;
            case 'B':
                state = 'C';
                break;
            case 'C':
                state = 'A';
                break;

        }
    }

    static class PrintThread extends Thread {
        private char name;

        public PrintThread(char name) {
            this.name = name;
        }

        private void print( Condition currentCondition, Condition nextCondition) {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    System.out.println(name+" 获取锁,state:" +state);
                    while (name != state){
                        System.out.println(name+"释放锁并等待,state:"+state);
                        currentCondition.await();
                    }
                    System.out.println(Thread.currentThread().getName() + " print "+name +" state:"+state);
                    nextLetter();
                    nextCondition.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    System.out.println(name+" 释放锁");
                    lock.unlock();
                }
            }
        }

        @Override
        public void run() {
            print(getCondition(),nextCondition());
        }

        Condition getCondition(){
            switch (name){
                case 'A':return conditionA;
                case 'B':return conditionB;
            }
            return conditionC;
        }

        Condition nextCondition(){
            switch (name){
                case 'A':return conditionB;
                case 'B':return conditionC;
            }
            return conditionA;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new PrintThread('A').start();
        new PrintThread('B').start();
        new PrintThread('C').start();
    }
}
