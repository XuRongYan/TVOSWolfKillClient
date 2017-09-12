package com.rongyan.tvoswolfkillclient.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rongyan.tvoswolfkillclient.MainActivity;
import com.rongyan.tvoswolfkillclient.R;
import com.rongyan.tvoswolfkillclient.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class StartGameActivity extends BaseActivity {
    @BindView(R.id.btn_start_game)
    Button button;

    @Override
    protected int getContentView() {
        return R.layout.activity_start_game;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @OnClick({R.id.btn_start_game})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_game:
                goActivity(MainActivity.class);
                break;
        }
    }
}
