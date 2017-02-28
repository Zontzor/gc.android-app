package com.example.alex.glucosecoach.services;

import com.example.alex.glucosecoach.models.FoodLog;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by alex on 2/28/17.
 */

public interface FoodLogService {
    @GET("users/{username}/foodlogs")
    Call<List<FoodLog>> getFoodLogs(@Path("username") String username);

    @POST("users/{username}/foodlogs")
    Call<FoodLog> postFoodLog(@Body FoodLog foodLog, @Path("username") String username);
}
