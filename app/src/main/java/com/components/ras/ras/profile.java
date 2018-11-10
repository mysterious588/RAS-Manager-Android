package com.components.ras.ras;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;

public class profile extends AppCompatActivity {
    String name;
    ArrayList<item> arrayList;
    DatabaseReference mrootRef = FirebaseDatabase.getInstance().getReference().child("users");
    possessions_adapter adapter;
    TextView itemNameText, quantityText;
    String image;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        arrayList = new ArrayList<>();
        list = findViewById(R.id.possessionsList);
        adapter = new possessions_adapter(this, arrayList);
        list.setAdapter(adapter);
        itemNameText = findViewById(R.id.profile_name);
        name = getIntent().getStringExtra("name");
        itemNameText.setText(name);
        DatabaseReference ref = mrootRef.child(name).child("possessions");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot f : dataSnapshot.getChildren()) {
                    if (f.child("Quantity").exists() && !f.child("Quantity").getValue().toString().equals("0") && f.exists()) {
                        String state = f.child("state").getValue().toString();
                        String Mname = f.getKey();
                        String quantity = f.child("Quantity").getValue().toString();
                        image = f.child("image id").getValue().toString();
                        arrayList.add(new item(Mname, image, quantity,state));
                    }
                    ProgressBar progressBar = findViewById(R.id.spin_kit);
                    progressBar.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
                if (arrayList.isEmpty()) {
                    ProgressBar progressBar = findViewById(R.id.spin_kit);
                    progressBar.setVisibility(View.GONE);
                    arrayList.add(new item("Empty ", "https://firebasestorage.googleapis.com/v0/b/ras-manager.appspot.com/o/listView%20icons%2Fras.png?alt=media&token=a36b1aa1-386b-4cf4-b9ed-cb1c89881e99", "",""));
                    adapter.notifyDataSetChanged();
                    list.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                Log.e("the name is",name);
                if (name.equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    mrootRef.child(name).child("possessions").child(arrayList.get(i).getName()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            FancyToast.makeText(profile.this,"removed",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                                            arrayList.remove(i);
                                            adapter.notifyDataSetChanged();
                                        }
                                    });

                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(profile.this);
                    builder.setMessage("return this item?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                }
            }
        });
    }
}
