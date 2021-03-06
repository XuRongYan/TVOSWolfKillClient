package com.rongyan.tvoswolfkillclient.mina;

import com.rongyan.model.entity.JesusEventEntity;
import com.rongyan.model.entity.UserEntity;
import com.rongyan.model.message.ConfirmMessage;
import com.rongyan.tvoswolfkillclient.GodProxy;
import com.rongyan.tvoswolfkillclient.UserHolder;
import com.rongyan.tvoswolfkillclient.activity.CardActivity;
import com.rongyan.tvoswolfkillclient.event_message.GoActivityEvent;
import com.rongyant.commonlib.util.LogUtils;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import de.greenrobot.event.EventBus;

/**
 * Created by XRY on 2017/8/1.
 */

public class ClientHandler extends IoHandlerAdapter {
    private static final String TAG = "ClientHandler";
    public static IoSession godSession;

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        LogUtils.e(TAG, "sessionCreated", "ip:" + session.getRemoteAddress().toString()
                + " session created");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        LogUtils.e(TAG, "sessionOpened", "ip:" + session.getRemoteAddress().toString()
                + " session closed");
        //GodProxy.getInstance();
        session.write(UserHolder.userEntity);

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
        godSession = session;
        GodProxy.getInstance();
        if (message instanceof JesusEventEntity) {
            EventBus.getDefault().post((message));
            //发送确认信号表示已经收到命令
            session.write(new ConfirmMessage(UserHolder.userEntity.getUserId(),
                    ConfirmMessage.CONFIRM));
        }
        //服务端在开启新游戏的时候直接发送UserEntity就可以了
        if (message instanceof UserEntity) {
            UserHolder.userEntity = (UserEntity) message;
            EventBus.getDefault().post(new GoActivityEvent(CardActivity.class));
        }
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
