package com.jinyi.medical.unsynch;

import java.util.concurrent.TimeUnit;

public class ABCThread implements Runnable{

    private String name;
    private Object pre;
    private Object self;

    public ABCThread(String name,Object pre,Object self){
        this.name = name;
        this.pre = pre;
        this.self = self;
    }

    public static void main(String[] args) throws InterruptedException {

        Object a = new Object();
        Object b = new Object();
        Object c = new Object();

        Thread pa = new Thread(new ABCThread("A",c,a));
        Thread pb = new Thread(new ABCThread("B",a,b));
        Thread pc = new Thread(new ABCThread("C",b,c));
        pa.start();
        TimeUnit.MILLISECONDS.sleep(100);
        pb.start();
        TimeUnit.MILLISECONDS.sleep(100);
        pc.start();

    }

    public void run(){

        int count = 10;
        while(count>0){
            synchronized (pre) {                        // C A
                synchronized (self) {                   // A B
                    System.out.print(name);             // B C
                    count --;
                    self.notify();
                }
                try {
                    pre.wait();
                } catch (Exception e) {
                }

            }

        }

    }


}
