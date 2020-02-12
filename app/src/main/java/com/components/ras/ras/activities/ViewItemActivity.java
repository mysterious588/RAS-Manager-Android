package com.components.ras.ras.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.components.ras.ras.R;
import com.components.ras.ras.adapters.ViewItemAdapter;
import com.components.ras.ras.models.UserInfo;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class ViewItemActivity extends AppCompatActivity {
    String itemName, imageId, description, datasheet, ownerImage, ownerName;
    int quantity, amount_requested, max, total, quantityOwnedByUser;
    TextView name, textView, descriptionText, datasheetText;
    ImageView img;
    Button request;
    SeekBar seekBar;
    FirebaseAuth mAuth;
    DatabaseReference mRootRef, ownersRef;
    boolean exists = false;
    ArrayList<UserInfo> user = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    ViewItemAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        mAuth = FirebaseAuth.getInstance();
        final String user_name = Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName();
        final String user_image = mAuth.getCurrentUser().getPhotoUrl().toString();
        ownersRef = FirebaseDatabase.getInstance().getReference().child("UsersActivity");
        mRootRef = FirebaseDatabase.getInstance().getReference().child("UsersActivity").child(Objects.requireNonNull(mAuth.getCurrentUser().getDisplayName()));
        ownerImage = Objects.requireNonNull(mAuth.getCurrentUser().getPhotoUrl()).toString();
        user.add(new UserInfo(user_name, ownerImage));
        descriptionText = findViewById(R.id.mDescription);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name = findViewById(R.id.name);
        total = Integer.parseInt(Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("quantity")).toString());
        max = total;
        datasheetText = findViewById(R.id.mDataSheet);
        textView = findViewById(R.id.amount_requested);
        seekBar = findViewById(R.id.seekbar);
        img = findViewById(R.id.itemViewImage);
        itemName = getIntent().getStringExtra("text");
        getOwners(itemName);
        exists(itemName);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("UsersActivity");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot f : dataSnapshot.getChildren()) {
                    Log.e("f", Objects.requireNonNull(f.getValue()).toString());
                    for (DataSnapshot s : f.child("possessions").getChildren()) {
                        Log.e("s", s.child("Quantity").getValue() + "\t" + itemName);
                        if (Objects.equals(s.getKey(), itemName)) {
                            quantityOwnedByUser = Integer.parseInt(Objects.requireNonNull(s.child("Quantity").getValue()).toString());
                            Log.e("the value", Integer.toString(quantityOwnedByUser));
                            max -= quantityOwnedByUser;
                            seekBar.setMax(max);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (getIntent().getStringExtra("description") != null) {
            description = getIntent().getStringExtra("description");
            descriptionText.setText(description);
            descriptionText.setVisibility(View.VISIBLE);
        }
        if (getIntent().getStringExtra("datasheet") != null) {
            datasheet = getIntent().getStringExtra("datasheet");
            Log.e("the problem", datasheet);
            datasheetText.setVisibility(View.VISIBLE);
            datasheetText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(datasheet));
                    startActivity(browserIntent);
                }
            });
        }

        imageId = getIntent().getStringExtra("image");
        name.setText(itemName);
        Picasso.get().load(imageId).into(img);
        seekBar.setMax(max);
        textView.setText("0" + " /" + max);
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);


        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        descriptionText.setAnimation(animation);
        request = findViewById(R.id.requestItem);
        seekBar = findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                amount_requested = i;
                String f = Integer.toString(amount_requested);
                int max = seekBar.getMax();
                textView.setText(String.format("%s /%s", f, Integer.toString(max)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String f = Integer.toString(amount_requested);
                textView.setText(String.format("%s /%s", f, Integer.toString(seekBar.getMax())));
            }
        });
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTimeView", false)) {
            TapTargetView.showFor(this,                 // `this` is an Activity
                    TapTarget.forView(findViewById(R.id.requestItem), "You can request an Item here", "By requesting, you add it into your account's database")
                            // All options below are optional
                            .outerCircleColor(R.color.colorPrimaryDark)      // Specify a color for the outer circle
                            .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                            .targetCircleColor(R.color.white)   // Specify a color for the target circle
                            .titleTextSize(20)                  // Specify the size (in sp) of the title text
                            .titleTextColor(R.color.white)      // Specify the color of the title text
                            .descriptionTextSize(12)            // Specify the size (in sp) of the description text
                            .descriptionTextColor(R.color.white)  // Specify the color of the description text
                            .textColor(R.color.white)            // Specify a color for both the title and description text
                            .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                            .dimColor(R.color.white)            // If set, will dim behind the view with 30% opacity of the given color
                            .drawShadow(true)                   // Whether to draw a drop shadow or not
                            .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                            .tintTarget(false)                   // Whether to tint the target view's color
                            .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                            .targetRadius(45),                  // Specify the target radius (in dp)
                    new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);      // This call is optional
                        }
                    });
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTimeView", true);
            editor.apply();
        }

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exists(itemName);
                //TODO make this button clickable only when the current user doesn't have the Item
                if (amount_requested == 0)
                    FancyToast.makeText(ViewItemActivity.this, "sure you can have as much zeros as you want :)", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                else if (exists) {
                    FancyToast.makeText(ViewItemActivity.this, "You already requested this Item", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                } else {
                    mRootRef.child("possessions").child(itemName).child("image id").setValue(imageId);
                    mRootRef.child("possessions").child(itemName).child("state").setValue("pending");
                    mRootRef.child("possessions").child(itemName).child("Quantity").setValue(amount_requested).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            max = max - amount_requested;
                            seekBar.setMax(max);
                            mNames.add(user_name);
                            mImageUrls.add(user_image);
                            initRecyclerView();
                            FancyToast.makeText(ViewItemActivity.this, "Success", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            FancyToast.makeText(ViewItemActivity.this, "Failed", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    });
                }

            }
        });
    }

    private void exists(final String item) {
        mRootRef.child("possessions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(item).exists()) {
                    exists = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getOwners(final String item) {
        //child ("UsersActivity")
        ownersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot f : dataSnapshot.getChildren()) {// loop UsersActivity >> Ahmed Khaled ... etc
                    Log.e("the owner", f.getKey());
                    if (f.child("possessions").child(item).exists()) {
                        Log.e(f.getKey(), f.child("images").getValue(String.class));
                        mNames.add(f.getKey());
                        mImageUrls.add(f.child("images").getValue(String.class));
                        initRecyclerView();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ViewItemAdapter(this, mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
    }


}
