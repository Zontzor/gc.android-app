package com.example.alex.glucosecoach.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 2/28/17.
 */

public class Food {

    @SerializedName("id")
    private Integer id;
    @SerializedName("f_name")
    private String f_name;
    @SerializedName("f_carbs")
    private Integer f_carbs;

    public Food(Integer id, String f_name, Integer f_carbs) {
        setId(id);
        setF_name(f_name);
        setF_carbs(f_carbs);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public Integer getF_carbs() {
        return f_carbs;
    }

    public void setF_carbs(Integer f_carbs) {
        this.f_carbs = f_carbs;
    }

    @Override
    public String toString() {
        return (getF_name() + " - " + getF_carbs().toString() + "g");            // What to display in the Spinner list.
    }
}
