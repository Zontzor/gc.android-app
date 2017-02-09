package com.example.alex.glucosecoach.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.controller.TokenManager;
import com.example.alex.glucosecoach.controller.UserManager;
import com.example.alex.glucosecoach.controller.ApiManager;
import com.example.alex.glucosecoach.models.BGValue;
import com.example.alex.glucosecoach.services.BGService;

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

    ApiManager apiService;
    UserManager userManager;
    TokenManager tokenManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bg);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("BG Reading");

        apiService = new ApiManager();
        userManager = new UserManager(this);
        tokenManager = new TokenManager(this);

        _bgValueText = (EditText) findViewById(R.id.editText_bg_value);
        _bgTimeText = (EditText) findViewById(R.id.editText_bg_time);
        _sumbitButton = (Button) findViewById(R.id.btn_sumbit_bg);

        _bgTimeText.setHint(DateFormat.getDateTimeInstance().format(new Date()));

        _sumbitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                BGValue bgValue = new BGValue(Double.parseDouble(_bgValueText.getText().toString()), _bgTimeText.getText().toString());

                BGService bgService = apiService.getBGService(tokenManager.getToken());
                Call<BGValue> call = bgService.postBGReading(bgValue, userManager.getUsername());
                call.enqueue(new Callback<BGValue >() {
                    @Override
                    public void onResponse(Call<BGValue> call, Response<BGValue> response) {

                        if (response.isSuccessful()) {
                            Log.d("bgreading", "Successful post");
                        } else {
                        }
                    }

                    @Override
                    public void onFailure(Call<BGValue> call, Throwable t) {
                        Log.d("Error", t.getMessage());
                    }
                });
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
