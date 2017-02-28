package com.example.alex.glucosecoach.services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by alex on 2/27/17.
 */

public interface PredictionService {
    @GET("users/predict/{username}")
    Call<Float> getPrediction(@Path("username") String username);
}
