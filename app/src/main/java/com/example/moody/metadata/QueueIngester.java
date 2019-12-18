package com.example.moody.metadata;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.moody.persistence.Database;
import com.example.moody.persistence.PersistanceService;
import com.example.moody.persistence.SQLService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class QueueIngester {
    private int batchingTimeInterval;
    private SQLService persistanceService;

    private boolean isRunning = true;

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public QueueIngester(int thresholdTime, SQLService persistanceService) {
        this.batchingTimeInterval = thresholdTime;
        this.persistanceService = persistanceService;
        SQLService service = new SQLService();
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
                    SQLiteDatabase db = Database.getInstance().getDatabase();
                    ContentValues values = new ContentValues();
                    values.put(SQLService.TableEntry.COLUMN_NAME_TIMESTAMP, metadata.getTimestamp().getMillis());
                    values.put(SQLService.TableEntry.COLUMN_NAME_NUMBER_OF_CHARACTERS, metadata.getNumberOfCharacters());
                    values.put(SQLService.TableEntry.COLUMN_NAME_TOTAL_TIME, metadata.getTime());
                    values.put(SQLService.TableEntry.COLUMN_NAME_NUMBER_OF_SPECIAL_CHARACTERS, metadata.getSpecialCharacters());
                    values.put(SQLService.TableEntry.COLUMN_NAME_NUMBER_OF_ERROR, metadata.getErrors());
                    values.put(SQLService.TableEntry.COLUMN_NAME_BATCH_ID, metadata.getBatchId());
                    db.insert("TypingData", null, values);
                    break;
                }
            }
        }
    }
}
