package com.sanri.deginmodel.observer.eventdelegate;

/**
 * 孩子通知者
 */
public class ChildNotifier extends Notifier {
    @Override
    public void addListener(Object object, String methodName, Object... args) {
        getEventHandler().addEvent(object,methodName,args);
    }

    @Override
    public void notifyX() {
        try {
            getEventHandler().notifyX();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
