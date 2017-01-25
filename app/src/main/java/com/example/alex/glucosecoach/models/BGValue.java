package com.example.alex.glucosecoach.models;

/**
 * Created by alex on 12/19/16.
 */

import java.util.HashMap;
import java.util.Map;

public class BGValue {

    private String bgTimestamp;
    private Double bgValue;
    private Integer id;
    private String username;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getBgTimestamp() {
        return bgTimestamp;
    }

    public void setBgTimestamp(String bgTimestamp) {
        this.bgTimestamp = bgTimestamp;
    }

    public Double getBgValue() {
        return bgValue;
    }

    public void setBgValue(Double bgValue) {
        this.bgValue = bgValue;
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

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}