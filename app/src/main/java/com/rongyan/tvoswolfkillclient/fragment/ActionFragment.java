package com.rongyan.tvoswolfkillclient.fragment;

import android.os.Bundle;
import android.view.View;

import com.rongyan.tvoswolfkillclient.R;
import com.rongyan.tvoswolfkillclient.activity.CardActivity;
import com.rongyan.tvoswolfkillclient.base.BaseFragment;
import com.rongyant.commonlib.util.ActivityUtils;

/**
 * Created by XRY on 2017/8/3.
 */

public class ActionFragment extends BaseFragment {

    public static ActionFragment newInstance() {


        Bundle args = new Bundle();

        ActionFragment fragment = new ActionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViews(View rootView) {

    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_action;
    }



    private void replaceFgm() {
//        CardFragment fragment = ((CardActivity) getActivity()).fragment;
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.content_frame_card, fragment);
//        transaction.commit();
        ActivityUtils.replaceFragment(getActivity().getSupportFragmentManager(), ((CardActivity) getActivity()).fragment, R.id.content_frame_card, null);
    }
}
