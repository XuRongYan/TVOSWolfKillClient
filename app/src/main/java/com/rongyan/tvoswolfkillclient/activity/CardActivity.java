package com.rongyan.tvoswolfkillclient.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.rongyan.model.entity.UserEventEntity;
import com.rongyan.model.enums.UserEventType;
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
import java.util.Arrays;

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
    private PopupWindowUtil showWitchPop;
    private PopupWindowUtil showGetShootState;
    private PopupWindowUtil showChampaign;
    private PopupWindowUtil showShoot;

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
        if (showWitchPop != null) {
            showWitchPop.dismiss();
        }
        if (showGetShootState != null) {
            showGetShootState.dismiss();
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEvent(ShowPopupEvent event) {
        showPopupWindow(event);
    }

    private void showPopupWindow(ShowPopupEvent event) {
        switch (event.getMessage()) {
            case ShowPopupEvent.SHOW_BAD:
                showBadPop();
                break;
            case ShowPopupEvent.SHOW_GOOD:
                showGoodPop();
                break;
            case ShowPopupEvent.WITCH_CHOOSE:
                int[] targetId = event.getTargetId();
                showWitch(targetId);
                break;
            case ShowPopupEvent.HUNTER_GET_SHOOT_STATE:
                showGetShootState(event.getTargetId());
                break;
            case ShowPopupEvent.SHOW_CHAMPAIGN:
                showCampaign();
                break;
            case ShowPopupEvent.HUNTER_SOOT_OR_NOT:
                showShoot(event.getTargetId());
                break;
        }
    }

    //TODO 还没有测试过，应该添加一个不开枪的事件，要不就是每个人死了都有一个固定时间判断开不开枪
    private void showShoot(final int[] ids) {
        showShoot = new PopupWindowUtil.Builder(this)
                .setView(R.layout.popup_shoot_or_not)
                .setLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                .setOnClickListener(R.id.btn_popup_shoot, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new ReplaceFgmEvent(ACTION_FGM, ids));
                        showShoot.dismiss();
                    }
                })
                .setOnClickListener(R.id.btn_popup_not_shoot, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showShoot.dismiss();
                    }
                })
                .build();
        showShoot.show(getLayoutInflater().inflate(getContentView(), null), Gravity.NO_GRAVITY, 0, 0);
    }

    private void showCampaign() {
        showChampaign = new PopupWindowUtil.Builder(this)
                .setView(R.layout.popup_campaign_or_not)
                .setLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                .setOnClickListener(R.id.btn_popup_campaign, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserHolder.userEntity.send(1);
                        showChampaign.dismiss();
                    }
                })
                .setOnClickListener(R.id.btn_popup_not_campaign, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserHolder.userEntity.send(0);
                        showChampaign.dismiss();
                    }
                })
                .build();
        showChampaign.show(getLayoutInflater().inflate(getContentView(), null),
                Gravity.NO_GRAVITY, 0, 0);
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

    private void showWitch(int[] targetId) {
        WitchActionListener saveAction = new WitchActionListener(targetId);
        showWitchPop = new PopupWindowUtil.Builder(this)
                .setView(R.layout.popup_witch_action)
                .setLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                .setText(R.id.tv_popup_witch, targetId[0] != -1 ? "今晚" + targetId[0] + "号玩家被杀，请选择您的行动" : "今晚是平安夜")
                .setClickable(R.id.btn_popup_witch_save, (targetId[1] & 0x01) == 0x01)
                .setClickable(R.id.btn_popup_witch_poison, (targetId[1] & 0x10) == 0x10)
                .setVisible(R.id.btn_popup_witch_do_nothing, targetId[0] != -1)
                .setVisible(R.id.btn_popup_witch_poison, targetId[0] != -1)
                .setVisible(R.id.btn_popup_witch_save, targetId[0] != -1)
                .setOnClickListener(R.id.btn_popup_witch_do_nothing, saveAction)
                .setOnClickListener(R.id.btn_popup_witch_save, saveAction)
                .setOnClickListener(R.id.btn_popup_witch_poison, saveAction)
                .build();
        showWitchPop.show(getLayoutInflater().inflate(getContentView(), null),
                Gravity.NO_GRAVITY, 0, 0);
    }

    private void showGetShootState(int[] targetId) {
        showGetShootState = new PopupWindowUtil.Builder(this)
                .setView(R.layout.popup_get_shoot_state)
                .setLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                .setText(R.id.tv_popup_get_shoot_state, targetId[0] == 0 ? "不能开枪" : "可以开枪")
                .build();
        showGetShootState.show(getLayoutInflater().inflate(getContentView(), null),
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

    private class WitchActionListener implements View.OnClickListener {
        int[] targetId;

        public WitchActionListener(int[] targetId) {
            this.targetId = targetId;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_popup_witch_do_nothing:
                    //just do nothing
                    break;
                case R.id.btn_popup_witch_save:
                    EventBus.getDefault().post(new UserEventEntity(UserHolder.userEntity,
                            UserEventType.SAVE,
                            targetId[0]));
                    break;
                case R.id.btn_popup_witch_poison:
                    EventBus.getDefault().post(new ReplaceFgmEvent(ACTION_FGM, Arrays.copyOfRange(targetId, 2, targetId.length)));
                    break;
            }
        }
    }
}
