package com.example.alex.glucosecoach.model;

/**
 * Created by alex on 12/19/16.
 */

import java.util.HashMap;
import java.util.Map;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String dateCreated;
    private String email;
    private String firstname;
    private Integer height;
    private Integer id;
    private String password;
    private Object profileImagePath;
    private String username;
    private Double weight;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
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

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Object getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(Object profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
