package com.example.alex.glucosecoach.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.activities.LoginActivity;
import com.example.alex.glucosecoach.controller.ApiManager;
import com.example.alex.glucosecoach.controller.TokenManager;
import com.example.alex.glucosecoach.controller.UserManager;
import com.example.alex.glucosecoach.models.Fact;
import com.example.alex.glucosecoach.models.InsValue;
import com.example.alex.glucosecoach.services.FactService;
import com.example.alex.glucosecoach.services.InsService;
import com.example.alex.glucosecoach.services.PredictionService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private TextView _txtBGValue, _txtInsulinValue, _txtCarbsValue, _txtExerciseValue;
    private Button _btnStartPredictionActivity;

    ApiManager _apiManager;
    TokenManager _tokenManager;
    UserManager _userManager;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        populateMainScreen();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        _tokenManager = new TokenManager(getActivity());

        if (isLoggedIn()) {
            _apiManager = new ApiManager(_tokenManager.getToken());
            _userManager = new UserManager(getActivity());

            // Setup Main Screen Text Views
            _txtBGValue = (TextView) view.findViewById(R.id.txt_last_bg_value);
            _txtInsulinValue = (TextView) view.findViewById(R.id.txt_last_ins_value);
            _txtCarbsValue = (TextView) view.findViewById(R.id.txt_last_carbs_value);
            _txtExerciseValue = (TextView) view.findViewById(R.id.txt_last_exrc_value);

            _btnStartPredictionActivity = (Button) view.findViewById(R.id.btn_start_predicition);
            _btnStartPredictionActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FactService factService = _apiManager.getFactService();
                    Call<Fact> call = factService.getFact(_userManager.getUsername());
                    call.enqueue(new Callback<Fact>() {
                        @Override
                        public void onResponse(Call<Fact> call, Response<Fact> response) {
                            if (response.isSuccessful()) {
                                Fact fact = response.body();

                                PredictionService predictionService = _apiManager.getPredictionService();
                                Call<Double> call2 = predictionService.getPrediction(fact, _userManager.getUsername());
                                call2.enqueue(new Callback<Double>() {
                                    @Override
                                    public void onResponse(Call<Double> call, final Response<Double> response) {
                                        if (response.isSuccessful()) {
                                            Double insulinValue = response.body();
                                            createDialog(insulinValue);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Double> call, Throwable t) {
                                    }
                                });
                            } else if (response.code() == 401) {
                                startLoginActivity();
                            }
                        }

                        @Override
                        public void onFailure(Call<Fact> call, Throwable t) {
                        }
                    });
                }
            });
        } else {
            startLoginActivity();
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void populateMainScreen() {
        // Populate main screen with data
        FactService factService = _apiManager.getFactService();
        Call<Fact> call = factService.getFact(_userManager.getUsername());
        call.enqueue(new Callback<Fact>() {
            @Override
            public void onResponse(Call<Fact> call, Response<Fact> response) {
                if (response.isSuccessful()) {
                    Fact fact = response.body();
                    _txtBGValue.setText(getString(R.string.main_bg, fact.getBgValue().toString()));
                    _txtInsulinValue.setText(getString(R.string.main_ins, fact.getInsValue().toString()));
                    _txtCarbsValue.setText(getString(R.string.main_carbs, fact.getFoodValue().toString()));
                    _txtExerciseValue.setText(getString(R.string.main_exer, fact.getExerciseValue().toString()));
                } else if (response.code() == 401) {
                    startLoginActivity();
                }
            }

            @Override
            public void onFailure(Call<Fact> call, Throwable t) {
            }
        });
    }

    public boolean isLoggedIn() {
        return _tokenManager.hasToken();
    }

    public void startLoginActivity() {
        Intent loginActivity = new Intent(getActivity(), LoginActivity.class);
        loginActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginActivity);
    }

    public void createDialog(final Double insulinValue) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Recommended Insulin")
                .setMessage(insulinValue.toString())
                .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        Date date = new Date();

                        InsValue insValue = new InsValue("novorapid", insulinValue, dateFormat.format(date));

                        InsService insService = _apiManager.getInsService();
                        Call<InsValue> call = insService.postInsDosage(insValue, _userManager.getUsername());
                        call.enqueue(new Callback<InsValue>() {
                            @Override
                            public void onResponse(Call<InsValue> call, Response<InsValue> response) {

                                if (response.isSuccessful()) {
                                    Log.d("insvalue", "Successful post");
                                    populateMainScreen();
                                } else {
                                    Log.d("insvalue", "Unsuccessful post");
                                }
                            }

                            @Override
                            public void onFailure(Call<InsValue> call, Throwable t) {
                                Log.d("Error", t.getMessage());
                            }
                        });
                    }
                })
                .create()
                .show();
    }
}
