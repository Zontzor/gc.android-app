package com.example.alex.glucosecoach.main;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.model.User;
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

        editTextBGValue = (EditText) findViewById(R.id.editText_bg_value);
        editTextBGTimeStamp = (EditText) findViewById(R.id.editText_bg_time);
        btnSubmit = (Button) findViewById(R.id.btn_sumbit_bg);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("click", "BG submit button clicked");
                apiService = new RestManager();
                try {
                    Call<List<User>> listCall = apiService.getUserService().getAllUsers();
                    listCall.enqueue(new Callback<List<User>>() {
                        @Override
                        public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                            Log.d("onResponse", "Entered method");
                            if (response.isSuccessful()) {
                                Log.d("connection", "Successful connection");
                                List<User> userList = response.body();

                                for (int i = 0; i < userList.size(); i++) {
                                    User user = userList.get(i);
                                }
                            } else {
                                Log.d("connection", "Unsuccessful connection");
                                int statusCode = response.code();

                                switch (statusCode) {

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<User>> call, Throwable t) {

                        }
                    });
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), "Unsuccessful connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
