package com.components.ras.ras;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class budget extends AppCompatActivity {

    ListView budgetList;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("budget");
    ArrayList<income_model> arrayList;
    budget_adapter income_modelArrayAdapter;
    TextView totalSpentTxt, budgetTxt, totalAvailable;
    DatabaseReference budRef;
    double total_spent, total_income;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        budgetList = findViewById(R.id.budgetListView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        arrayList = new ArrayList<>();
        income_modelArrayAdapter = new budget_adapter(budget.this, arrayList);
        budgetList.setAdapter(income_modelArrayAdapter);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.child("income sources").getChildren()) {
                    income_model model = new income_model(s.getKey(), s.getValue(Double.class), true);
                    arrayList.add(model);
                }
                for (DataSnapshot s : dataSnapshot.child("spent on").getChildren()) {
                    income_model model = new income_model(s.getKey(), s.getValue(Double.class), false);
                    arrayList.add(model);
                }
                income_modelArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        budRef = FirebaseDatabase.getInstance().getReference().child("budget");
        budgetTxt = findViewById(R.id.totalIncomeTxtView);
        totalSpentTxt = findViewById(R.id.TotalSpentTxtView);
        totalAvailable = findViewById(R.id.totalAvailable);
        setSupportActionBar(toolbar);
        budRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.child("income sources").getChildren()) {
                    Double value = s.getValue(Double.class);
                    total_income += value;
                }
                for (DataSnapshot f : dataSnapshot.child("spent on").getChildren()) {
                    Double value = f.getValue(Double.class);
                    total_spent += value;
                }
                totalSpentTxt.setText(String.format("%s↓", Double.toString(total_spent)));
                budgetTxt.setText(String.format("%s↑", Double.toString(total_income)));
                totalAvailable.setText(Double.toString(total_income - total_spent));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
