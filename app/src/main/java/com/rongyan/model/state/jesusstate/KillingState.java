package com.rongyan.model.state.jesusstate;


import android.util.Log;

import com.rongyan.model.abstractinterface.BaseJesusState;
import com.rongyan.model.entity.JesusEventEntity;
import com.rongyan.model.enums.JesusEvent;
import com.rongyan.model.enums.RoleType;

import de.greenrobot.event.EventBus;

/**
 * Created by XRY on 2017/7/28.
 */

public class KillingState implements BaseJesusState {
    private static final String TAG = "KillingState";
    @Override
    public void send(int id) {
        Log.e(TAG, "狼人请杀人");
        EventBus.getDefault().post(new JesusEventEntity(RoleType.WOLF, JesusEvent.KILL));
    }

    @Override
    public BaseJesusState next() {
        return new WolfCloseEyes();
    }


}
