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

public class WolfCloseEyes implements BaseJesusState {
    private static final String TAG = "WolfCloseEyes";
    @Override
    public void send(int id) {
        Log.e(TAG, "狼人请闭眼");
        EventBus.getDefault().post(new JesusEventEntity(RoleType.WOLF, JesusEvent.CLOSE_EYES));
    }

    @Override
    public BaseJesusState next() {
        return new GuardOpenEyesState();
    }
}
