package com.example.alex.glucosecoach.business;

/**
 * Created by alex on 12/19/16.
 */

public class BGValue {

    private String username;
    private Float BGReading;
    private String timestamp;

    public BGValue(String username, Float BGReading, String timestamp) {
        setUsername(username);
        setBGReading(BGReading);
        setTimestamp(timestamp);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Float getBGReading() {
        return BGReading;
    }

    public void setBGReading(Float BGReading) {
        this.BGReading = BGReading;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
