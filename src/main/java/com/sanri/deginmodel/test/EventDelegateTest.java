package com.sanri.deginmodel.test;

import com.sanri.deginmodel.observer.eventdelegate.ChildNotifier;
import com.sanri.deginmodel.observer.eventdelegate.DadListener;
import com.sanri.deginmodel.observer.eventdelegate.GrandFatherListener;
import com.sanri.deginmodel.observer.eventdelegate.Notifier;
import org.junit.Test;

import java.util.Date;

public class EventDelegateTest {

    @Test
    public void testDelegate(){
        //创建一个通知者
       Notifier notifier = new  ChildNotifier();

       //两个观察者
        DadListener dadListener = new DadListener();
        GrandFatherListener grandFatherListener = new GrandFatherListener();

        //委托通知事件
        notifier.addListener(dadListener,"stopWatchingTV",new Date());
        notifier.addListener(grandFatherListener,"stopPlayingGame",new Date());

        //小孩本来了,通知
        notifier.notifyX();
    }
}
