package com.rongyan.tvoswolfkillclient.base;

import android.app.Application;

import com.rongyan.tvoswolfkillclient.GodProxy;


/**
 *
 * Created by XRY on 2017/4/14.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        GodProxy.getInstance().unRegister();
    }
}
