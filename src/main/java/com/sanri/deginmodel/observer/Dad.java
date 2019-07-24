package com.sanri.deginmodel.observer;

/**
 * 作者: sanri
 * 时间: 2017/08/03 11:07
 * 功能: 父亲类
 */
public class Dad extends Observer {

    @Override
    protected void subjectUpdate(SubjectEvent event) {
        String eventName = event.getEventName();
        if("我醒来了".equals(eventName)){
            feed((Child)event.getSource());
        }
    }

    public void feed(Child child){
        System.out.println("父亲:需要喂小孩了,这个小孩:"+child.getName());
    }
}
