package com.example.alex.glucosecoach.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.alex.glucosecoach.models.User;
import com.google.gson.Gson;

/**
 * Created by alex on 2/6/17.
 *
 * Helper class for managing users
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

    public void setUser(User user) {
        SharedPreferences.Editor editor = settings.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString("user", json);
        editor.apply();
    }

    public String getUsername() {
        Gson gson = new Gson();
        String userString = settings.getString("user", "");

        User user = gson.fromJson(userString, User.class);

        if (user == null) {
            return "username";
        }

        return user.getUsername();
    }

    public String getEmail() {
        Gson gson = new Gson();
        String userString = settings.getString("user", "");

        User user = gson.fromJson(userString, User.class);

        if (user == null) {
            return "email";
        }

        return user.getEmail();
    }
}
