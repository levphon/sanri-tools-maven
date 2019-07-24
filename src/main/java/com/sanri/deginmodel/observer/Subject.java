package com.sanri.deginmodel.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: sanri
 * 时间: 2017/08/03 10:57
 * 功能: 抽象主题类,被观察者的超类
 */
public class Subject {
    protected List<Observer> observers = new ArrayList<Observer>();


    /**
     * @作者: sanri
     * @时间: 2017/8/3 10:59
     * @功能: 注册观察者
     * @param  observer
     */
    public void registerObserver(Observer observer){
        //判断是否已经注册 TODO
        observers.add(observer);
    }

    /**
     * @作者: sanri
     * @时间: 2017/8/3 11:04
     * @功能: 主题状态更新,通知所有的观察者
     * @param event
     */
    protected  void updateStatus(SubjectEvent event){
        for (Observer observer : observers) {
            observer.subjectUpdate(event);
        }
    }
}
