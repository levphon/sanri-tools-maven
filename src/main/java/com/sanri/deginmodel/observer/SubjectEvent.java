package com.sanri.deginmodel.observer;

/**
 * 作者: sanri
 * 时间: 2017/08/03 11:00
 * 功能: 被观察的主题的事件
 */
public class SubjectEvent {
    private Subject source;
    private String eventName;

    public Subject getSource() {
        return source;
    }

    public void setSource(Subject source) {
        this.source = source;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public SubjectEvent(String eventName,Subject source){
        this.eventName = eventName;
        this.source = source;
    }
}
