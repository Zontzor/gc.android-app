package com.example.alex.glucosecoach.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.adapters.LogAdapter;
import com.example.alex.glucosecoach.controller.ApiManager;
import com.example.alex.glucosecoach.controller.TokenManager;
import com.example.alex.glucosecoach.controller.UserManager;
import com.example.alex.glucosecoach.models.Fact;
import com.example.alex.glucosecoach.services.FactService;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

        listView = (ListView) view.findViewById(R.id.menu_list_logbook);

        FactService factService = _apiManager.getFactService();

        factService.getFactsObservable(_userManager.getUsername())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Fact>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Fact> value) {
                        listView.setAdapter(new LogAdapter(getActivity(), value));
                        setListViewListener();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("fact_log", "Error fetching facts");
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        return view;
    }

    public void setListViewListener() {
        listView.setOnItemClickListener((AdapterView<?> adapterView, View view, int position, long id) -> {
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

        });
    }

    @Override
    public void onAttach(Context context) {super.onAttach(context);}

    @Override
    public void onDetach() {super.onDetach();}

    public interface OnFragmentInteractionListener {void onFragmentInteraction(Uri uri);}
 }
