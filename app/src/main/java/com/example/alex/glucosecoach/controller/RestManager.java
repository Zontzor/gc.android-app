package com.example.alex.glucosecoach.controller;

import android.util.Log;

import com.example.alex.glucosecoach.services.BGService;
import com.example.alex.glucosecoach.services.InsService;
import com.example.alex.glucosecoach.services.UserService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alex on 1/24/17.
 */

public class RestManager {

    private static String BASE_URL = "http://192.168.1.101:5000/glucose_coach/api/v1.0/";
    private UserService userService;
    private BGService bgService;
    private InsService insService;

    public UserService getUserService() {
        if (userService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            userService = retrofit.create(UserService.class);
            Log.d("UserService", "Created retrofit userService instance");
        }

        return userService;
    }

    public BGService getBGService() {
        if (bgService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            bgService = retrofit.create(BGService.class);
            Log.d("BGService", "Created retrofit bgService instance");
        }

        return bgService;
    }

    public InsService getInsService() {
        if (insService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            insService = retrofit.create(InsService.class);
            Log.d("BGService", "Created retrofit bgService instance");
        }

        return insService;
    }
}
