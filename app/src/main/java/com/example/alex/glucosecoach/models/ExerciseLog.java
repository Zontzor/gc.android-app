package com.example.alex.glucosecoach.models;

/**
 * Created by alex on 12/19/16.
 */

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class ExerciseLog {

    @SerializedName("id")
    private Integer id;
    @SerializedName("user_id")
    private Integer user_id;
    @SerializedName("exercise_id")
    private Integer exercise_id;
    @SerializedName("el_duration")
    private Integer el_duration;
    @SerializedName("el_timestamp")
    private String el_timestamp;

    public ExerciseLog(Integer exercise_id, Integer el_duration, String el_timestamp) {
        setId(id);
        setUser_id(user_id);
        setExercise_id(exercise_id);
        setEl_duration(el_duration);
        setEl_timestamp(el_timestamp);
    }

    public ExerciseLog(Integer id, Integer user_id, Integer exercise_id, Integer el_duration, String el_timestamp) {
        setId(id);
        setUser_id(user_id);
        setExercise_id(exercise_id);
        setEl_duration(el_duration);
        setEl_timestamp(el_timestamp);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getExercise_id() {
        return exercise_id;
    }

    public void setExercise_id(Integer exercise_id) {
        this.exercise_id = exercise_id;
    }

    public Integer getEl_duration() {
        return el_duration;
    }

    public void setEl_duration(Integer el_duration) {
        this.el_duration = el_duration;
    }

    public String getEl_timestamp() {
        return el_timestamp;
    }

    public void setEl_timestamp(String el_timestamp) {
        this.el_timestamp = el_timestamp;
    }
}