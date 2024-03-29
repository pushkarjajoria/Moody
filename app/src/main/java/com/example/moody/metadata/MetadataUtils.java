package com.example.moody.metadata;

import org.joda.time.DateTime;

import java.util.Iterator;
import java.util.List;

public class MetadataUtils {
    private static final MetadataUtils singleton = new MetadataUtils();

    public static MetadataUtils getInstance() {
        return singleton;
    }

    private MetadataUtils() {
    }

    public Metadata createMetadata(List<KeyboardActivity> batch, String batchId){
        Metadata metadata = new Metadata();
        DateTime now = DateTime.now();
        int numberOfCharacter = 0;
        double totalTime = 0;
        int numberOfSpecialCharacters = 0;
        double previousTime = 0;
        int error = 0;
        Iterator iter = batch.iterator();
        while(iter.hasNext()) {
            KeyboardActivity kActivity = (KeyboardActivity) iter.next();
            if(previousTime == 0){
                previousTime = kActivity.getTime();
            }
            totalTime += kActivity.getTime() - previousTime;
            previousTime = kActivity.getTime();
            switch (kActivity.getType()) {
                case BACKSPACE:
                    error+=1;
                    break;
                case TEXT_CHARACTER:
                    numberOfCharacter += 1;
                    break;
                case SPECIAL_CHARACTER: {
                    numberOfSpecialCharacters += 1;
                    numberOfCharacter += 1;
                    break;
                }
            }
        }
        metadata.setBatchId(batchId);
        metadata.setErrors(error);
        metadata.setNumberOfCharacters(numberOfCharacter);
        metadata.setTime(totalTime);
        metadata.setSpecialCharacters(numberOfSpecialCharacters);
        metadata.setTimestamp(DateTime.now());
        return metadata;
    }
}
