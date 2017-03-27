package com.example.alex.glucosecoach.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.alex.glucosecoach.models.Token;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by alex on 2/5/17.
 *
 * Helper class to handle tokens
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
        Gson gson = new Gson();
        String authTokenString = settings.getString("token", "");

        Token token = gson.fromJson(authTokenString, Token.class);

        return token.getTokenValue();
    }

    public void setToken(String tokenValue) {
        SharedPreferences.Editor editor = settings.edit();

        Date now = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.DATE, 1);
        Date newDate = c.getTime();

        Token token = new Token(tokenValue, newDate);

        Gson gson = new Gson();
        String json = gson.toJson(token);
        editor.putString("token", json);
        editor.apply();
    }

    public boolean hasToken() {
        Gson gson = new Gson();
        String authTokenString = settings.getString("token", "");

        if (authTokenString.isEmpty()) {
            return false;
        }

        Token token = gson.fromJson(authTokenString, Token.class);

        Date now = new Date();

        return now.before(token.getExpirationDate());
    }

    public void clearToken() {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("token", null);
        editor.apply();
    }
}