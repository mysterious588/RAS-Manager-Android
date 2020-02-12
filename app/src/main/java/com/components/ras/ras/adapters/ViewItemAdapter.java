package com.components.ras.ras.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.components.ras.ras.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 2/12/2018.
 */

public class ViewItemAdapter extends RecyclerView.Adapter<ViewItemAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<String> mNames;
    private ArrayList<String> mImageUrls;
    private Context mContext;

    public ViewItemAdapter(Context context, ArrayList<String> names, ArrayList<String> imageUrls) {
        mNames = names;
        mImageUrls = imageUrls;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext).asBitmap().load(mImageUrls.get(position)).into(holder.image);


        holder.name.setText(mNames.get(position));

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on an image: " + mNames.get(position));

            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView name;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageOfOwner);
            name = itemView.findViewById(R.id.quantityOwnedByUser);
        }
    }
}
