package com.example.moody.metadata;

import com.example.moody.ButtonType;

public class KeyboardActivity {
    private ButtonType type;
    private long time;

    public KeyboardActivity(ButtonType type, long timestamp){
        this.type = type;
        this.time = timestamp;
    }

    public void setType(ButtonType type) {
        this.type = type;
    }

    public  ButtonType getType() {
        return this.type;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public  long getTime() {
        return this.time;
    }

}