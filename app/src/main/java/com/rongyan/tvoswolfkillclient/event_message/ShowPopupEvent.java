package com.rongyan.tvoswolfkillclient.event_message;

/**
 * Created by XRY on 2017/8/4.
 */

public class ShowPopupEvent {
    public static final String SHOW_CHAMPAIGN = "SHOW_CHAMPAIGN";
    public static final String SHOW_GOOD = "SHOW_GOOD"; //好人
    public static final String SHOW_BAD = "SHOW_BAD"; //坏人
    public static final String WITCH_CHOOSE = "WITCH_CHOOSE"; //女巫选择行动
    public static final String HUNTER_GET_SHOOT_STATE = "HUNTER_GET_SHOOT_STATE"; //获取猎人开枪状态
    public static final String HUNTER_SOOT_OR_NOT = "HUNTER_SOOT_OR_NOT";
    private String message;

    private int[] targetId;

    public ShowPopupEvent(String message) {
        this.message = message;
    }

    public ShowPopupEvent(String message, int[] targetId) {
        this.message = message;
        this.targetId = targetId;
    }

    public int[] getTargetId() {
        return targetId;
    }

    public void setTargetId(int[] targetId) {
        this.targetId = targetId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
