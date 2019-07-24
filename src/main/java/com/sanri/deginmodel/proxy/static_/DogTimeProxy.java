package com.sanri.deginmodel.proxy.static_;

/**
 * 作者: sanri
 * 时间: 2017/08/10 21:27
 * 功能: 代理类代理目标 Dog 类,用来计算狗的移动时间
 * 需要聚合 Dog
 */
public class DogTimeProxy implements  Moveable {
    private Moveable target;

    public DogTimeProxy(Moveable target){
        this.target = target;
    }

    @Override
    public void move() {
        long startTime = System.currentTimeMillis();
        System.out.println("移动开始时间为:"+startTime);
        target.move();
        System.out.println("移动花费时间:"+(System.currentTimeMillis() - startTime));
    }
}
