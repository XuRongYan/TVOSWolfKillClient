package com.rongyan.tvoswolfkillclient.entity;

/**
 * Created by XRY on 2017/8/6.
 */

public class VoteEntity {
    private int id;
    private boolean checked;

    public VoteEntity(int id, boolean checked) {
        this.id = id;
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
