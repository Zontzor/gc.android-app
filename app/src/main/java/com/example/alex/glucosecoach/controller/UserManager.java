package com.example.alex.glucosecoach.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.alex.glucosecoach.models.User;

/**
 * Created by alex on 2/6/17.
 */

public class UserManager {
    User user;

    public UserManager() {}

    public String getUsername(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String username = settings.getString("username", ""/*default value*/);
        return username;
    }

    public void setUsername(Context context, String username) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("username", username);
        editor.commit();
    }

    public String getEmail(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String email = settings.getString("email", ""/*default value*/);
        return email;
    }

    public void setEmail(Context context, String email) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("email", email);
        editor.commit();
    }
}
