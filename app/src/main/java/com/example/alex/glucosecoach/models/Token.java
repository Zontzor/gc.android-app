package com.example.alex.glucosecoach.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 2/5/17.
 */

public class Token {
    @SerializedName("token")
    String tokenValue;

    public Token(String tokenValue) {
        setTokenValue(tokenValue);
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }
}
