package com.rongyan.tvoswolfkillclient.event_message;

import android.app.Activity;

/**
 * Created by XRY on 2017/8/3.
 */

public class GoActivityEvent {
    private Class<? extends Activity> cls;

    public GoActivityEvent(Class<? extends Activity> cls) {
        this.cls = cls;
    }

    public Class<? extends Activity> getCls() {
        return cls;
    }

    public void setCls(Class<? extends Activity> cls) {
        this.cls = cls;
    }
}
