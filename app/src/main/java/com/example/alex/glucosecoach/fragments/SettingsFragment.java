package com.example.alex.glucosecoach.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.controller.ApiManager;
import com.example.alex.glucosecoach.controller.TokenManager;
import com.example.alex.glucosecoach.controller.UserManager;
import com.example.alex.glucosecoach.services.PredictionService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment {
    private Button _retrainButton;
    ApiManager _apiManager;
    TokenManager _tokenManager;
    UserManager _userManager;

    public SettingsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        _tokenManager = new TokenManager(getActivity());
        _apiManager = new ApiManager(_tokenManager.getToken());
        _userManager = new UserManager(getActivity());

        _retrainButton = (Button) view.findViewById(R.id.btn_sumbit_train);

        _retrainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("Confirm")
                        .setMessage("Are you sure you want to retrain your model?")
                        .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PredictionService predictionService = _apiManager.getPredictionService();
                                Call<Void> call = predictionService.trainModel(_userManager.getUsername());
                                call.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {

                                        if (response.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "Error in training", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Log.d("Error", t.getMessage());
                                        Toast.makeText(getActivity(), "Unsuccessful", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .create()
                        .show();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
