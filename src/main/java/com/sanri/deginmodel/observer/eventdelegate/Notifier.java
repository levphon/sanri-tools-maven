package com.sanri.deginmodel.observer.eventdelegate;

/**
 * 抽象的通知者,比如这里的父亲,祖父的抽象
 */
public abstract class Notifier {
    private EventHandler eventHandler = new EventHandler();
    
    public EventHandler getEventHandler()
    {
        return eventHandler;
    }
    
    public void setEventHandler(EventHandler eventHandler)
    {
        this.eventHandler = eventHandler;
    }
    
    public abstract void addListener(Object object,String methodName, Object...args);
    
    public abstract void notifyX();

}