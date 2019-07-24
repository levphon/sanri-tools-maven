package com.sanri.deginmodel.state;

public class CancelOrderState extends OrderState {
    @Override
    public void doSomething(RunOrder runOrder) {
        System.out.println("取消订单");
    }
}
