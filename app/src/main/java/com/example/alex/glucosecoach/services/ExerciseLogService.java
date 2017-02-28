package com.example.alex.glucosecoach.services;

import com.example.alex.glucosecoach.models.ExerciseLog;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by alex on 2/28/17.
 */

public interface ExerciseLogService {
    @GET("users/{username}/exerciselogs")
    Call<List<ExerciseLog>> getExerciseLogs(@Path("username") String username);

    @POST("users/{username}/exerciselogs")
    Call<ExerciseLog> postExerciseLog(@Body ExerciseLog exerciseLog, @Path("username") String username);
}
