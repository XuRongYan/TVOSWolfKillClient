package com.rongyan.tvoswolfkillclient.event_message;

/**
 * Created by XRY on 2017/8/4.
 */

public class ShowDialogEvent {
    public static final String SHOW_CHAMPAIGN = "SHOW_CHAMPAIGN";
    private String message;

    public ShowDialogEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
