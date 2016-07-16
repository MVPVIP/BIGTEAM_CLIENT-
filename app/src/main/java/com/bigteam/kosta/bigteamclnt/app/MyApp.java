package com.bigteam.kosta.bigteamclnt.app;


import android.app.Application;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

/**
 * Created by kosta on 2016-07-15.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //CustomActivityOnCrash 설치
        CustomActivityOnCrash.install(this);
    }
}
