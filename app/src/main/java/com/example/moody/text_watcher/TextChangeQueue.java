package com.example.moody.text_watcher;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.moody.metadata.KeyboardActivity;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class TextChangeQueue {

    private Queue<KeyboardActivity> activityQueue;

    private Queue<TypingEvent> typingQueue;

    TextChangeQueue() {
        typingQueue = new LinkedList<>();
    }

    public boolean addActivity(TypingEvent typingEvent){
        return typingQueue.add(typingEvent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Optional<TypingEvent> getActivity(){
        return Optional.ofNullable(typingQueue.remove());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Optional<TypingEvent> peekActivity(){
        return Optional.ofNullable(typingQueue.peek());
    }

}
