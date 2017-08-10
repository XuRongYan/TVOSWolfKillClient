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

public class HunterOpenEyesState implements BaseJesusState {
    private static final String TAG = "HunterOpenEyesState";
    @Override
    public void send(int...id) {
        Log.e(TAG, "猎人请睁眼");
        EventBus.getDefault().post(new JesusEventEntity(RoleType.HUNTER, JesusEvent.OPEN_EYES));
    }

    @Override
    public BaseJesusState next() {
        return new HunterShootState();
    }
}
