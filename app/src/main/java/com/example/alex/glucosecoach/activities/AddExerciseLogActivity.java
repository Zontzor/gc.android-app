package com.example.alex.glucosecoach.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.controller.ApiManager;
import com.example.alex.glucosecoach.controller.TokenManager;
import com.example.alex.glucosecoach.controller.UserManager;

/**
 * Created by alex on 1/25/17.
 */

public class AddExerciseLogActivity extends AppCompatActivity {
    private EditText _exerValueText;
    private EditText _exerTimeText;
    private Button _sumbitButton;

    ApiManager _apiService;
    UserManager _userManager;
    TokenManager _tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_log);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Exercise Log");

        _apiService = new ApiManager();
        _userManager = new UserManager(this);
        _tokenManager = new TokenManager(this);

        _exerValueText = (EditText) findViewById(R.id.editText_bg_value);
        _exerTimeText = (EditText) findViewById(R.id.editText_bg_time);
        _sumbitButton = (Button) findViewById(R.id.btn_sumbit_bg);

    }
}
