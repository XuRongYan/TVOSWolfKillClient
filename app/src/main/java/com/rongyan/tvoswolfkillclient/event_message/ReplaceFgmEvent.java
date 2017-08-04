package com.rongyan.tvoswolfkillclient.event_message;

/**
 * Created by XRY on 2017/8/4.
 */

public class ReplaceFgmEvent {
    private String fgmTag;

    public ReplaceFgmEvent(String fgmTag) {
        this.fgmTag = fgmTag;
    }

    public String getFgmTag() {
        return fgmTag;
    }

    public void setFgmTag(String fgmTag) {
        this.fgmTag = fgmTag;
    }
}
