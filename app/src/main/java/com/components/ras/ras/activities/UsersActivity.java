package com.components.ras.ras.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.components.ras.ras.R;
import com.components.ras.ras.adapters.users_adapter;
import com.components.ras.ras.models.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.Objects;

public class UsersActivity extends AppCompatActivity {
    ArrayList<UserInfo> userNames;
    ListView list;
    DatabaseReference mrootRef = FirebaseDatabase.getInstance().getReference();
    users_adapter adapter;
    FirebaseAuth mAuth;
    private static final int CONSTANT = 567894;
    String nameFromAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        mAuth = FirebaseAuth.getInstance();
        list = findViewById(R.id.usersList);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userNames = new ArrayList<>();
        adapter = new users_adapter(this, userNames);
        list.setAdapter(adapter);
        nameFromAuth = Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName();
        final ProgressBar progressBar = findViewById(R.id.mprogressbar);
        mrootRef.child("UsersActivity").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    String name = s.getKey();
                    String image = Objects.requireNonNull(s.child("images").getValue()).toString();
                    if (Objects.equals(name, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName())) {
                        userNames.add(0, new UserInfo(name, image));
                    } else {
                        userNames.add(new UserInfo(name, image));

                    }
                }
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                FancyToast.makeText(UsersActivity.this, databaseError.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(UsersActivity.this, ProfileActivity.class);
                intent.putExtra("name", userNames.get(i).getName());
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(UsersActivity.this, Pair.create(findViewById(i + CONSTANT), "user_name"));
                startActivity(intent, options.toBundle());
            }

        });
    }

}
