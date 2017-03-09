package com.example.alex.glucosecoach.activities;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.controller.ApiManager;
import com.example.alex.glucosecoach.controller.TokenManager;
import com.example.alex.glucosecoach.controller.UserManager;
import com.example.alex.glucosecoach.models.Exercise;
import com.example.alex.glucosecoach.models.ExerciseLog;
import com.example.alex.glucosecoach.services.ExerciseLogService;
import com.example.alex.glucosecoach.services.ExerciseService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.id.list;

/**
 * Created by alex on 1/25/17.
 */

public class AddExerciseLogActivity extends AppCompatActivity {
    private Spinner _exerTypeSpin;
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

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add Exercise Log");
        }

        _userManager = new UserManager(this);
        _tokenManager = new TokenManager(this);
        _apiService = new ApiManager(_tokenManager.getToken());

        _exerTypeSpin = (Spinner) findViewById(R.id.spin_exercise_type);
        _exerValueText = (EditText) findViewById(R.id.editText_exer_value);
        _exerTimeText = (EditText) findViewById(R.id.editText_exer_time);
        _sumbitButton = (Button) findViewById(R.id.btn_sumbit_exer);

        setSpinnerItems();

        Date date = new Date();
        String strDateFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        _exerTimeText.setText(sdf.format(date));

        _exerTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calender = Calendar.getInstance();
                int hour = calender.get(Calendar.HOUR_OF_DAY);
                int minute = calender.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddExerciseLogActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
                                _exerTimeText.setText(hourOfDay + ":" + minuteOfHour);
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

                Exercise exercise = (Exercise) _exerTypeSpin.getSelectedItem();

                ExerciseLog exerciseLog = new ExerciseLog(
                        exercise.getId(),
                        Integer.parseInt(_exerValueText.getText().toString()),
                        formateDateTime(_exerTimeText.getText().toString()));

                ExerciseLogService exerciseLogService = _apiService.getExerciseLogService();
                Call<ExerciseLog> call = exerciseLogService.postExerciseLog(exerciseLog, _userManager.getUsername());
                call.enqueue(new Callback<ExerciseLog>() {
                    @Override
                    public void onResponse(Call<ExerciseLog> call, Response<ExerciseLog> response) {

                        if (response.isSuccessful()) {
                            Log.d("exercise_log", "Successful post");
                            finish();
                        } else {
                            Log.d("exercise_log", "Unsuccessful post");
                        }
                    }

                    @Override
                    public void onFailure(Call<ExerciseLog> call, Throwable t) {
                        Log.d("Error", t.getMessage());
                    }
                });
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String bgValue = _exerValueText.getText().toString();
        String bgTime = _exerTimeText.getText().toString();

        if (bgValue.isEmpty() || !bgValue.matches("(?<![-.])\\b[0-9]+\\b(?!\\.[0-9])")) {
            _exerValueText.setError("enter a valid value");
            valid = false;
        } else {
            _exerValueText.setError(null);
        }

        if (bgTime.isEmpty() || !bgTime.matches("^([01]\\d|2[0-3]):?([0-5]\\d)$")) {
            _exerTimeText.setError("enter a valid value");
            valid = false;
        } else {
            _exerTimeText.setError(null);
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

    public void setSpinnerItems() {
        ExerciseService exerciseService = _apiService.getExerciseService();
        Call<List<Exercise>> call = exerciseService.getExercises();
        call.enqueue(new Callback<List<Exercise> >() {
            @Override
            public void onResponse(Call<List<Exercise>> call, Response<List<Exercise>> response) {

                if (response.isSuccessful()) {
                    ArrayAdapter<Exercise> dataAdapter = new ArrayAdapter<>(AddExerciseLogActivity.this,
                            android.R.layout.simple_spinner_item, response.body());
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    _exerTypeSpin.setAdapter(dataAdapter);
                } else {
                    Log.d("exercises", "Failed to get exercises list");
                }
            }

            @Override
            public void onFailure(Call<List<Exercise>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }
}
