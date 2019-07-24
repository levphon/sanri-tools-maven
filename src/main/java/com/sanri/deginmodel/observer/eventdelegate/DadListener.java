package com.sanri.deginmodel.observer.eventdelegate;

import java.util.Date;

/**
 * 父亲监听者
 */
public class DadListener {
    public DadListener() {
        System.out.println("watching TV");
    }

    public void stopWatchingTV(Date date) {
        System.out.println("dad stop watching tv" + date);
    }
}
