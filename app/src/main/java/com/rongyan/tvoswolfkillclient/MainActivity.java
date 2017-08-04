package com.rongyan.tvoswolfkillclient;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rongyan.tvoswolfkillclient.base.BaseActivity;
import com.rongyan.tvoswolfkillclient.event_message.GoActivityEvent;
import com.rongyan.tvoswolfkillclient.mina.BackService;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


public class MainActivity extends BaseActivity {
    @BindView(R.id.et_server_ip)
    EditText editText;
    @BindView(R.id.btn_connect)
    Button btnConnected;
    @BindView(R.id.btn_disconnect)
    Button btnDisconnected;

    private Intent intent;
    public BackService.LocalBinder binder;
    private ServiceConnection connection = new ServiceConnection() {


        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (BackService.LocalBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder = null;
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        //EventBus.getDefault().register(this);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //EventBus.getDefault().unregister(this);
        unbindService(connection);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        intent = new Intent(this, BackService.class);

    }

    @OnClick({R.id.btn_connect})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_connect:
                binder.setServerIp(editText.getText().toString());
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEvent(GoActivityEvent event) {
        goActivity(event.getCls());
    }



//    @Override
//    public void onClick(View v) {
//        binder.setServerIp(editText.getText().toString());
//        Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
//    }
}
