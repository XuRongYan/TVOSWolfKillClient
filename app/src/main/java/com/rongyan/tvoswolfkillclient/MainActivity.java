package com.rongyan.tvoswolfkillclient;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rongyan.tvoswolfkillclient.mina.BackService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


public class MainActivity extends AppCompatActivity {
    @BindView(R.id.et_server_ip)
    EditText editText;
    @BindView(R.id.btn_connect)
    Button btnConnected;
    @BindView(R.id.btn_disconnect)
    Button btnDisconnected;
    @BindView(R.id.tv_content)
    TextView textView;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        intent = new Intent(this, BackService.class);

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().unregister(this);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
    }

    @OnClick(R.id.btn_connect)
    public void onClick() {
        binder.setServerIp(editText.getText().toString());
        Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEvent(Object o) {/* Do something */};

//    @Override
//    public void onClick(View v) {
//        binder.setServerIp(editText.getText().toString());
//        Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
//    }
}
