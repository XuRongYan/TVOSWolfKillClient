package com.rongyan.tvoswolfkillclient.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.rongyan.tvoswolfkillclient.R;
import com.rongyan.tvoswolfkillclient.UserHolder;
import com.rongyan.tvoswolfkillclient.adapter.ChoosePlayerAdapter;
import com.rongyan.tvoswolfkillclient.base.BaseFragment;
import com.rongyan.tvoswolfkillclient.entity.VoteEntity;
import com.rongyan.tvoswolfkillclient.event_message.ReplaceFgmEvent;
import com.rongyant.commonlib.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by XRY on 2017/8/3.
 */

public class ActionFragment extends BaseFragment {
    private static final String TAG = "ActionFragment";
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.recycler_action)
    RecyclerView recyclerView;
    private ArrayList<Integer> players;
    private ChoosePlayerAdapter choosePlayerAdapter;
    private List<VoteEntity> voteList;

    public static ActionFragment newInstance() {


        Bundle args = new Bundle();

        ActionFragment fragment = new ActionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViews(View rootView) {
        LogUtils.e(TAG, "initViews", "im start");

        voteList = new ArrayList<>();
        players = getArguments().getIntegerArrayList("players");
        for (int i = 0; i < players.size(); i++) {
            voteList.add(new VoteEntity(players.get(i), false));
        }
        initRecyclerView();


    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_action;
    }

    private void initRecyclerView() {
        if (voteList == null) {
            LogUtils.e(TAG, "initRecyclerView", "fail to init recyclerView,because data is null");
            return;
        }

        choosePlayerAdapter = new ChoosePlayerAdapter(getActivity(),
                voteList,
                recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(choosePlayerAdapter);
    }

    @OnClick({R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                onSubmitClick();
                break;
        }
    }

    private void onSubmitClick() {
        int checkedId = choosePlayerAdapter.getCheckedId();
        if (checkedId != -1) {
            UserHolder.userEntity.send(checkedId);
        }
        EventBus.getDefault().post(new ReplaceFgmEvent(FragmentTagHolder.CARD_FGM, 0));
    }

    public void setList(ArrayList<Integer> list) {
        List<VoteEntity> voteList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                voteList.add(new VoteEntity(list.get(i), true));
            } else {
                voteList.add(new VoteEntity(list.get(i), false));
            }
        }
        choosePlayerAdapter.removeAll();
        choosePlayerAdapter.addListAtStart(voteList);
    }
}
