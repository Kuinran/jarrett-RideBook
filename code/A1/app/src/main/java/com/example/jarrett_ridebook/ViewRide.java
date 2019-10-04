package com.example.jarrett_ridebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

// Purpose: a class that handles the detailed view of a given ride
// Rational: only one ride needs to be viewed at a time, no other views are needed to be seen
// therefor, a class per ride for viewing is the most sensible for data hiding
// This class controls edit and deletion of single entries for ui design reasons
public class ViewRide extends AppCompatActivity {
    protected Ride ride;
    final static int EDIT = 3;
    final static int DELETE = 4;
    protected int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        Bundle bundle = getIntent().getExtras();
        if (bundle.getSerializable("RIDE_DATA") != null) {
            this.fillView((Ride) bundle.getSerializable("RIDE_DATA"), bundle.getInt("POSITION"));
        }
    }

    // setter for ride
    public void setRide(Ride ride) {this.ride = ride;}
    // getter for ride
    public Ride getRide() {return this.ride;}

    // populates text views on view screen
    private void fillView(Ride rideData, int position) {
        // sets view screen info to match ride obj
        this.setRide(rideData);
        this.position = position;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("hh:mm");
        TextView dateView = findViewById(R.id.view_data_date);
        dateView.setText(dateFormat.format(this.getRide().getDatetime().getTime()));
        TextView timeView = findViewById(R.id.view_data_time);
        timeView.setText(timeFormat.format(this.getRide().getDatetime().getTime()));
        TextView distanceView = findViewById(R.id.view_data_distance);
        distanceView.setText(String.format("%.2fkm", this.getRide().getDistance()));
        TextView speedView = findViewById(R.id.view_data_speed);
        speedView.setText(String.format("%.2fkm/h", this.getRide().getSpeed()));
        TextView cadenceView = findViewById(R.id.view_data_cadence);
        cadenceView.setText(String.format("%.2frpm", this.getRide().getCadence()));
        TextView commentView = findViewById(R.id.view_data_comment);
        if(commentView.getText().toString().compareTo("") == 0) {
            TextView commentTitle = findViewById(R.id.view_data_comment_label);
            commentTitle.setVisibility(View.GONE);
            commentView.setVisibility(View.GONE);
        } else {
            TextView commentTitle = findViewById(R.id.view_data_comment_label);
            commentTitle.setVisibility(View.VISIBLE);
            commentView.setVisibility(View.VISIBLE);
            commentView.setText(this.getRide().getComment());
        }
    }

    // handles preparing data when returning to main activity
    private void returnFromView(int returnCode, Intent intent) {
        if (intent == null) {
            intent = new Intent();
        }
        Bundle bundle = new Bundle();
        bundle.putInt("position", this.position);
        intent.putExtras(bundle);
        setResult(returnCode, intent);

        super.finish();
    }

    // options menu for edit and delete
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    // options menu for edit and delete
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            Intent intent = new Intent(this, RideDataHandler.class);
            Bundle bundle = new Bundle();
            intent.putExtra("MODE", "EDIT");
            bundle.putSerializable("RIDE_DATA", this.getRide());
            intent.putExtras(bundle);
            startActivityForResult(intent, RideDataHandler.EDIT);
            return true;
        } else if (id == R.id.action_delete) {
            returnFromView(ViewRide.DELETE, null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // for handling returned data from edit screen, passes it along to main activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RideDataHandler.EDIT && resultCode == RideDataHandler.EDIT) {
            super.onActivityResult(requestCode, resultCode, data);
            Bundle bundle = new Bundle();
            bundle.putInt("POSITION", this.position);
            data.putExtras(bundle);
            returnFromView(ViewRide.EDIT, data);
        }
    }
}
