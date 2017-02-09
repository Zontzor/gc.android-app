package com.example.alex.glucosecoach.services;

import com.example.alex.glucosecoach.models.BGValue;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by alex on 1/24/17.
 */

public interface BGService {
    @GET("users/{username}/bgreadings")
    Call<List<BGValue>> getBGReadings(@Path("username") String username);

    @POST("users/{username}/bgreadings")
    Call<BGValue> postBGReading(@Body BGValue bgValue, @Path("username") String username);
}
