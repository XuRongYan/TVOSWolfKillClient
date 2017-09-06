package com.rongyan.tvoswolfkillclient.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;

/**
 * 心跳包超时处理类
 * Created by XRY on 2017/8/1.
 */

public class KeepAliveRequestTimeoutHandlerImpl implements KeepAliveRequestTimeoutHandler {
    private static final String TAG = "KeepAliveRequestTimeout";
    @Override
    public void keepAliveRequestTimedOut(KeepAliveFilter keepAliveFilter, IoSession ioSession) throws Exception {
        //LogUtils.e(TAG, "keepAliveRequestTimedOut", ioSession.getRemoteAddress().toString() + "timeout");
    }
}
