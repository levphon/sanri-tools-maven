package learntest.locktest;

public class ABCThreadMy {

    static int count = 30;

    static class A extends Thread{
        Thread b;Thread c;

        @Override
        public void run() {
            while (true) {
                synchronized (c) {
                    synchronized (this) {
                        if (count-- > 0) {
                            System.out.print("A");
                            this.notify();
                        }
                    }

                    try {
                        c.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void setB(Thread b) {
            this.b = b;
        }

        public void setC(Thread c) {
            this.c = c;
        }
    }

    static class B extends Thread{
        Thread a;
        Thread c;

        @Override
        public void run() {
            while (true) {
                synchronized (a) {
                    synchronized (this) {
                        if (count-- > 0) {
                            System.out.print("B");
                            this.notify();
                        }
                    }

                    try {
                        a.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void setA(Thread a) {
            this.a = a;
        }

        public void setC(Thread c) {
            this.c = c;
        }
    }

    static class C extends Thread{
        Thread b;
        Thread a;

        @Override
        public void run() {
            while (true) {
                synchronized (b) {
                    synchronized (this) {
                        if (count-- > 0) {
                            System.out.print("C");
                            this.notify();
                        }
                    }

                    try {
                        b.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void setB(Thread b) {
            this.b = b;
        }

        public void setA(Thread a) {
            this.a = a;
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Thread a = new A();Thread b = new B();Thread c  =new C();

        ((A) a).setB(b);((A) a).setC(c);
        ((B) b).setA(a);((B) b).setC(c);
        ((C) c).setA(a);((C) c).setB(b);

        a.start();Thread.sleep(100);
        b.start();Thread.sleep(100);
        c.start();
    }
}
