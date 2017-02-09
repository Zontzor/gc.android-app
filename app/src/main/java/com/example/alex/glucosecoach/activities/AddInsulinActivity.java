package com.example.alex.glucosecoach.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.controller.ApiManager;

/**
 * Created by alex on 1/25/17.
 */

public class AddInsulinActivity extends Activity {
    private EditText editTextInsValue;
    private EditText editTextInsTimeStamp;
    private Button btnSubmit;
    private ApiManager apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insulin);

        apiService = new ApiManager();

        editTextInsValue = (EditText) findViewById(R.id.editText_ins_value);
        editTextInsTimeStamp = (EditText) findViewById(R.id.editText_ins_time);
        btnSubmit = (Button) findViewById(R.id.btn_sumbit_bg);




    }
}
