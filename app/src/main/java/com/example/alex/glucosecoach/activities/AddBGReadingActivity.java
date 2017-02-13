package com.example.alex.glucosecoach.activities;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.controller.TokenManager;
import com.example.alex.glucosecoach.controller.UserManager;
import com.example.alex.glucosecoach.controller.ApiManager;
import com.example.alex.glucosecoach.models.BGValue;
import com.example.alex.glucosecoach.services.BGService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    ApiManager _apiService;
    UserManager _userManager;
    TokenManager _tokenManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bg);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("BG Reading");

        _apiService = new ApiManager();
        _userManager = new UserManager(this);
        _tokenManager = new TokenManager(this);

        _bgValueText = (EditText) findViewById(R.id.editText_bg_value);
        _bgTimeText = (EditText) findViewById(R.id.editText_bg_time);
        _sumbitButton = (Button) findViewById(R.id.btn_sumbit_bg);

        Date date = new Date();
        String strDateFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        _bgTimeText.setText(sdf.format(date));

        _bgTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calender = Calendar.getInstance();
                int hour = calender.get(Calendar.HOUR_OF_DAY);
                int minute = calender.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddBGReadingActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
                                _bgTimeText.setText(hourOfDay + ":" + minuteOfHour);
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        _sumbitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validate()) {
                    return;
                }

                BGValue bgValue = new BGValue(Double.parseDouble(_bgValueText.getText().toString()), formateDateTime(_bgTimeText.getText().toString()));

                BGService bgService = _apiService.getBGService(_tokenManager.getToken());
                Call<BGValue> call = bgService.postBGReading(bgValue, _userManager.getUsername());
                call.enqueue(new Callback<BGValue >() {
                    @Override
                    public void onResponse(Call<BGValue> call, Response<BGValue> response) {

                        if (response.isSuccessful()) {
                            Log.d("bgreading", "Successful post");
                            finish();
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

    public boolean validate() {
        boolean valid = true;

        String bgValue = _bgValueText.getText().toString();
        String bgTime = _bgTimeText.getText().toString();

        if (bgValue.isEmpty() || !bgValue.matches("^\\d{0,2}(?:\\.\\d)?$")) {
            _bgValueText.setError("enter a valid value");
            valid = false;
        } else {
            _bgValueText.setError(null);
        }

        if (bgTime.isEmpty() || !bgTime.matches("^([01]\\d|2[0-3]):?([0-5]\\d)$")) {
            _bgTimeText.setError("enter a valid value");
            valid = false;
        } else {
            _bgTimeText.setError(null);
        }

        return valid;
    }

    public String formateDateTime(String time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date) + " " + time + ":00";
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
