package com.example.moody.metadata;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class JavaActivityQueue implements ActivityQueue {

    private Queue<KeyboardActivity> activityQueue;

    private static final JavaActivityQueue singleton = new JavaActivityQueue();

    public static JavaActivityQueue getInstance() {
        return singleton;
    }

    private JavaActivityQueue() {
        activityQueue = new LinkedList<>();
    }

    public boolean addActivity(KeyboardActivity keyboardActivity){
        return activityQueue.add(keyboardActivity);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Optional<KeyboardActivity> getActivity(){

        return Optional.ofNullable(activityQueue.remove());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Optional<KeyboardActivity> peekActivity(){
        return Optional.ofNullable(activityQueue.peek());
    }

}