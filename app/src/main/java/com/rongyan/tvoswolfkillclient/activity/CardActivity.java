package com.rongyan.tvoswolfkillclient.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.rongyan.model.message.ConfirmMessage;
import com.rongyan.tvoswolfkillclient.R;
import com.rongyan.tvoswolfkillclient.UserHolder;
import com.rongyan.tvoswolfkillclient.base.BaseActivity;
import com.rongyan.tvoswolfkillclient.event_message.ReplaceFgmEvent;
import com.rongyan.tvoswolfkillclient.event_message.ShowPopupEvent;
import com.rongyan.tvoswolfkillclient.fragment.ActionFragment;
import com.rongyan.tvoswolfkillclient.fragment.CardFragment;
import com.rongyan.tvoswolfkillclient.popupwindowHelper.PopupWindowUtil;
import com.rongyant.commonlib.util.ActivityUtils;
import com.rongyant.commonlib.util.LogUtils;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

import static com.rongyan.tvoswolfkillclient.fragment.FragmentTagHolder.ACTION_FGM;
import static com.rongyan.tvoswolfkillclient.fragment.FragmentTagHolder.CARD_FGM;

public class CardActivity extends BaseActivity {
    private static final String TAG = "CardActivity";
    public CardFragment fragment;
    public ActionFragment actionFragment;
    private PopupWindowUtil showGoodPopupHelper;

    @Override
    protected int getContentView() {
        return R.layout.activity_card;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        //发送确认帧
        EventBus.getDefault().post(new ConfirmMessage(UserHolder.userEntity.getUserId(), ConfirmMessage.CONFIRM_WATCH_CARD));
        fragment = (CardFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_frame_card);

        ActionFragment actionFragment = ActionFragment.newInstance();

        if (fragment == null) {
            fragment = CardFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.content_frame_card, null);
            //ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), actionFragment, R.id.content_frame_card);
        } else {
            ActivityUtils.showFragment(getSupportFragmentManager(), fragment);
        }
    }


    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEvent(ReplaceFgmEvent event) {
        LogUtils.e(TAG, "onMessageEvent ReplaceFgmEvent", "go " + event.getFgmTag());
        replaceFgm(event.getFgmTag(), event.getIds());
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEvent(ShowPopupEvent event) {
        showPopupWindow(event.getMessage());
    }

    private void showPopupWindow(String message) {
        switch (message) {
            case ShowPopupEvent.SHOW_BAD:
                showBadPop();
                break;
            case ShowPopupEvent.SHOW_GOOD:
                showGoodPop();
                break;
            case ShowPopupEvent.SHOW_CHAMPAIGN:

                break;
        }
    }

    private void showGoodPop() {
        showGoodPopupHelper = new PopupWindowUtil.Builder(this)
                .setView(R.layout.popup_good_or_not)
                .setLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                .setText(R.id.tv_popup_good_or_not, "您验到的是：好人")
                .setOnClickListener(R.id.img_ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showGoodPopupHelper.dismiss();
                    }
                }).build();
        showGoodPopupHelper.show(getLayoutInflater().inflate(getContentView(), null),
                Gravity.NO_GRAVITY, 0, 0);
    }

    private void showBadPop() {
        showGoodPopupHelper = new PopupWindowUtil.Builder(this)
                .setView(R.layout.popup_good_or_not)
                .setLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                .setText(R.id.tv_popup_good_or_not, "您验到的是：坏人")
                .setOnClickListener(R.id.img_ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showGoodPopupHelper.dismiss();
                    }
                }).build();
        showGoodPopupHelper.show(getLayoutInflater().inflate(getContentView(), null),
                Gravity.NO_GRAVITY, 0, 0);
    }

    private void replaceFgm(String tag, int[] ids) {
        switch (tag) {
            case ACTION_FGM:
                Bundle bundle = new Bundle();
                ArrayList<Integer> list = new ArrayList<>();
                for (int i = 0; i < ids.length; i++) {
                    list.add(ids[i]);
                }
                bundle.putIntegerArrayList("players", list);
                actionFragment = (ActionFragment) getSupportFragmentManager().findFragmentByTag(tag);
                if (actionFragment == null) {
                    actionFragment = ActionFragment.newInstance();
                }
                try {
                    actionFragment.setArguments(bundle);
                } catch (Exception e) {
                    actionFragment.setList(list);
                }

                ActivityUtils.replaceFragment(getSupportFragmentManager(),
                        actionFragment,
                        R.id.content_frame_card,
                        ACTION_FGM);
                //actionFragment.setList(list);
                break;
            case CARD_FGM:
                fragment = (CardFragment) getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = CardFragment.newInstance();
                }
                ActivityUtils.replaceFragment(getSupportFragmentManager(),
                        fragment,
                        R.id.content_frame_card,
                        tag);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
