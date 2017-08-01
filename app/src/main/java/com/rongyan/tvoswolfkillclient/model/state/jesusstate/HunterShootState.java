package com.rongyan.tvoswolfkillclient.model.state.jesusstate;


import com.rongyan.tvoswolfkillclient.model.abstractinterface.BaseJesusState;
import com.rongyan.tvoswolfkillclient.model.entity.JesusEventEntity;
import com.rongyan.tvoswolfkillclient.model.enums.JesusEvent;
import com.rongyan.tvoswolfkillclient.model.enums.RoleType;

import de.greenrobot.event.EventBus;

/**
 * Created by XRY on 2017/7/28.
 */

public class HunterShootState implements BaseJesusState {
    @Override
    public void send(int id) {
        EventBus.getDefault().post(new JesusEventEntity(RoleType.HUNTER, JesusEvent.SHOOT_STATE, id));
    }
}
