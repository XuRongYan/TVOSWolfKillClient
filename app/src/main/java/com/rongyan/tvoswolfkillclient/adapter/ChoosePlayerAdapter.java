package com.rongyan.tvoswolfkillclient.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rongyan.tvoswolfkillclient.R;
import com.rongyan.tvoswolfkillclient.entity.VoteEntity;
import com.rongyant.commonlib.commonadapter.CommonAdapter;
import com.rongyant.commonlib.commonadapter.ViewHolder;
import com.rongyant.commonlib.util.LogUtils;

import java.util.List;

/**
 * Created by XRY on 2017/8/5.
 */

public class ChoosePlayerAdapter extends CommonAdapter<VoteEntity> {

    private static final String TAG = "ChoosePlayerAdapter";

    public ChoosePlayerAdapter(Context context, List<VoteEntity> list) {
        super(context, list);
        //list.get(0).setChecked(true);
    }

    public ChoosePlayerAdapter(Context context, List<VoteEntity> list, RecyclerView recyclerView) {
        super(context, list, recyclerView);
        //list.get(0).setChecked(true);
    }

    @Override
    public int setLayoutId(int position) {
        return R.layout.item_choose_player;
    }

    @Override
    public void onBindVH(final ViewHolder viewHolder, final VoteEntity item, int position) {
        viewHolder.setText(R.id.tv_item_choose_player, (item.getId() + 1) + "");

        if (item.isChecked()) {
            viewHolder.setBackgroundRes(R.id.ll_item_choose_player, R.drawable.bg_accent_stroke);
        } else {
            viewHolder.setBackgroundRes(R.id.ll_item_choose_player, R.color.colorTransparent);
        }
        viewHolder.setOnClickListener(R.id.tv_item_choose_player, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCheck();
                item.setChecked(true);
                notifyDataSetChanged();
            }
        });
    }

    private void initCheck() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setChecked(false);
        }
    }

    public int getCheckedId() {
        int result = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isChecked()) {
                if (result != -1) {
                    LogUtils.e(TAG, "getCheckedId", "multi check");
                    return -1;
                }
                result = list.get(i).getId();
            }
        }
        return result;
    }
}
