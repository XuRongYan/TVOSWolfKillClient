package com.rongyan.tvoswolfkillclient.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.rongyan.model.entity.UserEventEntity;
import com.rongyan.model.enums.UserEventType;
import com.rongyan.model.message.ConfirmMessage;
import com.rongyan.tvoswolfkillclient.ClientManager;
import com.rongyan.tvoswolfkillclient.R;
import com.rongyan.tvoswolfkillclient.UserHolder;
import com.rongyan.tvoswolfkillclient.base.BaseActivity;
import com.rongyan.tvoswolfkillclient.base.BaseAppManager;
import com.rongyan.tvoswolfkillclient.event_message.ReplaceFgmEvent;
import com.rongyan.tvoswolfkillclient.event_message.ShowButton;
import com.rongyan.tvoswolfkillclient.event_message.ShowPopupEvent;
import com.rongyan.tvoswolfkillclient.fragment.ActionFragment;
import com.rongyan.tvoswolfkillclient.fragment.CardFragment;
import com.rongyan.tvoswolfkillclient.fragment.FragmentTagHolder;
import com.rongyan.tvoswolfkillclient.popupwindowHelper.PopupWindowUtil;
import com.rongyant.commonlib.util.ActivityUtils;
import com.rongyant.commonlib.util.LogUtils;
import com.rongyant.commonlib.util.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

import static com.rongyan.tvoswolfkillclient.fragment.FragmentTagHolder.ACTION_FGM;
import static com.rongyan.tvoswolfkillclient.fragment.FragmentTagHolder.CARD_FGM;

public class CardActivity extends BaseActivity {
    private static final String TAG = "CardActivity";
    public static final int CHIEF_LEFT = 0x00; //警左
    public static final int CHIEF_RIGHT = 0x01; //警右
    public static final int DEAD_LEFT = 0x10; //死左
    public static final int DEAD_RIGHT = 0x11; //死右
    private long exitTime = 0;
    public CardFragment fragment;
    public ActionFragment actionFragment;
    private PopupWindowUtil showGoodPopupHelper;
    private PopupWindowUtil showWitchPop;
    private PopupWindowUtil showGetShootState;
    private PopupWindowUtil showChampaign;
    private PopupWindowUtil showShoot;
    private PopupWindowUtil showChief;
    private PopupWindowUtil showChooseSequence;
    private PopupWindowUtil showDead;
    private PopupWindowUtil showGiveChief;

