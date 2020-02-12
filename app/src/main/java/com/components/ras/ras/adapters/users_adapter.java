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
import com.components.ras.ras.models.UserInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class users_adapter extends ArrayAdapter<UserInfo> {
    private final Activity context;
    private ArrayList<UserInfo> itemname;
    private static final int CONSTANT = 567894;

    public users_adapter(Activity context, ArrayList<UserInfo> itemname) {
        super(context, R.layout.users_list, itemname);
        this.context = context;
        this.itemname = itemname;
    }

    public class Holder {
        TextView itemName;
        ImageView image;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        Holder holder = new Holder();
        if (view == null) {
            view = inflater.inflate(R.layout.users_list, parent, false);
            holder.itemName = view.findViewById(R.id.userName);
            holder.image = view.findViewById(R.id.user_image);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        UserInfo currentUser = getItem(position);

        Picasso.get().load(currentUser.getImage()).into(holder.image);
        holder.itemName.setText(currentUser.getName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.itemName.setId(position + CONSTANT);
            holder.itemName.setTransitionName("user_name");
        }

        return view;

    }



}

