package com.example.alex.glucosecoach.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.activities.LoginActivity;
import com.example.alex.glucosecoach.controller.ApiManager;
import com.example.alex.glucosecoach.controller.TokenManager;
import com.example.alex.glucosecoach.controller.UserManager;
import com.example.alex.glucosecoach.models.Fact;
import com.example.alex.glucosecoach.models.InsValue;
import com.example.alex.glucosecoach.models.User;
import com.example.alex.glucosecoach.services.FactService;
import com.example.alex.glucosecoach.services.InsService;
import com.example.alex.glucosecoach.services.PredictionService;
import com.example.alex.glucosecoach.services.UserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alex on 3/9/17.
 */

public class HomeFragment extends AppCompatActivity {
    private TextView _txtBGValue, _txtInsulinValue, _txtCarbsValue, _txtExerciseValue;
    private Button _btnStartPredictionActivity;
    final Context _context = this;

    ApiManager _apiManager;
    TokenManager _tokenManager;
    UserManager _userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _tokenManager = new TokenManager(_context);
        _apiManager = new ApiManager(_tokenManager.getToken());
        _userManager = new UserManager(_context);

        if (isLoggedIn()) {
            _userManager = new UserManager(_context);
            loadContent();
        } else {
            startLoginActivity();
        }
    }

    protected void onResume() {
        super.onResume();

        populateMainScreen();
    }

    public void loadContent() {



        // Setup Main Screen Text Views
        _txtBGValue = (TextView) findViewById(R.id.txt_last_bg_value);
        _txtInsulinValue = (TextView) findViewById(R.id.txt_last_ins_value);
        _txtCarbsValue = (TextView) findViewById(R.id.txt_last_carbs_value);
        _txtExerciseValue = (TextView) findViewById(R.id.txt_last_exrc_value);

        // Create API Manager instance
        _apiManager = new ApiManager(_tokenManager.getToken());

        _btnStartPredictionActivity = (Button) findViewById(R.id.btn_start_predicition);
        _btnStartPredictionActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FactService factService = _apiManager.getFactService();
                Call<Fact> call = factService.getFact(_userManager.getUsername());
                call.enqueue(new Callback<Fact>() {
                    @Override
                    public void onResponse(Call<Fact> call, Response<Fact> response) {
                        if (response.isSuccessful()) {
                            Fact fact = response.body();

                            PredictionService predictionService = _apiManager.getPredictionService();
                            Call<Double> call2 = predictionService.getPrediction(fact, _userManager.getUsername());
                            call2.enqueue(new Callback<Double>() {
                                @Override
                                public void onResponse(Call<Double> call, final Response<Double> response) {
                                    if (response.isSuccessful()) {
                                        new AlertDialog.Builder(_context)
                                                .setTitle("Recommended Insulin")
                                                .setMessage(response.body().toString())
                                                .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                                                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                                    @Override public void onClick(DialogInterface dialog, int which) {
                                                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                                        Date date = new Date();

                                                        InsValue insValue = new InsValue("novorapid", response.body(), dateFormat.format(date));

                                                        InsService insService = _apiManager.getInsService();
                                                        Call<InsValue> call = insService.postInsDosage(insValue, _userManager.getUsername());
                                                        call.enqueue(new Callback<InsValue >() {
                                                            @Override
                                                            public void onResponse(Call<InsValue> call, Response<InsValue> response) {

                                                                if (response.isSuccessful()) {
                                                                    Log.d("insvalue", "Successful post");
                                                                    populateMainScreen();
                                                                } else {
                                                                    Log.d("insvalue", "Unsuccessful post");
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<InsValue> call, Throwable t) {
                                                                Log.d("Error", t.getMessage());
                                                            }
                                                        });
                                                    }
                                                })
                                                .create()
                                                .show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Double> call, Throwable t) {
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<Fact> call, Throwable t) {
                    }
                });
            }
        });

        // Get user info from API and fill in side menu info
        getUser();

        populateMainScreen();
    }

    public boolean isLoggedIn() {
        if (!_tokenManager.hasToken()) {
            return false;
        } else {
            return true;
        }
    }

    public void getUser() {
        UserService userService = _apiManager.getUserService();
        Call<User> call = userService.getUser(_userManager.getUsername());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    TextView _usernameText = (TextView) findViewById(R.id.txt_username);
                    TextView _emailText = (TextView) findViewById(R.id.txt_email);

                    _usernameText.setText(response.body().getUsername());
                    _emailText.setText(response.body().getEmail());

                    _userManager.setEmail(response.body().getEmail());
                } else {
                    // error response, no access to resource?
                    Log.d("authentication", "Incorrect login details");
                    startLoginActivity();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // something went completely south (like no internet connection)
            }
        });
    }

    public void populateMainScreen() {
        // Populate main screen with data
        FactService factService = _apiManager.getFactService();
        Call<Fact> call = factService.getFact(_userManager.getUsername());
        call.enqueue(new Callback<Fact>() {
            @Override
            public void onResponse(Call<Fact> call, Response<Fact> response) {
                if (response.isSuccessful()) {
                    Fact fact = response.body();
                    _txtBGValue.setText(getString(R.string.main_bg, fact.getBgValue().toString()));
                    _txtInsulinValue.setText(getString(R.string.main_ins, fact.getInsValue().toString()));
                    _txtCarbsValue.setText(getString(R.string.main_carbs, fact.getFoodValue().toString()));
                    _txtExerciseValue.setText(getString(R.string.main_exer, fact.getExerciseValue().toString()));
                }
            }

            @Override
            public void onFailure(Call<Fact> call, Throwable t) {
            }
        });
    }

    public void startLoginActivity() {
        Intent loginActivity = new Intent(this, LoginActivity.class);
        loginActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginActivity);
    }
}
