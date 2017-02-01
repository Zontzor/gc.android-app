package com.example.alex.glucosecoach.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.controller.RestManager;
import com.example.alex.glucosecoach.models.InsValue;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alex on 1/25/17.
 */

public class AddInsulinActivity extends Activity {
    private EditText editTextInsValue;
    private EditText editTextInsTimeStamp;
    private Button btnSubmit;
    private RestManager apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insulin_add);

        apiService = new RestManager();

        editTextInsValue = (EditText) findViewById(R.id.editText_bg_value);
        editTextInsTimeStamp = (EditText) findViewById(R.id.editText_bg_time);
        btnSubmit = (Button) findViewById(R.id.btn_sumbit_bg);




    }
}
