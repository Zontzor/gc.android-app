package com.example.alex.glucosecoach.services;

import com.example.alex.glucosecoach.models.Food;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.Call;

/**
 * Created by alex on 3/7/17.
 */

public interface FoodService {
    @GET("foods")
    Call<List<Food>> getFoods();
}
