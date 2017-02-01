package com.example.alex.glucosecoach.services;

import com.example.alex.glucosecoach.models.InsValue;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by alex on 1/31/17.
 */

public interface InsService {
    @GET("users/{username}/insdosages")
    Call<List<InsValue>> getUsersInsDosages(@Path("username") String username);

    @POST("users/{username}/insdosages")
    Call<InsValue> postUserInsDosage(@Body InsValue bgValue, @Path("username") String username);
}
