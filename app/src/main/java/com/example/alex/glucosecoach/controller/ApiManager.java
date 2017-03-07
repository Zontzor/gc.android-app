package com.example.alex.glucosecoach.controller;

import com.example.alex.glucosecoach.auth.AuthenticationInterceptor;
import com.example.alex.glucosecoach.services.BGService;
import com.example.alex.glucosecoach.services.ExerciseLogService;
import com.example.alex.glucosecoach.services.ExerciseService;
import com.example.alex.glucosecoach.services.FoodLogService;
import com.example.alex.glucosecoach.services.FoodService;
import com.example.alex.glucosecoach.services.InsService;
import com.example.alex.glucosecoach.services.LoginService;
import com.example.alex.glucosecoach.services.FactService;
import com.example.alex.glucosecoach.services.PredictionService;
import com.example.alex.glucosecoach.services.UserService;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alex on 1/24/17.
 */

public class ApiManager {

    private static String BASE_URL = "http://192.168.1.101:5000/glucose_coach/api/v1.0/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit retrofit;

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private String authToken;
    private AuthenticationInterceptor interceptor;

    public ApiManager(){}

    public ApiManager(String token) {
        this.authToken = Credentials.basic(token, "unused");
        this.interceptor = new AuthenticationInterceptor(this.authToken);

        httpClient.addInterceptor(interceptor);

        builder.client(httpClient.build());
        retrofit = builder.build();
    }

    public ApiManager(String username, String password) {
        this.authToken = Credentials.basic(username, password);
        this.interceptor = new AuthenticationInterceptor(this.authToken);

        httpClient.addInterceptor(interceptor);

        builder.client(httpClient.build());
        retrofit = builder.build();
    }

    public LoginService getLoginService() {return retrofit.create(LoginService.class);}

    public UserService getUserService() {return retrofit.create(UserService.class);}

    public BGService getBGService() {return retrofit.create(BGService.class);}

    public InsService getInsService() {return retrofit.create(InsService.class);}

    public FoodLogService getFoodLogService() {return retrofit.create(FoodLogService.class);}

    public ExerciseLogService getExerciseLogService() {return retrofit.create(ExerciseLogService.class);}

    public FoodService getFoodService() {return retrofit.create(FoodService.class);}

    public ExerciseService getExerciseService() {return retrofit.create(ExerciseService.class);}

    public FactService getFactService() {return retrofit.create(FactService.class);}

    public PredictionService getPredictionService() {return retrofit.create(PredictionService.class);}
}
