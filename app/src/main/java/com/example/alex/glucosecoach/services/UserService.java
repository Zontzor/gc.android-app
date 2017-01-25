package com.example.alex.glucosecoach.services;

import com.example.alex.glucosecoach.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by alex on 1/24/17.
 */

public interface UserService {

    @GET("users")
    Call<List<User>> getAllUsers();

    @GET("users/{username}")
    Call<User> getUser(@Path("username") String username);
}
