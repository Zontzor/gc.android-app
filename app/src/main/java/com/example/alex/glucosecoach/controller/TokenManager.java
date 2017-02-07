package com.example.alex.glucosecoach.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by alex on 2/5/17.
 */

public class TokenManager {
    private Context context;
    private final SharedPreferences settings;

    public TokenManager(Context context) {
        setContext(context);
        settings = PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getToken() {
        String auth_token_string = settings.getString("token", ""/*default value*/);
        return auth_token_string;
    }

    public void setToken(String token) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("token", token);
        editor.commit();
    }

    public boolean hasToken() {
        String auth_token_string = settings.getString("token", ""/*default value*/);

        if (!auth_token_string.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public void clearToken() {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("token", null);
        editor.commit();
    }
}
