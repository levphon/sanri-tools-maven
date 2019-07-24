package com.sanri.deginmodel.observer;

/**
 * 作者: sanri
 * 时间: 2017/08/03 10:58
 * 功能: 观察者超类
 */
public abstract class Observer {
    protected abstract void subjectUpdate(SubjectEvent event);
}
