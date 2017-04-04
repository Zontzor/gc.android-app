package com.example.alex.glucosecoach.activities;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.controller.ApiManager;
import com.example.alex.glucosecoach.controller.TokenManager;
import com.example.alex.glucosecoach.controller.UserManager;
import com.example.alex.glucosecoach.models.Food;
import com.example.alex.glucosecoach.models.FoodLog;
import com.example.alex.glucosecoach.services.FoodLogService;
import com.example.alex.glucosecoach.services.FoodService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alex on 1/25/17.
 */

public class AddFoodLogActivity extends AppCompatActivity {
    private Spinner _foodTypeSpin;
    private EditText _foodValueText;
    private EditText _foodTimeText;
    private Button _sumbitButton;

    ApiManager _apiService;
    UserManager _userManager;
    TokenManager _tokenManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_log);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add Food Log");
        }

        _userManager = new UserManager(this);
        _tokenManager = new TokenManager(this);
        _apiService = new ApiManager(_tokenManager.getToken());

        _foodTypeSpin = (Spinner) findViewById(R.id.spin_food_type);
        _foodValueText = (EditText) findViewById(R.id.editText_food_value);
        _foodTimeText = (EditText) findViewById(R.id.editText_food_time);
        _sumbitButton = (Button) findViewById(R.id.btn_sumbit_food_log);

        setSpinnerItems();

        Date date = new Date();
        String strDateFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        _foodTimeText.setText(sdf.format(date));

        _foodTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calender = Calendar.getInstance();
                int hour = calender.get(Calendar.HOUR_OF_DAY);
                int minute = calender.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddFoodLogActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
                                _foodTimeText.setText(hourOfDay + ":" + minuteOfHour);
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        _sumbitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    return;
                }

                Food food = (Food) _foodTypeSpin.getSelectedItem();

                FoodLog foodLog = new FoodLog(
                        food.getId(),
                        Double.parseDouble(_foodValueText.getText().toString()),
                        formateDateTime(_foodTimeText.getText().toString()));

                FoodLogService foodLogService = _apiService.getFoodLogService();
                Call<FoodLog> call = foodLogService.postFoodLog(foodLog, _userManager.getUsername());
                call.enqueue(new Callback<FoodLog>() {
                    @Override
                    public void onResponse(Call<FoodLog> call, Response<FoodLog> response) {

                        if (response.isSuccessful()) {
                            Log.d("food_log", "Successful post");
                            finish();
                        } else {
                            Log.d("food_log", "Unsuccessful post");
                        }
                    }

                    @Override
                    public void onFailure(Call<FoodLog> call, Throwable t) {
                        Log.d("Error", t.getMessage());
                    }
                });
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String bgValue = _foodValueText.getText().toString();
        String bgTime = _foodTimeText.getText().toString();

        if (bgValue.isEmpty() || !bgValue.matches("^\\d{0,2}(?:\\.\\d)?$")) {
            _foodValueText.setError("enter a valid value");
            valid = false;
        } else {
            _foodValueText.setError(null);
        }

        if (bgTime.isEmpty() || !bgTime.matches("^([01]\\d|2[0-3]):?([0-5]\\d)$")) {
            _foodTimeText.setError("enter a valid value");
            valid = false;
        } else {
            _foodTimeText.setError(null);
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
        FoodService foodService = _apiService.getFoodService();

        foodService.getFoodsObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Food>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Food> value) {
                ArrayAdapter<Food> dataAdapter = new ArrayAdapter<>(AddFoodLogActivity.this,
                        android.R.layout.simple_spinner_item, value);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                _foodTypeSpin.setAdapter(dataAdapter);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("food_log", "Error fetching foods");
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
