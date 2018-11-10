package com.components.ras.ras;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Picasso;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Dialog.dialogListener {
    ListView list;
    DatabaseReference mrootRef = FirebaseDatabase.getInstance().getReference();
    String description;
    ImageView image;
    ArrayList<item> item_names;
    ArrayList<item> quantity;
    adapter adapter;
    int i;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    int x;
    MaterialSearchView searchView;
    private static final int CONSTANT = 999990;
    private static final int CONSTANT2 = 500000;
    SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            Alerter.create(this).setTitle("Stealing all your data...").setText("Wooah so much passwords").setBackgroundColorRes(R.color.colorPrimary).setDuration(7000).enableProgress(true).setProgressColorInt(getResources().getColor(R.color.colorAccent)).show();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.apply();
        }

        item_names = new ArrayList<>();
        ArrayList<item> arrayList = new ArrayList<>();
        final ProgressBar progressBar = findViewById(R.id.mprogressbar);
        adapter = new adapter(this, item_names, arrayList);
        list = findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setTextFilterEnabled(true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        if (user == null) {
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUsername();
            }
        });
        mrootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (final DataSnapshot postSnapshot : snapshot.getChildren()) {
                    if (postSnapshot.child("Quantity").exists()) {
                        if (!postSnapshot.child("description").exists() && !postSnapshot.child("datasheet").exists()) {
                            String value = Objects.requireNonNull(postSnapshot.child("Quantity").getValue()).toString();
                            String imageUri = Objects.requireNonNull(postSnapshot.child("image id").getValue()).toString();
                            String item_name = postSnapshot.getKey();
                            item_names.add(new item(item_name, imageUri, value));
                        } else if (postSnapshot.child("datasheet").exists()) {
                            String value = Objects.requireNonNull(postSnapshot.child("Quantity").getValue()).toString();
                            String imageUri = Objects.requireNonNull(postSnapshot.child("image id").getValue()).toString();
                            String description = Objects.requireNonNull(postSnapshot.child("description").getValue()).toString();
                            String item_name = postSnapshot.getKey();
                            String datasheet = Objects.requireNonNull(postSnapshot.child("datasheet").getValue()).toString();
                            item_names.add(new item(item_name, imageUri, value, description, datasheet));
                        } else if (!postSnapshot.child("datasheet").exists()) {
                            String value = Objects.requireNonNull(postSnapshot.child("Quantity").getValue()).toString();
                            String imageUri = Objects.requireNonNull(postSnapshot.child("image id").getValue()).toString();
                            String description = Objects.requireNonNull(postSnapshot.child("description").getValue()).toString();
                            String item_name = postSnapshot.getKey();
                            item_names.add(new item(item_name, imageUri, value, description));
                        }
                    }

                }

                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError firebaseError) {
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navEmail = headerView.findViewById(R.id.emailNavHeader);
        final TextView navUsername = headerView.findViewById(R.id.nameNavHeader);
        image = headerView.findViewById(R.id.imageView);
        navEmail.setText(user.getEmail());
        if (user.getDisplayName() != null) {
            navUsername.setText(user.getDisplayName());
        }
        if (user.getPhotoUrl() != null) {
            String uri = user.getPhotoUrl().toString();
            loadImageFromUri(uri);
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, view_item.class);
                intent.putExtra("quantity", item_names.get(position).getQuantity());
                intent.putExtra("image", item_names.get(position).getImageId());
                intent.putExtra("text", item_names.get(position).getName());
                list.setEnabled(false);
                if (item_names.get(position).getDescription() != null)
                    intent.putExtra("description", item_names.get(position).getDescription());

                if (item_names.get(position).getDatasheet() != null)
                    intent.putExtra("datasheet", item_names.get(position).getDatasheet());
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(MainActivity.this, Pair.create(findViewById(position + CONSTANT), "tImage"), Pair.
                                create(findViewById(position + CONSTANT2), "tText"));
                startActivity(intent, options.toBundle());
            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        searchView = findViewById(R.id.search_view);
        MenuItem item = menu.findItem(R.id.search);
        searchView.setMenuItem(item);
        searchView.setCursorDrawable(R.drawable.curser);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)) {
                    adapter.filter("");
                    list.clearTextFilter();
                } else {
                    adapter.filter(s);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FancyToast.makeText(this, "لا هوا حلو كده متغيرش حاجة", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
            return true;
        } else if (id == R.id.refresh) {
            super.recreate();
            adapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
        } else if (id == R.id.signOut) {
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);
        } else if (id == R.id.team_members) {
            Intent intent = new Intent(MainActivity.this, users.class);
            startActivity(intent);
        } else if (id == R.id.chat) {
            FancyToast.makeText(this, "Developer of this app had a headache when he started making this section", FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
        } else if (id == R.id.nav_share) {
            FancyToast.makeText(this, "WHAT A SPY!", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
        }
        else if (id == R.id.github){
            String url = "https://github.com/RAS-ZSC";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setUsername() {
        Dialog f = new Dialog();
        f.show(getSupportFragmentManager(), "example dialog");
    }

    private void loadImageFromUri(String uri) {
        Picasso.get().load(uri).into(image);
    }

    @Override
    public void applyTexts(String item, String Quantity, String imgId) {
        DatabaseReference mRefChild = mrootRef.child(item).child("Quantity");
        mRefChild.setValue(Quantity);
        DatabaseReference imageChild = mrootRef.child(item).child("image id");
        imageChild.setValue("https://firebasestorage.googleapis.com/v0/b/ras-manager.appspot.com/o/listView%20icons%2Fras.png?alt=media&token=a36b1aa1-386b-4cf4-b9ed-cb1c89881e99");
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        list.setEnabled(true);
        super.onResume();
    }

}
