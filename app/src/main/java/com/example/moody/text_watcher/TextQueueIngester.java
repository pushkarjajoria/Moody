package com.example.moody.text_watcher;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.moody.metadata.ActivityQueue;
import com.example.moody.metadata.KeyboardActivity;
import com.example.moody.metadata.KeyboardActivityUtils;

import java.util.Optional;

public class TextQueueIngester {
    private ActivityQueue activityQueue;
    private TextChangeQueue beforeQueue = QueueFactory.getBeforeChangeInstance();
    private TextChangeQueue afterQueue = QueueFactory.getAfterChangeInstance();

    TextQueueIngester(ActivityQueue queue) {
        this.activityQueue = queue;
    }

    private boolean idMatchFor(TypingEvent beforeEvent, TypingEvent afterEvent) {
        return beforeEvent.getId().equals(afterEvent.getId());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startIngestActivities(ActivityQueue activityQueue) {
        while(true){
            Optional<TypingEvent> beforeEventOpt = beforeQueue.peekActivity();
            Optional<TypingEvent> afterEventOpt = afterQueue.peekActivity();

            if(beforeEventOpt.isPresent() && afterEventOpt.isPresent() && idMatchFor(beforeEventOpt.get(), afterEventOpt.get())) {
                TypingEvent beforeEvent = beforeQueue.getActivity().get();
                TypingEvent afterEvent = afterQueue.getActivity().get();
                Optional<KeyboardActivity> processedActivity = KeyboardActivityUtils.getInstance().generateKeyboardActivityFrom(beforeEvent, afterEvent);
                if(processedActivity.isPresent()) {
                    activityQueue.addActivity(processedActivity.get());
                }
            }
            else{
                continue;
            }

        }
    }

}
