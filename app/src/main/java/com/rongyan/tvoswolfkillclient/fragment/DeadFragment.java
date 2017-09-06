package com.rongyan.tvoswolfkillclient.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.rongyan.tvoswolfkillclient.R;
import com.rongyan.tvoswolfkillclient.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeadFragment extends BaseFragment {

    public static DeadFragment newInstance() {

        Bundle args = new Bundle();

        DeadFragment fragment = new DeadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViews(View rootView) {

    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_dead;
    }

}
