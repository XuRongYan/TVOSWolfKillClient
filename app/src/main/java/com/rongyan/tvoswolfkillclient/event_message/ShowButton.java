package com.rongyan.tvoswolfkillclient.event_message;

/**
 * Created by XRY on 2017/8/16.
 */

public class ShowButton {
    public static final String SHOW = "SHOW";
    public static final String DISMISS = "DISMISS";
    public static final String SHOW_SELF_DESTRUCTION = "SHOW_SELF_DESTRUCTION";
    public static final String DISMISS_SELF_DESTRUCTION = "DISMISS_SELF_DESTRUCTION";
    private String message;

    public ShowButton(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
