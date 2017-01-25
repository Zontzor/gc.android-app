package com.example.alex.glucosecoach.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.models.BGValue;
import com.example.alex.glucosecoach.models.User;
import com.example.alex.glucosecoach.controller.RestManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alex on 1/22/17.
 */

public class AddBGReadingActivity extends Activity {
    private EditText editTextBGValue;
    private EditText editTextBGTimeStamp;
    private Button btnSubmit;
    private RestManager apiService;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bg_reading_add);

        apiService = new RestManager();

        editTextBGValue = (EditText) findViewById(R.id.editText_bg_value);
        editTextBGTimeStamp = (EditText) findViewById(R.id.editText_bg_time);
        btnSubmit = (Button) findViewById(R.id.btn_sumbit_bg);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Call<List<BGValue>> userCall = apiService.getBGService().getUsersBGReadings("Neutr0n");
                    userCall.enqueue(new Callback<List<BGValue>> () {
                        @Override
                        public void onResponse(Call<List<BGValue>>  call, Response<List<BGValue>>  response) {
                            if (response.isSuccessful()) {

                                Log.d("BGReading", "Successful retrieving resource");
                            } else {
                                Log.d("connection", "Error retrieving resource" + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<BGValue>>  call, Throwable t) {

                        }
                    });
                } catch (Exception ex) {
                    Log.d("connection", "Unsuccessful connection");
                }
            }
        });
    }
}
