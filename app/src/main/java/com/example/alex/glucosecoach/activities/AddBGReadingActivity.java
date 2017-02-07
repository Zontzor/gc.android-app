package com.example.alex.glucosecoach.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.controller.UserManager;
import com.example.alex.glucosecoach.models.BGValue;
import com.example.alex.glucosecoach.controller.ApiManager;

import java.text.DateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alex on 1/22/17.
 */

public class AddBGReadingActivity extends AppCompatActivity {
    private EditText _bgValueText;
    private EditText _bgTimeText;
    private Button _sumbitButton;

    private ApiManager apiService;
    UserManager userManager;

    BGValue testValue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bg_reading_add);

        apiService = new ApiManager();
        userManager = new UserManager(this);

        _bgValueText = (EditText) findViewById(R.id.editText_bg_value);
        _bgTimeText = (EditText) findViewById(R.id.editText_bg_time);
        _sumbitButton = (Button) findViewById(R.id.btn_sumbit_bg);

        _bgTimeText.setHint(DateFormat.getDateTimeInstance().format(new Date()));

        _sumbitButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String bgValue = _bgValueText.getText().toString();
                if (bgValue.matches("[0-9.]+"))  {
                    try {
                        testValue = new BGValue(userManager.getUsername(), Double.parseDouble(bgValue), "2016-12-19 08:00:00");
                        Call<BGValue> bgCall = apiService.getBGService().postUserBGReading(testValue, testValue.getUsername());
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
