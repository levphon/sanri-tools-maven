package com.sanri.deginmodel.observer;

/**
 * 作者: sanri
 * 时间: 2017/08/03 11:05
 * 功能: 孩子类
 */
public class Child extends  Subject {
    private String name;

    public Child(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * @作者: sanri
     * @时间: 2017/8/3 11:06
     * @功能: 孩子醒来事件
     * @param
     */
    public void wakeup(){
        SubjectEvent event = new SubjectEvent("我醒来了",this);
        this.updateStatus(event);
    }
}
