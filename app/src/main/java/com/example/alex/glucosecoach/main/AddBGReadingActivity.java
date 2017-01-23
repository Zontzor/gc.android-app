package com.example.alex.glucosecoach.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.alex.glucosecoach.R;

/**
 * Created by alex on 1/22/17.
 */

public class AddBGReadingActivity extends Activity {
    private EditText editTextBGValue;
    private EditText editTextBGTimeStamp;
    private Button btnSubmit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bg_reading_add);

        editTextBGValue = (EditText) findViewById(R.id.editText_bg_value);
        editTextBGTimeStamp = (EditText) findViewById(R.id.editText_bg_time);
        btnSubmit = (Button) findViewById(R.id.btn_sumbit_bg);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
