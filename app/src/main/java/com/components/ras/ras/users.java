package com.components.ras.ras;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;

public class users extends AppCompatActivity {
    ArrayList<user_info> userNames;
    ListView list ;
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
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userNames = new ArrayList<>();
        adapter = new users_adapter(this,userNames);
        list.setAdapter(adapter);
        nameFromAuth = mAuth.getCurrentUser().getDisplayName();
        final ProgressBar progressBar = findViewById(R.id.mprogressbar);
        mrootRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()){
                    String name = s.getKey();
                    String image = s.child("images").getValue().toString();
                    if (name.equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())) {
                        userNames.add(0,new user_info(name, image));
                    }
                    else {
                        userNames.add(new user_info(name, image));

                    }
                }
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                FancyToast.makeText(users.this,databaseError.getMessage(),FancyToast.LENGTH_SHORT,FancyToast.ERROR,false);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(users.this,profile.class);
                intent.putExtra("name",userNames.get(i).getName());
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(users.this, Pair.create(findViewById(i + CONSTANT), "user_name"));
                startActivity(intent,options.toBundle());
            }

        });
    }

}
