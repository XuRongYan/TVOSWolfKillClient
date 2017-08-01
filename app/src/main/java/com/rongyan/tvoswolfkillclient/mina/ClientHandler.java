package com.rongyan.tvoswolfkillclient.mina;

import com.rongyan.model.entity.UserEntity;
import com.rongyan.model.entity.UserEventEntity;
import com.rongyan.model.enums.UserEventType;
import com.rongyan.tvoswolfkillclient.R;
import com.rongyant.commonlib.util.LogUtils;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * Created by XRY on 2017/8/1.
 */

public class ClientHandler extends IoHandlerAdapter {
    private static final String TAG = "ClientHandler";

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        LogUtils.e(TAG, "sessionCreated", "ip:" + session.getRemoteAddress().toString()
                + " session created");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        LogUtils.e(TAG, "sessionClosed", "ip:" + session.getRemoteAddress().toString()
                + " session closed");
        session.write(new UserEventEntity(new UserEntity(1, "test", R.mipmap.ic_launcher), UserEventType.GET, 1));

    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        LogUtils.e(TAG, "sessionClosed", "ip:" + session.getRemoteAddress().toString()
                + " session closed");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
        LogUtils.e(TAG, "sessionIdle", "ip:" + session.getRemoteAddress().toString()
                + " session idled");
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);
        LogUtils.e(TAG, "messageReceived", "ip:" + session.getRemoteAddress().toString()
                + " received" + message);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
        LogUtils.e(TAG, "messageSent", "ip:" + session.getRemoteAddress().toString()
                + " sent" + message);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
        LogUtils.e(TAG, "exceptionCaught", cause.toString());
        cause.printStackTrace();
    }
}
