package com.example.alex.glucosecoach.models;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by alex on 2/5/17.
 */

public class Token {
    @SerializedName("token")
    String tokenValue;
    Date expirationDate;

    public Token(String tokenValue) {
        setTokenValue(tokenValue);
    }

    public Token(String tokenValue, Date expirationDate) {
        setTokenValue(tokenValue);
        setExpirationDate(expirationDate);
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
