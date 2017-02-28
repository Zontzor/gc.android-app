package com.example.alex.glucosecoach.activities;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.controller.ApiManager;
import com.example.alex.glucosecoach.controller.TokenManager;
import com.example.alex.glucosecoach.controller.UserManager;
import com.example.alex.glucosecoach.models.InsValue;
import com.example.alex.glucosecoach.services.InsService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alex on 1/25/17.
 */

public class AddInsulinActivity extends AppCompatActivity {
    private RadioGroup _insTypeRadio;
    private RadioButton _insTypeRadioButton;
    private EditText _insValueText;
    private EditText _insTimeText;
    private Button _sumbitButton;

    ApiManager _apiManager;
    UserManager _userManager;
    TokenManager _tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insulin);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("BG Reading");

        _apiManager = new ApiManager();
        _userManager = new UserManager(this);
        _tokenManager = new TokenManager(this);

        _insTypeRadio = (RadioGroup) findViewById(R.id.radio_ins_type);
        _insValueText = (EditText) findViewById(R.id.editText_ins_value);
        _insTimeText = (EditText) findViewById(R.id.editText_ins_time);
        _sumbitButton = (Button) findViewById(R.id.btn_sumbit_ins);

        Date date = new Date();
        String strDateFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        _insTimeText.setText(sdf.format(date));

        _insTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calender = Calendar.getInstance();
                int hour = calender.get(Calendar.HOUR_OF_DAY);
                int minute = calender.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddInsulinActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
                                _insValueText.setText(hourOfDay + ":" + minuteOfHour);
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

                int selectedId=_insTypeRadio.getCheckedRadioButtonId();
                _insTypeRadioButton = (RadioButton)findViewById(selectedId);

                InsValue insValue = new InsValue(_insTypeRadioButton.getText().toString(), Double.parseDouble(_insValueText.getText().toString()), formateDateTime(_insTimeText.getText().toString()));

                InsService insService = _apiManager.getInsService(_tokenManager.getToken());
                Call<InsValue> call = insService.postInsDosage(insValue, _userManager.getUsername());
                call.enqueue(new Callback<InsValue >() {
                    @Override
                    public void onResponse(Call<InsValue> call, Response<InsValue> response) {

                        if (response.isSuccessful()) {
                            Log.d("insvalue", "Successful post");
                            finish();
                        } else {
                        }
                    }

                    @Override
                    public void onFailure(Call<InsValue> call, Throwable t) {
                        Log.d("Error", t.getMessage());
                    }
                });
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String insValue = _insValueText.getText().toString();
        String insTime = _insTimeText.getText().toString();

        if (insValue.isEmpty() || !insValue.matches("^\\d{0,2}(?:\\.\\d)?$")) {
            _insValueText.setError("enter a valid value");
            valid = false;
        } else {
            _insValueText.setError(null);
        }

        if (insTime.isEmpty() || !insTime.matches("^([01]\\d|2[0-3]):?([0-5]\\d)$")) {
            _insValueText.setError("enter a valid value");
            valid = false;
        } else {
            _insValueText.setError(null);
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
