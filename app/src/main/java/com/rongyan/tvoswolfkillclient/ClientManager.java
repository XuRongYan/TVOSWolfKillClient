package com.rongyan.tvoswolfkillclient;

import com.rongyan.model.entity.JesusEventEntity;
import com.rongyan.model.entity.UserEntity;
import com.rongyan.model.enums.RoleType;
import com.rongyan.model.state.ChiefCampaignState;
import com.rongyan.model.state.CloseEyesState;
import com.rongyan.model.state.DeadState;
import com.rongyan.model.state.GetState;
import com.rongyan.model.state.KillState;
import com.rongyan.model.state.OpenEyesState;
import com.rongyan.model.state.PoisonDeadState;
import com.rongyan.model.state.PoisonState;
import com.rongyan.model.state.ProtectState;
import com.rongyan.model.state.SaveState;
import com.rongyan.model.state.ShootState;
import com.rongyan.model.state.SpeechState;
import com.rongyan.model.state.VoteState;
import com.rongyan.tvoswolfkillclient.event_message.ReplaceFgmEvent;
import com.rongyan.tvoswolfkillclient.event_message.ShowDialogEvent;
import com.rongyan.tvoswolfkillclient.fragment.FragmentTagHolder;
import com.rongyant.commonlib.util.LogUtils;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by XRY on 2017/8/4.
 */

public class ClientManager {
    private static final String TAG = "ClientManager";
    private static ClientManager INSTANCE = null;
    private UserEntity userEntity;
    public static boolean isChampaign = false; //是否参与竞选

    private ClientManager() {
        EventBus.getDefault().register(this);
        userEntity = UserHolder.userEntity;
    }

    public static ClientManager getInstance() {
        if (INSTANCE == null) {
            synchronized (ClientManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ClientManager();
                }
            }
        }
        return INSTANCE;
    }

    @Subscribe(threadMode = ThreadMode.BackgroundThread)
    public void onMessageEvent(JesusEventEntity eventEntity) {
        LogUtils.e(TAG, "onMessageEvent", "receive");
        if (userEntity == null) {
            userEntity = UserHolder.userEntity;
            if (userEntity == null) {
                LogUtils.e(TAG, "onMessageEvent", "can't find user");
                return;
            }
        }
        if (eventEntity.getRoleType() == userEntity.getRoleType()
                || eventEntity.getRoleType() == RoleType.ANY
                || (eventEntity.getRoleType() == RoleType.GOD && (userEntity.getRoleType() == RoleType.TELLER || userEntity.getRoleType() == RoleType.WITCH || userEntity.getRoleType() == RoleType.HUNTER || userEntity.getRoleType() == RoleType.IDIOT || userEntity.getRoleType() == RoleType.GUARD))) {
            LogUtils.e(TAG, "onMessageEvent","event bus received message:" + eventEntity.toString());
            switch (eventEntity.getEvent()) {
                case CLOSE_EYES:
                    userEntity.setState(new CloseEyesState());
                    break;
                case OPEN_EYES:
                    userEntity.setState(new OpenEyesState());
                    break;
                case KILL:
                    userEntity.setState(new KillState());
                    EventBus.getDefault().post(new ReplaceFgmEvent(FragmentTagHolder.ACTION_FGM, eventEntity.getTargetId()));
                    break;
                case PROTECT:
                    userEntity.setState(new ProtectState());
                    EventBus.getDefault().post(new ReplaceFgmEvent(FragmentTagHolder.ACTION_FGM, eventEntity.getTargetId()));
                    break;
                case POISON:
                    userEntity.setState(new PoisonState());
                    EventBus.getDefault().post(new ReplaceFgmEvent(FragmentTagHolder.ACTION_FGM, eventEntity.getTargetId()));
                    break;
                case SAVE:
                    userEntity.setState(new SaveState());
                    EventBus.getDefault().post(new ReplaceFgmEvent(FragmentTagHolder.SAVE_FGM, eventEntity.getTargetId()));
                    break;
                case SHOOT:
                    userEntity.setState(new ShootState());
                    EventBus.getDefault().post(new ReplaceFgmEvent(FragmentTagHolder.ACTION_FGM, eventEntity.getTargetId()));
                    break;
                case TRUE:
                    break;
                case FALSE:
                    break;
                case GET:
                    userEntity.setState(new GetState());
                    EventBus.getDefault().post(new ReplaceFgmEvent(FragmentTagHolder.ACTION_FGM, eventEntity.getTargetId()));
                    break;
                case CHIEF_CAMPAIGN:
                    userEntity.setState(new ChiefCampaignState());
                    EventBus.getDefault().post(new ShowDialogEvent(ShowDialogEvent.SHOW_CHAMPAIGN));
                    break;
                case VOTE:
                    userEntity.setState(new VoteState());
                    EventBus.getDefault().post(new ReplaceFgmEvent(FragmentTagHolder.ACTION_FGM));
                    break;
                case SPEECH:
                    userEntity.setState(new SpeechState());
                    break;
                case STOP_SPEECH:
                    userEntity.setState(new OpenEyesState());
                    break;
                case GOOD_OR_NOT:

                    break;
                case DEAD:
                    userEntity.setState(new DeadState());
                    EventBus.getDefault().post(new ReplaceFgmEvent(FragmentTagHolder.DEAD_FGM));
                    break;
                case POISON_DEAD:
                    userEntity.setState(new PoisonDeadState());
                    EventBus.getDefault().post(new ReplaceFgmEvent(FragmentTagHolder.DEAD_FGM));
                    break;
                case CHIEF_SPEECH:
                    if (isChampaign) {
                        userEntity.setState(new SpeechState());
                    }
                    break;
                case CHIEF_VOTE:
                    userEntity.setState(new VoteState());
                    EventBus.getDefault().post(new ReplaceFgmEvent(FragmentTagHolder.ACTION_FGM));
                    break;
            }
        }
    }
}
