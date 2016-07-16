package com.bigteam.kosta.bigteamclnt.app;

import android.app.Application;
import android.content.pm.PackageInfo;

/**
 * Created by kosta on 2016-07-12.
 */
public class AppData extends Application {

    PackageInfo packageInfo;

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }
}
