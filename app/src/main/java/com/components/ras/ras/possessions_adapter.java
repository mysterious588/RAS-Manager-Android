package com.components.ras.ras;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class possessions_adapter extends ArrayAdapter<item> {
    private final Activity context;
    private ArrayList<item> itemname;
    private static final int CONSTANT = 555770;
    int x = 0;

    public possessions_adapter(Activity context, ArrayList<item> itemname) {
        super(context, R.layout.list, itemname);
        this.context = context;
        this.itemname = itemname;
    }

    public class Holder {
        TextView itemName, extraText, state;
        ImageView image;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        Holder holder = new Holder();
        if (view == null) {
            view = inflater.inflate(R.layout.possessions_list, parent, false);
            holder.itemName = view.findViewById(R.id.mItem);
            holder.extraText = view.findViewById(R.id.mText);
            holder.image = view.findViewById(R.id.mIcon);
            holder.state = view.findViewById(R.id.state);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        item currentWord = getItem(position);

        Picasso.get().load(currentWord.getImageId()).into(holder.image);
        holder.itemName.setText(currentWord.getName());
        holder.extraText.setText(currentWord.getQuantity());
        if (currentWord.getDescription().equals("rejected")) {
            holder.state.setTextColor(Color.RED);
            holder.state.setText(currentWord.getDescription());
        } else if (currentWord.getDescription().equals("pending")) {
            holder.state.setTextColor(Color.parseColor("#FBC02D"));
            holder.state.setText(currentWord.getDescription());
        } else {
            holder.state.setTextColor(Color.parseColor("#4CAF50"));
            holder.state.setText(currentWord.getDescription());
        }
        return view;

    }
}

