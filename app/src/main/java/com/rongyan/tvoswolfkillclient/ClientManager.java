package com.rongyan.tvoswolfkillclient;

import com.rongyan.model.entity.JesusEventEntity;
import com.rongyan.model.entity.UserEntity;
import com.rongyan.model.enums.JesusEvent;
import com.rongyan.model.enums.RoleType;
import com.rongyan.model.state.ChiefCampaignState;
import com.rongyan.model.state.CloseEyesState;
import com.rongyan.model.state.DeadState;
import com.rongyan.model.state.GetState;
import com.rongyan.model.state.GiveChiefState;
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
import com.rongyan.tvoswolfkillclient.event_message.ShowButton;
import com.rongyan.tvoswolfkillclient.event_message.ShowPopupEvent;
import com.rongyan.tvoswolfkillclient.fragment.FragmentTagHolder;
import com.rongyant.commonlib.util.LogUtils;

import java.util.Arrays;

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
    public static final int GOOD = 1; //好人
    public static final int BAD = 0; //狼人
    public static final int SINGLE_DEATH = 0X0; //单死，死左死右
    public static final int DOUBLE_DEATH = 0X1; //双死，警左警右
    public static boolean isChampaign = false; //是否参与竞选
    private boolean canWitchSaveHerself = true;
    private boolean idiotVoted = false;

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
        if (eventEntity.getEvent() == JesusEvent.GIVE_CHIEF) {
            if (UserHolder.userEntity.getUserId() == eventEntity.getTargetId()[0]) {
                userEntity.setState(new GiveChiefState());

                EventBus.getDefault().post(new ShowPopupEvent(ShowPopupEvent.GIVE_CHIEF, eventEntity.getTargetId()));

            }

        }

        if ((eventEntity.getRoleType() == userEntity.getRoleType()
                || eventEntity.getRoleType() == RoleType.ANY
                || (eventEntity.getRoleType() == RoleType.GOD && (userEntity.getRoleType() == RoleType.TELLER || userEntity.getRoleType() == RoleType.WITCH || userEntity.getRoleType() == RoleType.HUNTER || userEntity.getRoleType() == RoleType.IDIOT || userEntity.getRoleType() == RoleType.GUARD)))
                && !(UserHolder.userEntity.getState() instanceof DeadState)
                && !(UserHolder.userEntity.getState() instanceof PoisonDeadState)) {
            LogUtils.e(TAG, "onMessageEvent", "event bus received message:" + eventEntity.toString());
            switch (eventEntity.getEvent()) {

                case CLOSE_EYES:
                    EventBus.getDefault().postSticky(new ShowButton(ShowButton.DISMISS_SELF_DESTRUCTION));
                    userEntity.setState(new CloseEyesState());
                    EventBus.getDefault().post(new ReplaceFgmEvent(FragmentTagHolder.CARD_FGM, eventEntity.getTargetId()));
                    if (userEntity.getRoleType() == RoleType.WITCH) {
                        canWitchSaveHerself = false;
                    }
                    break;
                case OPEN_EYES:
                    if (eventEntity.getRoleType() == RoleType.ANY && UserHolder.userEntity.getRoleType() == RoleType.WOLF) {
                        EventBus.getDefault().postSticky(new ShowButton(ShowButton.SHOW_SELF_DESTRUCTION));
                    }
                    userEntity.setState(new OpenEyesState());
                    EventBus.getDefault().post(new ReplaceFgmEvent(FragmentTagHolder.CARD_FGM, eventEntity.getTargetId()));
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
                    //弃用
                    userEntity.setState(new PoisonState());
                    EventBus.getDefault().post(new ShowPopupEvent(ShowPopupEvent.WITCH_CHOOSE, eventEntity.getTargetId()));
                    break;
                case SAVE:
                    userEntity.setState(new SaveState());
                    EventBus.getDefault().post(new ShowPopupEvent(ShowPopupEvent.WITCH_CHOOSE, eventEntity.getTargetId()));
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
                    EventBus.getDefault().post(new ShowPopupEvent(ShowPopupEvent.SHOW_CHAMPAIGN));
                    break;
                case VOTE:
                    userEntity.setState(new VoteState());
                    EventBus.getDefault().post(new ReplaceFgmEvent(FragmentTagHolder.ACTION_FGM, eventEntity.getTargetId()));
                    break;
                case SPEECH:
                    userEntity.setState(new SpeechState());
                    break;
                case STOP_SPEECH:
                    userEntity.setState(new OpenEyesState());
                    break;
                case GOOD_OR_NOT:

                    int[] targetId = eventEntity.getTargetId();
                    EventBus.getDefault().post(new ShowPopupEvent(targetId[0] == GOOD ?
                            ShowPopupEvent.SHOW_GOOD : ShowPopupEvent.SHOW_BAD));
                    break;
                case DEAD:
                    if (UserHolder.userEntity.getUserId() == eventEntity.getTargetId()[0]) {
                        userEntity.setState(new DeadState());
                        EventBus.getDefault().post(new ReplaceFgmEvent(FragmentTagHolder.DEAD_FGM));
                    }
                    break;
                case POISON_DEAD:
                    if (UserHolder.userEntity.getUserId() == eventEntity.getTargetId()[0]) {
                        userEntity.setState(new PoisonDeadState());
                        EventBus.getDefault().post(new ReplaceFgmEvent(FragmentTagHolder.DEAD_FGM));
                    }
                    break;
                case CHIEF_SPEECH:
                    if (UserHolder.userEntity.isChampaign()) {
                        userEntity.setState(new SpeechState());
                        EventBus.getDefault().post(new ReplaceFgmEvent(FragmentTagHolder.CARD_FGM, eventEntity.getTargetId()));
                    }
                    break;
                case CHIEF_VOTE:
                    if (!UserHolder.userEntity.isChampaign()) {
                        LogUtils.e(TAG, "CHIEF_VOTE", UserHolder.userEntity.isChampaign() + "");
                        userEntity.setState(new VoteState());
                        EventBus.getDefault().post(new ShowButton(ShowButton.DISMISS));
                        EventBus.getDefault().post(new ReplaceFgmEvent(FragmentTagHolder.ACTION_FGM, eventEntity.getTargetId()));
                    } else {
                        EventBus.getDefault().post(new ShowButton(ShowButton.DISMISS));
                    }
                    break;
                case GET_SHOOT_STATE:
                    EventBus.getDefault().post(new ShowPopupEvent(ShowPopupEvent.HUNTER_GET_SHOOT_STATE, eventEntity.getTargetId()));
                    break;
                case YOU_ARE_CHIEF:
                    int[] targetId1 = eventEntity.getTargetId();
                    if (targetId1[0] == userEntity.getUserId()) {
                        EventBus.getDefault().post(new ShowPopupEvent(ShowPopupEvent.SHOW_CHIEF, eventEntity.getTargetId()));
                    }
                    break;
                case CHOOSE_SEQUENCE:
                    int[] sequenceType = eventEntity.getTargetId();
                    LogUtils.e(TAG, "choose sequence ", Arrays.toString(sequenceType));
                    if (sequenceType[0] == userEntity.getUserId()) {
                        EventBus.getDefault().post(new ShowPopupEvent(ShowPopupEvent.CHOOSE_SEQUENCE, sequenceType));
                    }
                    break;
                case IDIOT_VOTED:
                    idiotVoted = true;
                    break;
                case GAME_OVER:
                    EventBus.getDefault().post(new ReplaceFgmEvent("GAME_OVER"));
                    break;
                case GIVE_CHIEF:
                    if (UserHolder.userEntity.getUserId() == eventEntity.getTargetId()[0]) {
                        userEntity.setState(new GiveChiefState());
                        EventBus.getDefault().post(new ShowPopupEvent(ShowPopupEvent.GIVE_CHIEF, eventEntity.getTargetId()));
                    }
                    break;
            }
        } else if (UserHolder.userEntity.getState() instanceof DeadState && idiotVoted == true && UserHolder.userEntity.getRoleType() == RoleType.IDIOT) {
            if (eventEntity.getEvent() == JesusEvent.SPEECH) {
                userEntity.setState(new SpeechState());
            } else if (eventEntity.getEvent() == JesusEvent.STOP_SPEECH) {
                userEntity.setState(new OpenEyesState());
            }
        }
    }
}
