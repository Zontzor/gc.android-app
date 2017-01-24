package com.example.alex.glucosecoach.services;

import com.example.alex.glucosecoach.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by alex on 1/24/17.
 */

public interface UserService {

    @GET("users")
    Call<List<User>> getAllUsers();
}
