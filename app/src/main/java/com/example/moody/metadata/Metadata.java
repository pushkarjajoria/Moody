package com.example.moody.metadata;

import org.joda.time.DateTime;

public class Metadata {

    private DateTime timestamp;

    private int numberOfCharacters;

    private double time; //Total time taken to write all these characters

    private int specialCharacters;

    private int errors;

    private String batchId;

    public DateTime getTimestamp() {
        return timestamp;
    }

    public float getErrorRate(){
        return (errors/numberOfCharacters)*100;
    }

    public float getTimeInSec(){
        return 0.0f;
    }

    // Setter Getter
    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getNumberOfCharacters() {
        return numberOfCharacters;
    }

    public void setNumberOfCharacters(int numberOfCharacters) {
        this.numberOfCharacters = numberOfCharacters;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int getSpecialCharacters() {
        return specialCharacters;
    }

    public void setSpecialCharacters(int specialCharacters) {
        this.specialCharacters = specialCharacters;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

}
