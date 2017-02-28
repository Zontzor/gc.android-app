package com.example.alex.glucosecoach.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 2/28/17.
 */

public class Exercise {

    @SerializedName("id")
    private Integer id;
    @SerializedName("e_name")
    private String e_name;
    @SerializedName("e_energy_phour")
    private Integer e_energy_phour;

    public Exercise(Integer id, String e_name, Integer e_energy_phour) {
        setId(id);
        setE_name(e_name);
        setE_energy_phour(e_energy_phour);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getE_name() {
        return e_name;
    }

    public void setE_name(String e_name) {
        this.e_name = e_name;
    }

    public Integer getE_energy_phour() {
        return e_energy_phour;
    }

    public void setE_energy_phour(Integer e_energy_phour) {
        this.e_energy_phour = e_energy_phour;
    }

    @Override
    public String toString() {
        return getE_name();            // What to display in the Spinner list.
    }
}
