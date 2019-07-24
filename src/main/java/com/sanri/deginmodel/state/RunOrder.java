package com.sanri.deginmodel.state;

public class RunOrder {
    private OrderState orderState;
    public int value;

    public RunOrder(OrderState orderState) {
        this.orderState = orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public void printState() {
        orderState.doSomething(this);
    }
}
