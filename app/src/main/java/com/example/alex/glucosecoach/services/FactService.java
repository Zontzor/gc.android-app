package com.example.alex.glucosecoach.services;

import com.example.alex.glucosecoach.models.Fact;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by alex on 2/28/17.
 */

public interface FactService {
    @GET("facts/{username}/latest")
    Call<Fact> getFact(@Path("username") String username);

    @GET("facts/{username}")
    Call<List<Fact>> getFacts(@Path("username") String username);
}
