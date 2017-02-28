package com.example.alex.glucosecoach.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 2/28/17.
 */

public class FoodLog {

    @SerializedName("id")
    private Integer id;
    @SerializedName("user_id")
    private Integer user_id;
    @SerializedName("food_id")
    private Integer food_id;
    @SerializedName("fl_quantity")
    private Double fl_quantity;
    @SerializedName("fl_timestamp")
    private String fl_timestamp;

    public FoodLog(Integer id, Integer user_id, Integer food_id, Double fl_quantity, String fl_timestamp) {
        setId(id);
        setUser_id(user_id);
        setFood_id(food_id);
        setFl_quantity(fl_quantity);
        setFl_timestamp(fl_timestamp);
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

    public Integer getFood_id() {
        return food_id;
    }

    public void setFood_id(Integer food_id) {
        this.food_id = food_id;
    }

    public Double getFl_quantity() {
        return fl_quantity;
    }

    public void setFl_quantity(Double fl_quantity) {
        this.fl_quantity = fl_quantity;
    }

    public String getFl_timestamp() {
        return fl_timestamp;
    }

    public void setFl_timestamp(String fl_timestamp) {
        this.fl_timestamp = fl_timestamp;
    }
}
