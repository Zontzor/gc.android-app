package com.example.alex.glucosecoach.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.graphics.Color;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.adapters.LogAdapter;
import com.example.alex.glucosecoach.controller.ApiManager;
import com.example.alex.glucosecoach.controller.TokenManager;
import com.example.alex.glucosecoach.controller.UserManager;
import com.example.alex.glucosecoach.models.Fact;
import com.example.alex.glucosecoach.services.FactService;

import java.util.ArrayList;
import java.util.List;

import im.dacer.androidcharts.LineView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartsFragment extends Fragment {

    public ChartsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_charts, container, false);

        UserManager _userManager = new UserManager(getActivity());
        TokenManager _tokenManager = new TokenManager(getActivity());
        ApiManager _apiManager = new ApiManager(_tokenManager.getToken());

        FactService factService = _apiManager.getFactService();
        Call<List<Fact>> call = factService.getFacts(_userManager.getUsername());
        call.enqueue(new Callback<List<Fact>>() {
            @Override
            public void onResponse(Call<List<Fact>> call, Response<List<Fact>> response) {
                if (response.isSuccessful()) {
                    List<Fact> facts = response.body();
                    ArrayList<Float> dataList = new ArrayList<>();

                    final LineView lineView = (LineView)view.findViewById(R.id.line_view_bg);

                    for (Fact fact: facts) {
                        dataList.add(Float.valueOf(String.valueOf(fact.getBgValue())));
                    }

                    ArrayList<String> test = new ArrayList<String>();
                    for (int i=0; i<dataList.size(); i++){
                        test.add(String.valueOf(i+1));
                    }
                    lineView.setBottomTextList(test);
                    lineView.setColorArray(new int[]{Color.parseColor("#F44336")});
                    lineView.setDrawDotLine(true);
                    lineView.setShowPopup(LineView.SHOW_POPUPS_NONE);

                    ArrayList<ArrayList<Float>> dataLists = new ArrayList<>();
                    dataLists.add(dataList);
                    lineView.setFloatDataList(dataLists);
                }
            }

            @Override
            public void onFailure(Call<List<Fact>> call, Throwable t) {
            }
        });

        return view;
    }

    private void initLineView(LineView lineView) {


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
