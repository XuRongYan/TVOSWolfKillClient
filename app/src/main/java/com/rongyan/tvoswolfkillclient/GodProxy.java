package com.rongyan.tvoswolfkillclient;

import com.rongyan.model.entity.UserEventEntity;
import com.rongyan.tvoswolfkillclient.mina.ClientHandler;
import com.rongyant.commonlib.util.LogUtils;

import org.apache.mina.core.session.IoSession;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by XRY on 2017/8/2.
 */

public class GodProxy {
    private static GodProxy INSTANCE = null;
    private static final String TAG = "GodProxy";
    private IoSession godSession;
    private GodProxy() {
        godSession = ClientHandler.godSession;
        if (godSession == null) {
            LogUtils.e(TAG, "constructor", "getSessionFailed");
        }
    }

    public static GodProxy getInstance() {
        if (INSTANCE == null) {
            synchronized (GodProxy.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GodProxy();
                }
            }
        }
        return INSTANCE;
    }

    @Subscribe(threadMode = ThreadMode.BackgroundThread)
    public void onMessageEvent(UserEventEntity userEventEntity) {
        godSession.write(userEventEntity);
    }

    public void unRegister() {
        EventBus.getDefault().unregister(this);
    }




}