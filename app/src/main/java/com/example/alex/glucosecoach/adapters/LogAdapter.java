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
        View view = convertView;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = inflater.inflate(R.layout.log_item_option, parent, false);
        }

        TextView txtDate = (TextView) view.findViewById(R.id.list_item_date);
        TextView txtBGValue = (TextView) view.findViewById(R.id.list_item_bg);
        TextView txtInsValue = (TextView) view.findViewById(R.id.list_item_ins);
        TextView txtCarbsValue = (TextView) view.findViewById(R.id.list_item_carbs);
        TextView txtExerValue = (TextView) view.findViewById(R.id.list_item_exer);

        txtDate.setText(this.context.getString(R.string.fact_date, facts.get(position).getPfDate(),
                facts.get(position).todToString(facts.get(position).getPfTimeOfDay())));
        txtBGValue.setText(this.context.getString(R.string.fact_bg, facts.get(position)
                .getBgValue().toString()));
        txtInsValue.setText(this.context.getString(R.string.fact_ins, facts.get(position)
                .getInsValue().toString()));
        txtCarbsValue.setText(this.context.getString(R.string.fact_carbs, facts.get(position)
                .getFoodValue().toString()));
        txtExerValue.setText(this.context.getString(R.string.fact_exer, facts.get(position)
                .getExerciseValue().toString()));

        return view;
    }

    @Override
    public int getCount() {
        return facts.size();
    }

    @Override
    public Object getItem(int position) {
        return facts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
