package com.example.alex.glucosecoach.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by alex on 2/5/17.
 */

public class TokenManager {

    public TokenManager() {}

    public String getToken(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String auth_token_string = settings.getString("token", ""/*default value*/);
        return auth_token_string;
    }

    public void setToken(Context context, String token) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("token", token);
        editor.commit();
    }

    public boolean hasToken(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String auth_token_string = settings.getString("token", ""/*default value*/);

        if (!auth_token_string.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public void clearToken(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("token", null);
        editor.commit();
    }
}
