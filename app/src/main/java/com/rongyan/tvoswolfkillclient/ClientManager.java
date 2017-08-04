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

import de.greenrobot.event.EventBus;

/**
 * Created by XRY on 2017/8/4.
 */

public class ClientManager {
    private static ClientManager INSTANCE = null;
    private UserEntity userEntity;

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

    public void onMessageEvent(JesusEventEntity eventEntity) {
        //TODO 用户输入采用Rxjava的zip方法打包发送给God
        if (eventEntity.getRoleType() == userEntity.getRoleType()
                || eventEntity.getRoleType() == RoleType.ANY
                || (eventEntity.getRoleType() == RoleType.GOD && (userEntity.getRoleType() == RoleType.TELLER || userEntity.getRoleType() == RoleType.WITCH || userEntity.getRoleType() == RoleType.HUNTER || userEntity.getRoleType() == RoleType.IDIOT || userEntity.getRoleType() == RoleType.GUARD))) {
            switch (eventEntity.getEvent()) {
                case CLOSE_EYES:
                    userEntity.setState(new CloseEyesState());
                    break;
                case OPEN_EYES:
                    userEntity.setState(new OpenEyesState());
                    break;
                case KILL:
                    userEntity.setState(new KillState());
                    EventBus.getDefault().post(new ReplaceFgmEvent(FragmentTagHolder.WOLF_FGM));
                    break;
                case PROTECT:
                    userEntity.setState(new ProtectState());
                    EventBus.getDefault().post(new ReplaceFgmEvent(FragmentTagHolder.GUARD_FGM));
                    break;
                case POISON:
                    userEntity.setState(new PoisonState());
                    EventBus.getDefault().post(new ReplaceFgmEvent(FragmentTagHolder.WITCH_FGM));
                    break;
                case SAVE:
                    userEntity.setState(new SaveState());
                    EventBus.getDefault().post(new ReplaceFgmEvent(FragmentTagHolder.SAVE_FGM));
                    break;
                case SHOOT:
                    userEntity.setState(new ShootState());
                    EventBus.getDefault().post(new ReplaceFgmEvent(FragmentTagHolder.HUNTER_FGM));
                    break;
                case TRUE:
                    break;
                case FALSE:
                    break;
                case GET:
                    userEntity.setState(new GetState());
                    EventBus.getDefault().post(new ReplaceFgmEvent(FragmentTagHolder.TELLER_FGM));
                    break;
                case CHIEF_CAMPAIGN:
                    userEntity.setState(new ChiefCampaignState());
                    EventBus.getDefault().post(new ShowDialogEvent(ShowDialogEvent.SHOW_CHAMPAIGN));
                    break;
                case VOTE:
                    userEntity.setState(new VoteState());
                    EventBus.getDefault().post(new ShowDialogEvent(FragmentTagHolder.VOTE_FGM));
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
            }
        }
    }
}
