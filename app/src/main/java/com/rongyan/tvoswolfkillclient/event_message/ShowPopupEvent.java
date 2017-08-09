package com.rongyan.tvoswolfkillclient.event_message;

/**
 * Created by XRY on 2017/8/4.
 */

public class ShowPopupEvent {
    public static final String SHOW_CHAMPAIGN = "SHOW_CHAMPAIGN";
    public static final String SHOW_GOOD = "SHOW_GOOD"; //好人
    public static final String SHOW_BAD = "SHOW_BAD"; //坏人
    private String message;

    public ShowPopupEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
