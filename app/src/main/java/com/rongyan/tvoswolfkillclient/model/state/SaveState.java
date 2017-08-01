package com.rongyan.tvoswolfkillclient.model.state;


import com.rongyan.tvoswolfkillclient.model.abstractinterface.BaseState;
import com.rongyan.tvoswolfkillclient.model.entity.UserEntity;
import com.rongyan.tvoswolfkillclient.model.entity.UserEventEntity;
import com.rongyan.tvoswolfkillclient.model.enums.UserEventType;

import de.greenrobot.event.EventBus;

/**
 * Created by XRY on 2017/7/28.
 */

public class SaveState implements BaseState {
    /**
     *
     * @param userEntity
     * @param targetId = 0不救，=1救
     */
    @Override
    public void send(UserEntity userEntity, int targetId) {
        if (targetId == 0) {
            EventBus.getDefault().post(new UserEventEntity(userEntity, UserEventType.NOT_SAVE, -1));
        } else {
            EventBus.getDefault().post(new UserEventEntity(userEntity, UserEventType.SAVE, targetId));
        }
    }
}
