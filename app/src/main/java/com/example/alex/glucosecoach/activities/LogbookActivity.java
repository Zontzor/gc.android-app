package com.example.alex.glucosecoach.activities;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.adapters.LogAdapter;
import com.example.alex.glucosecoach.controller.ApiManager;
import com.example.alex.glucosecoach.controller.TokenManager;
import com.example.alex.glucosecoach.controller.UserManager;
import com.example.alex.glucosecoach.models.Fact;
import com.example.alex.glucosecoach.services.FactService;
import com.example.alex.glucosecoach.services.PredictionService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alex on 3/7/17.
 */

public class LogbookActivity extends AppCompatActivity {
    ListView listView;
    ApiManager _apiManager;
    TokenManager _tokenManager;
    UserManager _userManager;
    List<Fact> facts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logbook);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Logbook");
        }

        _userManager = new UserManager(this);
        _tokenManager = new TokenManager(this);
        _apiManager = new ApiManager(_tokenManager.getToken());

        FactService factService = _apiManager.getFactService();
        Call<List<Fact>> call = factService.getFacts(_userManager.getUsername());
        call.enqueue(new Callback<List<Fact>>() {
            @Override
            public void onResponse(Call<List<Fact>> call, Response<List<Fact>> response) {
                if (response.isSuccessful()) {
                    facts = response.body();
                    populateList();
                }
            }

            @Override
            public void onFailure(Call<List<Fact>> call, Throwable t) {
            }
        });
    }

    public void populateList() {
        listView = (ListView) findViewById(R.id.menu_list_logbook);
        listView.setAdapter(new LogAdapter(this, facts));
    }
}
