package com.example.alex.glucosecoach.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.adapters.LogAdapter;
import com.example.alex.glucosecoach.controller.ApiManager;
import com.example.alex.glucosecoach.controller.TokenManager;
import com.example.alex.glucosecoach.controller.UserManager;
import com.example.alex.glucosecoach.models.Fact;
import com.example.alex.glucosecoach.services.FactService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogbookFragment extends Fragment {
    private ListView listView;

    public LogbookFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the layout for fragment
        View view = inflater.inflate(R.layout.fragment_logbook, container, false);

        UserManager _userManager = new UserManager(getActivity());
        TokenManager _tokenManager = new TokenManager(getActivity());
        ApiManager _apiManager = new ApiManager(_tokenManager.getToken());

        final Context context = this.getContext();

        listView = (ListView) view.findViewById(R.id.menu_list_logbook);

        FactService factService = _apiManager.getFactService();
        Call<List<Fact>> call = factService.getFacts(_userManager.getUsername());
        call.enqueue(new Callback<List<Fact>>() {
            @Override
            public void onResponse(Call<List<Fact>> call, Response<List<Fact>> response) {
                if (response.isSuccessful() && response.body().size() != 0) {
                    listView.setAdapter(new LogAdapter(getActivity(), response.body()));
                    setListViewListener();
                } else {
                    Toast.makeText(context, "No Data", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Fact>> call, Throwable t) {
            }
        });



        return view;
    }

    public void setListViewListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Fact fact = (Fact) adapterView.getAdapter().getItem(position);

                StringBuilder sb = new StringBuilder();
                sb.append(getString(R.string.fact_bg, fact.getBgValue().toString()));
                sb.append("\n");
                sb.append(getString(R.string.fact_ins, fact.getInsValue().toString()));
                sb.append("\n");
                sb.append(getString(R.string.fact_carbs, fact.getFoodValue().toString()));
                sb.append("\n");
                sb.append(getString(R.string.fact_exer, fact.getExerciseValue().toString()));

                new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.fact_date, fact.getPfDate(), fact.todToString(fact.getPfTimeOfDay())))
                        .setMessage(sb)
                    .create()
                    .show();
            }
        });
    }

    @Override
    public void onAttach(Context context) {super.onAttach(context);}

    @Override
    public void onDetach() {super.onDetach();}

    public interface OnFragmentInteractionListener {void onFragmentInteraction(Uri uri);}
 }
