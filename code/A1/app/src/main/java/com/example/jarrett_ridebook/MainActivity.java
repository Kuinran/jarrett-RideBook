package com.example.jarrett_ridebook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

// Purpose: main page for application, has sole control over a user's ride data
// Rational: the app is made with 1 user in mind. control of anything at the userlevel data should
// be here as a result. It is also the logical root to all other activities
public class MainActivity extends AppCompatActivity {
    protected ListView rideList;
    protected ArrayAdapter<Ride> rideAdapter;
    protected User currentUser;
    static final int VIEW = 2;
    static final int ADD = 1;
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get preferences in event that the data is cleared. all other data can be recovered from user
        Gson gson = new Gson();
        SharedPreferences mPrefs = getSharedPreferences("UserMem", MODE_PRIVATE);
        try {
            this.currentUser = gson.fromJson( mPrefs.getString("USER", null), User.class);
        } catch (NullPointerException e) {
            this.currentUser = null;
        }

        this.initCurrentUser();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAdd();
            }
        });
        rideList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startView(position);
            }
        });

        // TODO: implement multiselect delete
        // not implemented due to time constraints

    }

    // starts screen for viewing a ride. Needs position for handling potential deletion
    private void startView(int position) {
        Intent intent = new Intent(this, ViewRide.class);
        intent.putExtra("RIDE_DATA", this.rideAdapter.getItem(position));
        intent.putExtra("POSITION", position);
        startActivityForResult(intent, MainActivity.VIEW);
    }

    // starts screen for editing data in add mode
    private void startAdd() {
        Intent intent = new Intent(this, RideDataHandler.class);
        intent.putExtra("MODE", "ADD");
        startActivityForResult(intent, MainActivity.ADD);
    }

    // sets class variables related to the user
    private void initCurrentUser() {
        if (this.currentUser == null) {
            this.currentUser = new User();
        }
        this.rideList = findViewById(R.id.ride_list);
        this.rideAdapter = new RideAdapter(this, R.layout.list_content, currentUser.getRides());
        this.rideList.setAdapter(rideAdapter);
        this.refreshTotalDist();
        this.checkEmpty();
    }

    // checks if a user has any rides and enables prompt message if true
    private void checkEmpty() {
        TextView emptyMsg = findViewById(R.id.empty_message);
        if (this.currentUser.getRides().isEmpty()) {
            emptyMsg.setVisibility(View.VISIBLE);
        } else {
            emptyMsg.setVisibility(View.GONE);
        }
    }

    // refreshes bottom counter of total distance
    private void refreshTotalDist() {
        TextView totDistView = findViewById(R.id.total_distance_value);
        totDistView.setText(String.format("%.2fkm", currentUser.getTotalDistance()));
    }

    // handles returned data from all screens
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainActivity.ADD && resultCode == RideDataHandler.ADD) {
            // adds ride if result is to add
            super.onActivityResult(requestCode, resultCode, data);
            Ride ride = (Ride) data.getExtras().getSerializable("RIDE_DATA");
            this.rideAdapter.add(ride);
        } else if (requestCode == MainActivity.VIEW && resultCode == ViewRide.DELETE) {
            // deletes ride from stored position if result code is delete
            super.onActivityResult(requestCode, resultCode, data);
            this.rideAdapter.remove(this.rideAdapter.getItem(data.getExtras().getInt("position")));
        } else if (requestCode == MainActivity.VIEW && resultCode == ViewRide.EDIT) {
            // edits ride from stored position if result code is edit
            super.onActivityResult(requestCode, resultCode, data);
            Ride ride = this.rideAdapter.getItem(data.getExtras().getInt("position"));
            Ride newRideData = (Ride) data.getExtras().getSerializable("RIDE_DATA");
            ride.setDatetime(newRideData.getDatetime());
            ride.setDistance(newRideData.getDistance());
            ride.setSpeed(newRideData.getSpeed());
            ride.setCadence(newRideData.getCadence());
            ride.setComment(newRideData.getComment());
        }
        // refresh data handlers
        this.rideAdapter.notifyDataSetChanged();
        this.refreshTotalDist();
        this.checkEmpty();
    }

    // options menu for delete selected feature
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // options menu for delete selected feature
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_deleteSelected) {
            Snackbar.make(getWindow().getDecorView(), "Not yet implemented", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // saves state of user when activity is paused
    @Override
    public void onPause() {
        super.onPause();
        Gson gson = new Gson();
        SharedPreferences mPrefs = getSharedPreferences("UserMem", MODE_PRIVATE);
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putString("USER", gson.toJson(this.currentUser));
        ed.commit();
    }
}
