package com.sanri.deginmodel.proxy.static_;

/**
 * 作者: sanri
 * 时间: 2017/08/10 21:41
 * 功能: 事务代理
 */
public class TransactionProxy implements Moveable{
    private Moveable target;
    public TransactionProxy(Moveable target){
        this.target = target;
    }

    @Override
    public void move() {
        System.out.println("开始事务");
        target.move();
        System.out.println("提交事务");
    }
}
