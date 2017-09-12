package com.rongyan.tvoswolfkillclient.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rongyan.model.entity.JesusEventEntity;
import com.rongyan.model.entity.UserEventEntity;
import com.rongyan.model.enums.JesusEvent;
import com.rongyan.model.enums.UserEventType;
import com.rongyan.model.state.SpeechState;
import com.rongyan.tvoswolfkillclient.R;
import com.rongyan.tvoswolfkillclient.UserHolder;
import com.rongyan.tvoswolfkillclient.activity.CardActivity;
import com.rongyan.tvoswolfkillclient.base.BaseFragment;
import com.rongyan.tvoswolfkillclient.event_message.ShowButton;
import com.rongyant.commonlib.util.ActivityUtils;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by XRY on 2017/8/3.
 */

public class CardFragment extends BaseFragment {
    @BindView(R.id.tv_role_type)
    TextView tvRoleType;
    @BindView(R.id.tv_userid)
    TextView tvUserid;
    @BindView(R.id.tv_speeching)
    TextView tvSpeeching;
    @BindView(R.id.btn_skip)
    Button btnSkip;
    @BindView(R.id.btn_exit_water)
    Button btnReturnWater;
    @BindView(R.id.btn_self_destruction)
    Button btnSelfDestruction;
    @BindView(R.id.iv_identity)
    ImageView ivIdentity;

    private ActionFragment fragment;

    public static CardFragment newInstance() {

        Bundle args = new Bundle();

        CardFragment fragment = new CardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initViews(View rootView) {

        tvRoleType.setText(R.string.string_your_role_is);
        tvUserid.setText((UserHolder.userEntity.getUserId() + 1) + "号");
        switch (UserHolder.userEntity.getRoleType()) {
            case TELLER:
                tvRoleType.setText(tvRoleType.getText() + getString(R.string.string_teller));
                ivIdentity.setImageResource(R.mipmap.img_teller);
                break;
            case WITCH:
                tvRoleType.setText(tvRoleType.getText() + getString(R.string.string_witch));
                ivIdentity.setImageResource(R.mipmap.img_witch);
                break;
            case WOLF:
                tvRoleType.setText(tvRoleType.getText() + getString(R.string.string_wolf));
                ivIdentity.setImageResource(R.mipmap.img_wolf);
                break;
            case HUNTER:
                tvRoleType.setText(tvRoleType.getText() + getString(R.string.string_hunter));
                ivIdentity.setImageResource(R.mipmap.img_hunter);
                break;
            case IDIOT:
                tvRoleType.setText(tvRoleType.getText() + getString(R.string.string_idiot));
                ivIdentity.setImageResource(R.mipmap.img_idiot);
                break;
            case GUARD:
                tvRoleType.setText(tvRoleType.getText() + getString(R.string.string_guard));
                ivIdentity.setImageResource(R.mipmap.img_guard);
                break;
            case VILLAGER:
                tvRoleType.setText(tvRoleType.getText() + getString(R.string.string_villager));
                ivIdentity.setImageResource(R.mipmap.img_villager);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        if (UserHolder.userEntity.getState() instanceof SpeechState) {
            showSpeeching();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_card;
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEvent(JesusEventEntity eventEntity) {
        if (eventEntity.getEvent() == JesusEvent.SPEECH && eventEntity.getTargetId()[0] == UserHolder.userEntity.getUserId()) {
            showSpeeching();
        } else if (eventEntity.getEvent() == JesusEvent.STOP_SPEECH) {
            if (eventEntity.getTargetId() == null || eventEntity.getTargetId()[0] == UserHolder.userEntity.getUserId())
            dismissSpeeching();
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void onMessageEvent(ShowButton msg) {
        switch (msg.getMessage()) {
            case ShowButton.SHOW:
                showReturnWater();
                break;
            case ShowButton.DISMISS:
                dismissReturnWater();
                break;
            case ShowButton.SHOW_SELF_DESTRUCTION:
                showSelfDestruction();
                break;
            case ShowButton.DISMISS_SELF_DESTRUCTION:
                dismissSelfDestruction();
                break;
        }
    }

    private void replaceFgm() {
//        if (fragment == null) {
//            fragment = ActionFragment.newInstance();
//        }
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.content_frame_card, fragment);
//        transaction.commit();
        ActivityUtils.replaceFragment(getActivity().getSupportFragmentManager(), ((CardActivity) getActivity()).actionFragment, R.id.content_frame_card, null);
    }

    public void showReturnWater() {
        btnReturnWater.setVisibility(View.VISIBLE);
    }

    public void dismissReturnWater() {
        btnReturnWater.setVisibility(View.GONE);
    }

    private void showSpeeching() {
        btnSkip.setVisibility(View.VISIBLE);
        tvSpeeching.setVisibility(View.VISIBLE);
    }

    private void dismissSpeeching() {
        btnSkip.setVisibility(View.GONE);
        tvSpeeching.setVisibility(View.GONE);
    }

    private void showSelfDestruction() {
        btnSelfDestruction.setVisibility(View.VISIBLE);
    }

    private void dismissSelfDestruction() {
        btnSelfDestruction.setVisibility(View.GONE);
    }

    @OnClick({R.id.btn_skip, R.id.btn_exit_water, R.id.btn_self_destruction})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_skip:
                dismissSpeeching();
                EventBus.getDefault().post(new UserEventEntity(UserHolder.userEntity, UserEventType.END_SPEECH, 0));
                break;
            case R.id.btn_exit_water:
                EventBus.getDefault().post(new UserEventEntity(UserHolder.userEntity, UserEventType.RETURN_WATER, -1));
                dismissReturnWater();
                break;
            case R.id.btn_self_destruction:
                EventBus.getDefault().post(new UserEventEntity(UserHolder.userEntity, UserEventType.SELF_DESTRUCTION, -1));
                dismissSelfDestruction();
                break;
        }

    }
}
