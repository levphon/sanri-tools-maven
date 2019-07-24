package com.sanri.deginmodel.state;

public class FinishOrderState extends OrderState {
    @Override
    public void doSomething(RunOrder runOrder) {
        System.out.println("订单完成");
    }
}
