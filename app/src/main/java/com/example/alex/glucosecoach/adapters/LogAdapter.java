package com.example.alex.glucosecoach.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.models.Fact;

import java.util.List;

/**
 * Created by alex on 3/7/17.
 */

public class LogAdapter extends BaseAdapter {
    private Context context;
    private List<Fact> facts;

    public LogAdapter(Context context, List<Fact> facts) {
        this.context = context;
        this.facts = facts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.log_item_option, null);

        TextView txtBGValue = (TextView) view.findViewById(R.id.list_item_bg);
        TextView txtInsValue = (TextView) view.findViewById(R.id.list_item_ins);
        TextView txtCarbsValue = (TextView) view.findViewById(R.id.list_item_carbs);
        TextView txtExerValue = (TextView) view.findViewById(R.id.list_item_exer);

        txtBGValue.setText(facts.get(position).getBgValue().toString());

        return view;
    }

    @Override
    public int getCount() {
        return facts.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
