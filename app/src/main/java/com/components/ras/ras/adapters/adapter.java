package com.components.ras.ras.adapters;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.components.ras.ras.R;
import com.components.ras.ras.models.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class adapter extends ArrayAdapter<Item> {
    private ArrayList<Item> constArrayList;
    private final Activity context;
    private ArrayList<Item> itemname;
    private static final int CONSTANT = 999990;
    private static final int CONSTANT2 = 500000;
    int x = 0;

    public adapter(Activity context, ArrayList<Item> itemname, ArrayList<Item> constArrayList) {
        super(context, R.layout.list, itemname);
        this.context = context;
        this.itemname = itemname;
        constArrayList.addAll(this.itemname);
        this.constArrayList = constArrayList;
    }

    public class Holder {
        TextView itemName, extraText;
        ImageView image;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        Holder holder = new Holder();
        if (view == null) {
            view = inflater.inflate(R.layout.list, parent, false);
            holder.itemName = view.findViewById(R.id.itemP);
            holder.extraText = view.findViewById(R.id.textViewP);
            holder.image = view.findViewById(R.id.icon);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        Item currentWord = getItem(position);

        Picasso.get().load(currentWord.getImageId()).into(holder.image);
        holder.itemName.setText(currentWord.getName());
        holder.extraText.setText(currentWord.getQuantity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.itemName.setId(position + CONSTANT2);
            holder.image.setId(position + CONSTANT);
            holder.extraText.setTransitionName("tText");
            holder.image.setTransitionName("tImage");
        }
        return view;

    }

    public void filter(String charText) {
        setConstArray();
        itemname.addAll(constArrayList);
        charText = charText.toLowerCase(Locale.getDefault());
        itemname.clear();
        if (charText.length() == 0) {
            itemname.addAll(constArrayList);
        } else {
            for (Item model : constArrayList) {
                if (model.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    itemname.add(model);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void setConstArray() {
        x++;
        if (x == 1) constArrayList.addAll(itemname);
    }
}

