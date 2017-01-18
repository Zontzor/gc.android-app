package com.example.alex.glucosecoach.business;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 12/19/16.
 */

public class User {

    private String username;
    private String email;
    private String firstname;
    private Float weight;
    private Integer height;

    public User(String username, String email, String firstname) {
        setUsername(username);
        setEmail(email);
        setFirstname(firstname);
    }

    public User(int id, String username, String email, String firstname, Float weight, Integer height) {
        setUsername(username);
        setEmail(email);
        setFirstname(firstname);
        setWeight(weight);
        setHeight(height);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

}
