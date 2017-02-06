package com.example.alex.glucosecoach.controller;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.alex.glucosecoach.services.AuthenticationInterceptor;
import com.example.alex.glucosecoach.services.BGService;
import com.example.alex.glucosecoach.services.InsService;
import com.example.alex.glucosecoach.services.LoginService;
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

    private static Retrofit retrofit;

    private UserService userService;
    private BGService bgService;
    private InsService insService;

    private static TokenManager tokenManager = new TokenManager();;

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public UserService getUserService(Context context) {
        String authToken = Credentials.basic(tokenManager.getToken(context), "unused");

        /*if (userService == null) {
            retrofit = builder.build();

            userService = retrofit.create(UserService.class);
            Log.d("UserService", "Created retrofit userService instance");
        }*/

        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);

                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }

        return retrofit.create(UserService.class);
    }

    public BGService getBGService() {
        if (bgService == null) {
            retrofit = builder.build();

            bgService = retrofit.create(BGService.class);
            Log.d("BGService", "Created retrofit bgService instance");
        }

        return bgService;
    }

    public InsService getInsService() {
        if (insService == null) {
            retrofit = builder.build();

            insService = retrofit.create(InsService.class);
            Log.d("BGService", "Created retrofit bgService instance");
        }

        return insService;
    }

    public LoginService getLoginService(String username, String password) {
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            String authToken = Credentials.basic(username, password);
            return getLoginService(authToken);
        }

        return getLoginService(null);
    }

    public LoginService getLoginService(String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);

                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }

        return retrofit.create(LoginService.class);
    }
}
