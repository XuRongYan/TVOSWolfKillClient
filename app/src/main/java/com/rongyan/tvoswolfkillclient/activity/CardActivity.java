package com.rongyan.tvoswolfkillclient.activity;

import android.os.Bundle;

import com.rongyan.tvoswolfkillclient.R;
import com.rongyan.tvoswolfkillclient.base.BaseActivity;
import com.rongyan.tvoswolfkillclient.fragment.ActionFragment;
import com.rongyan.tvoswolfkillclient.fragment.CardFragment;
import com.rongyant.commonlib.util.ActivityUtils;

public class CardActivity extends BaseActivity {
    public CardFragment fragment;
    public ActionFragment actionFragment = ActionFragment.newInstance();
    @Override
    protected int getContentView() {
        return R.layout.activity_card;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        fragment = (CardFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_frame_card);
        ActionFragment actionFragment = ActionFragment.newInstance();

        if (fragment == null) {
            fragment = CardFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.content_frame_card);
            //ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), actionFragment, R.id.content_frame_card);
        } else {
            ActivityUtils.showFragment(getSupportFragmentManager(), fragment);
        }
    }
}