    @Override
    protected int getContentView() {
        return R.layout.activity_card;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        EventBus.getDefault().post("finish");
        //发送确认帧
        EventBus.getDefault().post(new ConfirmMessage(UserHolder.userEntity.getUserId(), ConfirmMessage.CONFIRM_WATCH_CARD));
        fragment = (CardFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_frame_card);

        ActionFragment actionFragment = ActionFragment.newInstance();
        if (savedInstanceState != null) {
            fragment = (CardFragment) getSupportFragmentManager().findFragmentByTag(FragmentTagHolder.CARD_FGM);
            actionFragment = (ActionFragment) getSupportFragmentManager().findFragmentByTag(FragmentTagHolder.ACTION_FGM);
        }
        if (fragment == null) {
            fragment = CardFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.content_frame_card, null);
            //ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), actionFragment, R.id.content_frame_card);
        } else {
            ActivityUtils.showFragment(getSupportFragmentManager(), fragment);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEvent(ReplaceFgmEvent event) {
        LogUtils.e(TAG, "onMessageEvent ReplaceFgmEvent", "go " + event.getFgmTag());
        dismissAllPopup();
        replaceFgm(event.getFgmTag(), event.getIds());
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEvent(ShowPopupEvent event) {
        dismissAllPopup();
        showPopupWindow(event);
    }

    private void dismissAllPopup() {
        if (showWitchPop != null) {
            showWitchPop.dismiss();
        }
        if (showGetShootState != null) {
            showGetShootState.dismiss();
        }
        if (showChief != null) {
            showChief.dismiss();
        }
        if (showShoot != null) {
            showShoot.dismiss();
        }
        if (showGoodPopupHelper != null) {
            showGoodPopupHelper.dismiss();
        }
        if (showChampaign != null) {
            showChampaign.dismiss();
        }
        if (showChooseSequence != null) {
            showChooseSequence.dismiss();
        }
        if (showChief != null) {
            showChief.dismiss();
        }
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
            case ShowPopupEvent.SHOW_CHIEF:
                showChief();
                break;
            case ShowPopupEvent.CHOOSE_SEQUENCE:
                showChooseSequence(event.getTargetId());
                break;
            case ShowPopupEvent.DEAD:
                showDead();
                break;
            case ShowPopupEvent.GIVE_CHIEF:
                showGiveChief(event.getTargetId());
                break;
        }
    }

    private void showGiveChief(int...ids) {
        GiveChiefAction giveChiefAction = new GiveChiefAction(ids);
        showGiveChief = new PopupWindowUtil.Builder(this)
                .setView(R.layout.popup_give_chief)
                .setLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                .setOnClickListener(R.id.btn_popup_give_chief_yes, giveChiefAction)
                .setOnClickListener(R.id.btn_popup_give_chief_no, giveChiefAction)
                .build();

        showChief.show(getLayoutInflater().inflate(getContentView(), null),
                Gravity.NO_GRAVITY, 0, 0);

    }

    private void showDead() {
        showDead = new PopupWindowUtil.Builder(this)
                .setView(R.layout.popup_dead)
                .setLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                .build();
        showDead.show(getLayoutInflater().inflate(getContentView(), null),
                Gravity.NO_GRAVITY, 0, 0);
    }

    private void showChooseSequence(int[] targetId) {
        int i = targetId[1];
        PopupWindowUtil.Builder builder = new PopupWindowUtil.Builder(this)
                .setView(R.layout.popup_choose_speech_sequence)
                .setLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (i == ClientManager.SINGLE_DEATH) {
            //单死死左死右
            showChooseSequence = builder.setText(R.id.tv_popup_choose_speech_sequence, "请选择死左死右发言")
                    .setText(R.id.btn_popup_choose_speech_sequence_clockwise, "死左发言")
                    .setText(R.id.btn_popup_choose_speech_sequence_anti_clockwise, "死右发言")
                    .setOnClickListener(R.id.btn_popup_choose_speech_sequence_clockwise, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          EventBus.getDefault().post(new UserEventEntity(UserHolder.userEntity,
                                  UserEventType.CHOOSE_SEQUENCE, DEAD_LEFT));
                            showChooseSequence.dismiss();

                        }
                    })
                    .setOnClickListener(R.id.btn_popup_choose_speech_sequence_anti_clockwise, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EventBus.getDefault().post(new UserEventEntity(UserHolder.userEntity,
                                    UserEventType.CHOOSE_SEQUENCE, DEAD_RIGHT));
                            showChooseSequence.dismiss();
                        }
                    })
                    .build();
        } else {
            //双死或者没死警左警右
            showChooseSequence = builder.setText(R.id.tv_popup_choose_speech_sequence, "请选择警左警右发言")
                    .setText(R.id.btn_popup_choose_speech_sequence_clockwise, "警左发言")
                    .setText(R.id.btn_popup_choose_speech_sequence_anti_clockwise, "警右发言")
                    .setOnClickListener(R.id.btn_popup_choose_speech_sequence_clockwise, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EventBus.getDefault().post(new UserEventEntity(UserHolder.userEntity,
                                    UserEventType.CHOOSE_SEQUENCE, CHIEF_LEFT));
                            showChooseSequence.dismiss();
                        }
                    })
                    .setOnClickListener(R.id.btn_popup_choose_speech_sequence_anti_clockwise, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EventBus.getDefault().post(new UserEventEntity(UserHolder.userEntity,
                                    UserEventType.CHOOSE_SEQUENCE, CHIEF_RIGHT));
                            showChooseSequence.dismiss();
                        }
                    })
                    .build();
        }

        showChooseSequence.show(getLayoutInflater().inflate(getContentView(), null),
                Gravity.NO_GRAVITY, 0, 0);
    }

    private void showChief() {
        showChief = new PopupWindowUtil.Builder(this)
                .setView(R.layout.popup_show_chief)
                .setLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                .setOnClickListener(R.id.ll_popup_show_chief_background, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showChief.dismiss();
                    }
                })
                .build();
        showChief.show(getLayoutInflater().inflate(getContentView(), null),
                Gravity.NO_GRAVITY, 0, 0);
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
                        EventBus.getDefault().post(new ShowButton(ShowButton.SHOW));
                        UserHolder.userEntity.setChampaign(true);
                    }
                })
                .setOnClickListener(R.id.btn_popup_not_campaign, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserHolder.userEntity.send(0);
                        showChampaign.dismiss();
                        UserHolder.userEntity.setChampaign(false);
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

    /**
     * 按两次BACK键退出程序
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                ToastUtils.showShort(this, "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                BaseAppManager.getInstance().clearAll();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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

    private class GiveChiefAction implements View.OnClickListener {
        final int[] targetId;

        private GiveChiefAction(int[] targetId) {
            this.targetId = targetId;
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_popup_give_chief_yes:
                    EventBus.getDefault().post(new ReplaceFgmEvent(ACTION_FGM, targetId));

                    break;
                case R.id.btn_popup_give_chief_no:
                    EventBus.getDefault().post(new UserEventEntity(UserHolder.userEntity, UserEventType.NOT_GIVE_CHIEF, -1));

                    break;
            }
            showChief.dismiss();
        }
    }
}
