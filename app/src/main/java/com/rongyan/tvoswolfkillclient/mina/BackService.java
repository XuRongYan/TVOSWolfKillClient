package com.rongyan.tvoswolfkillclient.mina;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.rongyant.commonlib.util.LogUtils;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

public class BackService extends Service {
    private static String SERVER_IP = null;
    public static final int SERVER_PORT = 9800;
    //30S后超时
    public static final int IDLE_TIMEOUT = 30;
    //15s发送一次心跳包
    public static final int HEART_BEAT_RATE = 15;
    private NioSocketConnector connector;
    private ConnectFuture connectFuture;
    private  IBinder binder = new LocalBinder();
    public BackService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        connector = new NioSocketConnector();
        connector.getSessionConfig().setReadBufferSize(2048);
        connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, IDLE_TIMEOUT);
        connector.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        KeepAliveMessageFactory heartBeatFactory = new KeepAliveMessageFactoryImpl(); //心跳包收发处理
        KeepAliveRequestTimeoutHandler heartBeatTimeoutHandler = new KeepAliveRequestTimeoutHandlerImpl(); //心跳包超时处理
        KeepAliveFilter heartBeatFilter = new KeepAliveFilter(heartBeatFactory, IdleStatus.BOTH_IDLE, heartBeatTimeoutHandler);
        heartBeatFilter.setForwardEvent(true); //向后传递事件（默认截取空闲事件，这里设置成可以向后传递）
        heartBeatFilter.setRequestInterval(HEART_BEAT_RATE); //设置心跳包频率
        connector.getFilterChain().addLast("heartbeat", heartBeatFilter);
        connector.setHandler(new ClientHandler());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (connectFuture != null) {
            connectFuture.getSession().getCloseFuture().awaitUninterruptibly(); //等待连接断开
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }

    public class LocalBinder extends Binder {
        private static final String TAG = "LocalBinder";
        public void setServerIp(final String ip) {
            SERVER_IP = ip;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    connectFuture = connector.connect(new InetSocketAddress(ip, SERVER_PORT));

                    if (connectFuture.isConnected()) {
                        LogUtils.e(TAG, "setServerIp", "connected server,ip:" + ip);
                    } else {

                        LogUtils.e(TAG, "setServerIp", "connected failed");

                    }

                }
            }).start();


        }

        public void sendMsg(String message) {
            if (connectFuture != null) {
                connectFuture.getSession().write(message);
            }
        }
    }
}
