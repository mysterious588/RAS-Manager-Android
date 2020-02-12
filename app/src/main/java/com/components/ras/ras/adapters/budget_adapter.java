package com.components.ras.ras.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.components.ras.ras.R;
import com.components.ras.ras.models.Income;

import java.util.ArrayList;

public class budget_adapter extends ArrayAdapter<Income> {
    private ArrayList<Income> event;
    private final Activity context;

    public budget_adapter(Activity context, ArrayList<Income> event) {
        super(context, R.layout.budget_texts, event);
        this.context = context;
        this.event = event;
    }
    public class Holder {
        TextView eventText, moneyText;
    }
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        budget_adapter.Holder holder = new budget_adapter.Holder();
        if (view == null) {
            view = inflater.inflate(R.layout.budget_texts, parent, false);
            holder.eventText = view.findViewById(R.id.budget1);
            holder.moneyText = view.findViewById(R.id.budget2);
            view.setTag(holder);
        } else {
            holder = (budget_adapter.Holder) view.getTag();
        }
        Income currentWord = getItem(position);
        if(currentWord.getWin()) {
            holder.eventText.setText(currentWord.getEventName());
            holder.moneyText.setText(currentWord.getOutcome() + "↑");
        }
        else {
            holder.eventText.setText(currentWord.getEventName());
            holder.moneyText.setTextColor(Color.parseColor("#ff0000"));
            holder.moneyText.setText(currentWord.getOutcome() + "↓");
        }
        return view;

    }
}
