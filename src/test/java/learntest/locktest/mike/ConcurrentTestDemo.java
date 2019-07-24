package learntest.locktest.mike;

import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ConcurrentTestDemo {

    public static void main(String[] args) {
        int currency = 20;
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(currency);

        final OrderService orderService = new OrderServiceWithLock();

        //多线程模拟高并发
        for (int i = 0; i < currency; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() +" 我准备好了");
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                    orderService.createOrder();

                }
            }).start();
        }

    }
}
