package com.sanri.deginmodel.state;

public class StartOrderState extends OrderState {
    @Override
    public void doSomething(RunOrder runOrder) {
        System.out.println("开始订单");
        if(runOrder.value == 1){
            runOrder.setOrderState(new CancelOrderState());
        }else{
            runOrder.setOrderState(new DoneOrderState());
        }
    }
}
