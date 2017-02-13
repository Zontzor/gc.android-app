package com.example.alex.glucosecoach.models;

/**
 * Created by alex on 1/31/17.
 */

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class InsValue {
    @SerializedName("id")
    private Integer id;
    @SerializedName("username")
    private String username;
    @SerializedName("ins_type")
    private String insulinType;
    @SerializedName("ins_value")
    private Double insulinValue;
    @SerializedName("ins_timestamp")
    private String insulinTimestamp;

    public InsValue(String insulinType, Double insulinValue, String insulinTimestamp) {
        setInsulinType(insulinType);
        setInsulinValue(insulinValue);
        setInsulinTimestamp(insulinTimestamp);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInsulinType() {
        return insulinType;
    }

    public void setInsulinType(String insulinType) {
        this.insulinType = insulinType;
    }

    public Double getInsulinValue() {
        return insulinValue;
    }

    public void setInsulinValue(Double insulinValue) {
        this.insulinValue = insulinValue;
    }

    public String getInsulinTimestamp() {
        return insulinTimestamp;
    }

    public void setInsulinTimestamp(String insulinTimestamp) {
        this.insulinTimestamp = insulinTimestamp;
    }
}