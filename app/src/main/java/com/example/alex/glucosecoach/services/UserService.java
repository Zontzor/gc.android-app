package com.example.alex.glucosecoach.services;

import com.example.alex.glucosecoach.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by alex on 1/24/17.
 */

public interface UserService {

    @GET("users/usernames/{username}")
    Call<String> getUsername(@Path("username") String username);

    @GET("users")
    Call<List<User>> getAllUsers();

    @GET("users/{username}")
    Call<User> getUser(@Path("username") String username);

    @POST("users")
    Call<User> postUser(@Body User user);
}
