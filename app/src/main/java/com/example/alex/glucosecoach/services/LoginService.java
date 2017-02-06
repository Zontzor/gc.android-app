package com.example.alex.glucosecoach.services;

import com.example.alex.glucosecoach.models.Token;
import com.example.alex.glucosecoach.models.User;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by alex on 2/5/17.
 */

public interface LoginService {
    @GET("token")
    Call<Token> basicLogin();
}
