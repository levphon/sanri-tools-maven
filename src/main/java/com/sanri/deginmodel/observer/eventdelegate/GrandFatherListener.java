package com.sanri.deginmodel.observer.eventdelegate;

import java.util.Date;

public class GrandFatherListener {
    public GrandFatherListener() {
        System.out.println("playing");
    }

    public void stopPlayingGame(Date date) {
        System.out.println("GrandFather stop playing" + date);
    }
}
