package com.example.alex.glucosecoach.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.alex.glucosecoach.models.User;

/**
 * Created by alex on 2/6/17.
 */

public class UserManager {
    private Context context;
    private final SharedPreferences settings;

    public UserManager(Context context) {
        setContext(context);
        settings = PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getUsername() {
        String username = settings.getString("username", ""/*default value*/);
        return username;
    }

    public void setUsername(String username) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("username", username);
        editor.commit();
    }

    public String getEmail() {
        String email = settings.getString("email", ""/*default value*/);
        return email;
    }

    public void setEmail(String email) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("email", email);
        editor.commit();
    }
}
