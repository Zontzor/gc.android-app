package com.example.alex.glucosecoach.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.activities.AddBGReadingActivity;
import com.example.alex.glucosecoach.activities.AddExerciseLogActivity;
import com.example.alex.glucosecoach.activities.AddFoodLogActivity;
import com.example.alex.glucosecoach.activities.AddInsulinActivity;
import com.example.alex.glucosecoach.activities.LoginActivity;
import com.example.alex.glucosecoach.activities.MainActivity;
import com.example.alex.glucosecoach.controller.ApiManager;
import com.example.alex.glucosecoach.controller.TokenManager;
import com.example.alex.glucosecoach.controller.UserManager;
import com.example.alex.glucosecoach.models.Fact;
import com.example.alex.glucosecoach.models.InsValue;
import com.example.alex.glucosecoach.services.FactService;
import com.example.alex.glucosecoach.services.InsService;
import com.example.alex.glucosecoach.services.PredictionService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private TextView _txtBGValue, _txtInsulinValue, _txtCarbsValue, _txtExerciseValue;
    private CardView _cardBGValue, _cardInsulinValue, _cardCarbsValue, _cardExerciseValue;
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

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Home - " + getTimeOfDay());

        _tokenManager = new TokenManager(getActivity());

        if (isLoggedIn()) {
            _apiManager = new ApiManager(_tokenManager.getToken());
            _userManager = new UserManager(getActivity());

            // Setup Main Screen Text Views
            _txtBGValue = (TextView) view.findViewById(R.id.txt_last_bg_value);
            _txtInsulinValue = (TextView) view.findViewById(R.id.txt_last_ins_value);
            _txtCarbsValue = (TextView) view.findViewById(R.id.txt_last_carbs_value);
            _txtExerciseValue = (TextView) view.findViewById(R.id.txt_last_exrc_value);

            _cardBGValue = (CardView) view.findViewById(R.id.card_bg);
            _cardInsulinValue = (CardView) view.findViewById(R.id.card_ins);
            _cardCarbsValue = (CardView) view.findViewById(R.id.card_carbs);
            _cardExerciseValue = (CardView) view.findViewById(R.id.card_exer);

            _cardBGValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent addBGReadingActivity = new Intent(getContext(), AddBGReadingActivity.class);
                    startActivity(addBGReadingActivity);
                }
            });

            _cardInsulinValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent addInsulinActivity = new Intent(getContext(), AddInsulinActivity.class);
                    startActivity(addInsulinActivity);
                }
            });

            _cardCarbsValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent addCarbsActivity = new Intent(getContext(), AddFoodLogActivity.class);
                    startActivity(addCarbsActivity);
                }
            });

            _cardExerciseValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent addExerciseActivity = new Intent(getContext(), AddExerciseLogActivity.class);
                    startActivity(addExerciseActivity);
                }
            });

            _btnStartPredictionActivity = (Button) view.findViewById(R.id.btn_start_predicition);

            _btnStartPredictionActivity.setOnClickListener(v -> {
                predictionButtonLogic();
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

                    if (checkFactDate(fact)) {
                        _txtBGValue.setText(getString(R.string.main_bg, fact.getBgValue().toString()));
                        _txtInsulinValue.setText(getString(R.string.main_ins, fact.getInsValue().toString()));
                        _txtCarbsValue.setText(getString(R.string.main_carbs, fact.getFoodValue().toString()));
                        _txtExerciseValue.setText(getString(R.string.main_exer, fact.getExerciseValue().toString()));
                    }
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

    /*
        Function to check if the date and time of day of the facts matches now,
        if the fact matches a date object of now then return true, else return false
     */
    public boolean checkFactDate(Fact fact) {
        DateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy", Locale.getDefault());
        Date factDate;

        try {
            factDate = dateFormat.parse(fact.getPfDate());

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY,0);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.SECOND,0);
            cal.set(Calendar.MILLISECOND,0);

            Date today = cal.getTime();

            if (factDate.before(today)) {
                return false;
            }

            int nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int timeOfDay;

            if (nowHour >= 7 & nowHour < 12) {
                timeOfDay = 1;
            } else if (nowHour >= 12 & nowHour < 17) {
                timeOfDay = 2;
            } else if(nowHour >= 17 & nowHour < 20) {
                timeOfDay = 3;
            } else {
                timeOfDay = 4;
            }

            return fact.getPfTimeOfDay() == timeOfDay;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    /*
        Function to get the time of day for now as a string version
     */
    public String getTimeOfDay() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (hour >= 7 & hour < 12) {
            return "Morning";
        } else if (hour >= 12 & hour < 17) {
            return "Afternoon";
        } else if(hour >= 17 & hour < 20) {
            return "Evening";
        } else {
            return "Night";
        }
    }

    public void predictionButtonLogic() {
        Context context = getContext();
        FactService factService = _apiManager.getFactService();
        Call<Fact> factCall = factService.getFact(_userManager.getUsername());
        factCall.enqueue(new Callback<Fact>() {
            @Override
            public void onResponse(Call<Fact> call, Response<Fact> response) {
                if (response.isSuccessful()) {
                    Fact fact = response.body();

                    if (checkFactDate(fact)) {
                        PredictionService predictionService = _apiManager.getPredictionService();
                        Call<Double> callPredict = predictionService.getPrediction(fact, _userManager.getUsername());
                        callPredict.enqueue(new Callback<Double>() {
                            @Override
                            public void onResponse(Call<Double> call, final Response<Double> response) {
                                if (response.isSuccessful()) {
                                    Double insulinValue = response.body();
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

                            @Override
                            public void onFailure(Call<Double> call, Throwable t) {
                            }
                        });
                    } else {
                        Toast.makeText(context, "No data for " + getTimeOfDay(), Toast.LENGTH_LONG).show();
                    }

                } else if (response.code() == 401) {
                    startLoginActivity();
                }
            }

            @Override
            public void onFailure(Call<Fact> call, Throwable t) {
            }
        });
    }
}
