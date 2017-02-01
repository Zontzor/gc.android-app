package com.example.alex.glucosecoach.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.models.BGValue;
import com.example.alex.glucosecoach.controller.RestManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alex on 1/22/17.
 */

public class AddBGReadingActivity extends Activity {
    private EditText _bgValueText;
    private EditText _bgTimeText;
    private Button _sumbitButton;
    private RestManager _apiService;

    BGValue testValue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bg_reading_add);

        _apiService = new RestManager();

        _bgValueText = (EditText) findViewById(R.id.editText_bg_value);
        _bgTimeText = (EditText) findViewById(R.id.editText_bg_time);
        _sumbitButton = (Button) findViewById(R.id.btn_sumbit_bg);

        _sumbitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bgValue = _bgValueText.getText().toString();
                if (bgValue.matches("[0-9.]+"))  {
                    try {
                        testValue = new BGValue("Test", Double.parseDouble(bgValue), "2016-12-19 08:00:00");
                        Call<BGValue> bgCall = _apiService.getBGService().postUserBGReading(testValue, testValue.getUsername());
                        bgCall.enqueue(new Callback<BGValue>() {
                            @Override
                            public void onResponse(Call<BGValue> call, Response<BGValue> response) {
                                if (response.isSuccessful()) {
                                    Log.d("postBgValue", "Successful post");
                                } else {
                                    Log.d("postBgValue", "Unsuccessful post");
                                }
                            }

                            @Override
                            public void onFailure(Call<BGValue> call, Throwable t) {

                            }
                        });
                    } catch (Exception ex) {
                        Log.d("connection", "Unsuccessful connection");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Enter a BG Value", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
