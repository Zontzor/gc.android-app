package com.example.alex.glucosecoach.models;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alex on 2/27/17.
 */

public class PredicitionFact {
    @SerializedName("pf_date")
    private String pfDate;
    @SerializedName("pf_time_of_day")
    private Integer pfTimeOfDay;
    @SerializedName("user_id")
    private Integer userID;
    @SerializedName("bg_value")
    private Double bgValue;
    @SerializedName("ins_value")
    private Double insValue;
    @SerializedName("food_value")
    private Integer foodValue;
    @SerializedName("exercise_value")
    private Integer exerciseValue;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public PredicitionFact(String pfDate, Integer pfTimeOfDay, Integer userID, Double bgValue, Double insValue, Integer foodValue, Integer exerciseValue, Map<String, Object> additionalProperties) {
        setPfDate(pfDate);
        setPfTimeOfDay(pfTimeOfDay);
        setUserID(userID);
        setBgValue(bgValue);
        setInsValue(insValue);
        setFoodValue(foodValue);
        setExerciseValue(exerciseValue);
        setAdditionalProperties(additionalProperties);
    }

    public String getPfDate() {
        return pfDate;
    }

    public void setPfDate(String pfDate) {
        this.pfDate = pfDate;
    }

    public Integer getPfTimeOfDay() {
        return pfTimeOfDay;
    }

    public void setPfTimeOfDay(Integer pfTimeOfDay) {
        this.pfTimeOfDay = pfTimeOfDay;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Double getBgValue() {
        return bgValue;
    }

    public void setBgValue(Double bgValue) {
        this.bgValue = bgValue;
    }

    public Double getInsValue() {
        return insValue;
    }

    public void setInsValue(Double insValue) {
        this.insValue = insValue;
    }

    public Integer getFoodValue() {
        return foodValue;
    }

    public void setFoodValue(Integer foodValue) {
        this.foodValue = foodValue;
    }

    public Integer getExerciseValue() {
        return exerciseValue;
    }

    public void setExerciseValue(Integer exerciseValue) {
        this.exerciseValue = exerciseValue;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}
