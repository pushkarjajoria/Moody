package com.example.moody.text_watcher;


public class TypingEvent {
    private long time;
    private String text;
    private String id;

    public void setTime(long time) {
        this.time = time;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }
}
