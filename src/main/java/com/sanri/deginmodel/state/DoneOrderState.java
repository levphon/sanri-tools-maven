package com.sanri.deginmodel.state;

public class DoneOrderState extends OrderState {
    @Override
    public void doSomething(RunOrder runOrder) {
        System.out.println("订单已结束 ,请尽快支付");
        runOrder.setOrderState(new PayOrderState());
    }
}
