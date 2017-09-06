package com.rongyan.tvoswolfkillclient;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.rongyan.model.entity.UserEntity;
import com.rongyan.tvoswolfkillclient.base.BaseActivity;
import com.rongyan.tvoswolfkillclient.base.PermissionListener;
import com.rongyan.tvoswolfkillclient.event_message.GoActivityEvent;
import com.rongyan.tvoswolfkillclient.event_message.ShowPopupEvent;
import com.rongyan.tvoswolfkillclient.mina.BackService;
import com.rongyant.commonlib.util.BitmapUtil;
import com.rongyant.commonlib.util.CameraUtil;
import com.rongyant.commonlib.util.LogUtils;
import com.rongyant.commonlib.util.NetWorkUtil;

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
    @BindView(R.id.img_main_head_img)
    ImageView imageView;

    private String[] permissions = {"android.permission.CAMERA"};

    private Intent intent;
    private Uri mUri;
    public BackService.LocalBinder binder;
    public ServiceConnection connection = new ServiceConnection() {


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
        LogUtils.e(TAG_LOG, "initViews", NetWorkUtil.getHostIp());
        requestRuntimePermission(permissions, new PermissionListener() {
            @Override
            public void onGranted() {
               mUri =  CameraUtil.openCamera(MainActivity.this, getApplication());
            }

            @Override
            public void onDenied(String[] deniedPermissions) {
                finish();
            }
        });
        editText.setText("192.168.2.218");

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
        //finish();
    }


    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEvent(ShowPopupEvent event) {

    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEvent(String event) {
        if (event.equals("该IP已被占用")) {
            Toast.makeText(mContext, event, Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void replaceFgm(String tag) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CameraUtil.PHOTO_REQUEST_CAREMA:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(bitmap);
                        UserHolder.userEntity = new UserEntity("test", BitmapUtil.bitmap2Byte(bitmap));
                    } else {
                        Bitmap bitmap = BitmapFactory.decodeFile(mUri.getPath());

                        imageView.setImageBitmap(bitmap);

                        //UserHolder.userEntity = new UserEntity("test", BitmapUtil.bitmap2Byte(bitmap));
                    }
                }
                break;
        }
    }

    //    @Override
//    public void onClick(View v) {
//        binder.setServerIp(editText.getText().toString());
//        Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
//    }
}
