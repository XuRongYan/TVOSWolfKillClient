package com.rongyan.tvoswolfkillclient.event_message;

import android.support.annotation.Nullable;

/**
 * Created by XRY on 2017/8/4.
 */

public class ReplaceFgmEvent {
    private String fgmTag;

    private int[] ids;

    public ReplaceFgmEvent(String fgmTag, @Nullable int...ids) {
        this.fgmTag = fgmTag;
        this.ids = ids;
    }

    public String getFgmTag() {
        return fgmTag;
    }

    public void setFgmTag(String fgmTag) {
        this.fgmTag = fgmTag;
    }

    public int[] getIds() {
        return ids;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }
}
