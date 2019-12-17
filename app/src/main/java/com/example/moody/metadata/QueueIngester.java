package com.example.moody.metadata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.moody.persistence.PersistanceService;
import com.example.moody.persistence.SQLService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QueueIngester {
    private int batchingTimeInterval;
    private PersistanceService persistanceService;

    private boolean isRunning = true;
    private SQLiteDatabase database;

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public QueueIngester(int thresholdTime, PersistanceService persistanceService, Context context) {
        this.batchingTimeInterval = thresholdTime;
        this.persistanceService = persistanceService;
        database = new SQLService.FeedReaderDbHelper(context).getWritableDatabase();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startIngestActivities(ActivityQueue activityQueue, String batchId) {
        MetadataUtils metadataUtils = MetadataUtils.getInstance();
        List<KeyboardActivity> batch = new ArrayList<>();
        while (true) {
            Optional<KeyboardActivity> activityOptional = activityQueue.peekActivity();
            if (activityOptional.isPresent()) {
                KeyboardActivity previousActivity = activityQueue.getActivity().get();
                batch.add(previousActivity);
            }
            // Ignoring isolated character events

            if(!isRunning && !activityQueue.peekActivity().isPresent()){
                if (batch.size() > 1) {
                    Log.d("QueueIgester", "Persisting Data");
                    Metadata metadata = metadataUtils.createMetadata(batch, batchId);
                    persistanceService.persistMetadata(metadata, database);
                }
            }
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public void startIngestActivities(ActivityQueue activityQueue, String batchId) {
//        MetadataUtils metadataUtils = MetadataUtils.getInstance();
//        List<KeyboardActivity> batch = new ArrayList<>();
//        while (isRunning && !activityQueue.peekActivity().isPresent()) {
//            Optional<KeyboardActivity> activityOptional = activityQueue.peekActivity();
//
//            if (activityOptional.isPresent()) {
//                KeyboardActivity previousActivity = activityQueue.getActivity().get();
//                batch.add(previousActivity);
//                if (activityQueue.peekActivity().isPresent()) {
//                    Optional<KeyboardActivity> nextActivityOption = activityQueue.getActivity();
//
//                    if (nextActivityOption.isPresent() && nextActivityOption.get().getTime() - previousActivity.getTime() < batchingTimeInterval) {
//                        KeyboardActivity presentActivity = activityQueue.getActivity().get();
//                        batch.add(presentActivity);
//                        previousActivity = presentActivity;
//                    }
//                    else break;
//                }
//            }
//            // Ignoring isolated character events
//            if (batch.size() > 1) {
//                Log.d("QueueIgester", "Persisting Data");
//                Metadata metadata = metadataUtils.createMetadata(batch, batchId);
//                persistanceService.persistMetadata(metadata, database);
//            }
//            batch.clear();
//        }
//    }
}
