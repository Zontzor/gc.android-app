package com.example.alex.glucosecoach.services;

import com.example.alex.glucosecoach.models.Exercise;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by alex on 2/28/17.
 */

public interface ExerciseService {
    @GET("exercises")
    Call<List<Exercise>> getExercises();
}
