package com.example.moody.metadata;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.example.moody.persistence.PersistanceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QueueIngester {
    private int batchingTimeInterval;
    private PersistanceService persistanceService;

    public QueueIngester(int thresholdTime, PersistanceService persistanceService) {
        this.batchingTimeInterval = thresholdTime;
        this.persistanceService = persistanceService;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startIngestActivities(ActivityQueue activityQueue) {
        MetadataUtils metadataUtils = MetadataUtils.getInstance();
        List<KeyboardActivity> batch = new ArrayList<>();
        while(true){
            Optional<KeyboardActivity> activityOptional =  activityQueue.peekActivity();

            if(activityOptional.isPresent()) {
                KeyboardActivity previousActivity = activityQueue.getActivity().get();
                batch.add(previousActivity);
                while(activityQueue.peekActivity().isPresent()){
                    Optional<KeyboardActivity> nextActivityOption = activityQueue.getActivity();

                    if (nextActivityOption.isPresent() && nextActivityOption.get().getTime() - previousActivity.getTime() < batchingTimeInterval) {
                        KeyboardActivity presentActivity = activityQueue.getActivity().get();
                        batch.add(presentActivity);
                        previousActivity = presentActivity;
                    }
                    else break;
                }
            }
            // Ignoring isolated character events
            if(batch.size() > 1){
                Metadata metadata = metadataUtils.createMetadata(batch);
                persistanceService.persistMetadata(metadata);
            }
            batch.clear();
        }
    }
}
