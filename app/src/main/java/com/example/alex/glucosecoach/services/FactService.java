package com.example.alex.glucosecoach.services;

import com.example.alex.glucosecoach.models.Fact;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by alex on 2/28/17.
 */

public interface FactService {
    @GET("facts/{username}")
    Call<Fact> getFact(@Path("username") String username);
}
