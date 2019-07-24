package com.sanri.deginmodel.test;

import com.sanri.deginmodel.state.RunOrder;
import com.sanri.deginmodel.state.StartOrderState;
import org.junit.Test;

public class StateTest {
    @Test
    public void testState(){
        RunOrder runOrder = new RunOrder(new StartOrderState());
        runOrder.value = 2;
        runOrder.printState();
        runOrder.printState();
        runOrder.printState();
        runOrder.printState();
    }
}
