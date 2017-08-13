package com.rongyan.tvoswolfkillclient.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rongyan.tvoswolfkillclient.R;
import com.rongyan.tvoswolfkillclient.UserHolder;
import com.rongyan.tvoswolfkillclient.activity.CardActivity;
import com.rongyan.tvoswolfkillclient.base.BaseFragment;
import com.rongyant.commonlib.util.ActivityUtils;

import butterknife.BindView;

/**
 * Created by XRY on 2017/8/3.
 */

public class CardFragment extends BaseFragment {
    @BindView(R.id.tv_role_type)
    TextView tvRoleType;
    @BindView(R.id.tv_userid)
    TextView tvUserid;

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
        tvUserid.setText(UserHolder.userEntity.getUserId());
        switch (UserHolder.userEntity.getRoleType()) {
            case TELLER:
                tvRoleType.setText(tvRoleType.getText() + getString(R.string.string_teller));
                break;
            case WITCH:
                tvRoleType.setText(tvRoleType.getText() + getString(R.string.string_witch));
                break;
            case WOLF:
                tvRoleType.setText(tvRoleType.getText() + getString(R.string.string_wolf));
                break;
            case HUNTER:
                tvRoleType.setText(tvRoleType.getText() + getString(R.string.string_hunter));
                break;
            case IDIOT:
                tvRoleType.setText(tvRoleType.getText() + getString(R.string.string_idiot));
                break;
            case GUARD:
                tvRoleType.setText(tvRoleType.getText() + getString(R.string.string_guard));
                break;
            case VILLAGER:
                tvRoleType.setText(tvRoleType.getText() + getString(R.string.string_villager));
                break;
        }
    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_card;
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
}
