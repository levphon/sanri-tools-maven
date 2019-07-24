package com.sanri.deginmodel.observer;


/**
 * 作者: sanri
 * 时间: 2017/08/03 11:14
 * 功能: 这也是一个观察者,它认为小孩醒来需要让其活动活动而不是喂它
 */
public class GrandFather extends Observer {

    @Override
    protected void subjectUpdate(SubjectEvent event) {
        if("我醒来了".equals(event.getEventName())){
            Subject source = event.getSource();
            Child child = (Child) source;
            System.out.println("爷爷:"+child.getName()+" 需要活动");
        }
    }
}
