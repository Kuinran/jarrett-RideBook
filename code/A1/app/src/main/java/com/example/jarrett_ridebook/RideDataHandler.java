package com.example.jarrett_ridebook;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


// handles data manipulation
// changes behavior based on mode (ADD, EDIT)
// Rational: editing and adding data to a class are extremely similar
// as such, a class with dual modes is best suited for code reusability
public class RideDataHandler extends AppCompatActivity implements View.OnClickListener{
    protected Ride ride;
    final static int ADD = 5;
    final static int EDIT = 6;
    private Button btnDatePicker;
    private Button btnTimePicker;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_data);

        Bundle extras = getIntent().getExtras();
        if (extras.getString("MODE").compareTo("ADD") == 0) {
            initAddScreen();
        } else if (extras.getString("MODE").compareTo("EDIT") == 0) {
            initEditScreen((Ride) extras.getSerializable("RIDE_DATA"));
        }

        this.btnDatePicker = findViewById(R.id.edit_date_button);
        this.btnTimePicker = findViewById(R.id.edit_time_button);
        this.btnDatePicker.setOnClickListener(this);
        this.btnTimePicker.setOnClickListener(this);
    }

    // getters and setters for ride
    public Ride getRide() {return this.ride;}

    public void setRide(Ride ride) {this.ride = ride;}

    // inits text items for add configuration
    private void initAddScreen() {
        this.setRide(new Ride());

        Button confirmButton = findViewById(R.id.mod_data_confirm);
        confirmButton.setText("Add ride");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("hh:mm");
        TextView dateEditText = findViewById(R.id.mod_data_date);
        dateEditText.setText(dateFormat.format(this.getRide().getDatetime().getTime()));
        TextView timeEditText = findViewById(R.id.mod_data_time);
        timeEditText.setText(timeFormat.format(this.getRide().getDatetime().getTime()));
        EditText distanceEditText = findViewById(R.id.mod_data_distance);
        distanceEditText.setText("");
        EditText speedEditText = findViewById(R.id.mod_data_speed);
        speedEditText.setText("");
        EditText cadenceEditText = findViewById(R.id.mod_data_cadence);
        cadenceEditText.setText("");
        EditText commentEditText = findViewById(R.id.mod_data_comment);
        commentEditText.setText("");

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ride ride = setFields(null);
                if (ride != null) {
                    returnData(ride, true);
                }
            }
        });
    }
    // inits text items for edit configuration
    private void initEditScreen(Ride rideData) {
        final Ride currentRide = rideData;
        this.setRide(rideData);
        Button confirmButton = findViewById(R.id.mod_data_confirm);
        confirmButton.setText("Save ride");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("hh:mm");
        TextView dateEditText = findViewById(R.id.mod_data_date);
        dateEditText.setText(dateFormat.format(rideData.getDatetime().getTime()));
        TextView timeEditText = findViewById(R.id.mod_data_time);
        timeEditText.setText(timeFormat.format(rideData.getDatetime().getTime()));
        EditText distanceEditText = findViewById(R.id.mod_data_distance);
        distanceEditText.setText(String.format("%.2f", currentRide.getDistance()));
        EditText speedEditText = findViewById(R.id.mod_data_speed);
        speedEditText.setText(String.format("%.2f", currentRide.getSpeed()));
        EditText cadenceEditText = findViewById(R.id.mod_data_cadence);
        cadenceEditText.setText(String.format("%.2f", currentRide.getCadence()));
        EditText commentEditText = findViewById(R.id.mod_data_comment);
        commentEditText.setText(currentRide.getComment());

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ride ride = setFields(currentRide);
                if (ride != null) {
                    returnData(ride, false);
                }
            }
        });

    }

    // for returning data back to parent screen
    private void returnData(Ride ride, boolean isNew) {
        Intent intent = new Intent();
        intent.putExtra("RIDE_DATA" , ride);
        if (isNew) {
            setResult(RideDataHandler.ADD, intent);
        } else {
            setResult(RideDataHandler.EDIT, intent);
        }

        super.finish();
    }

    // handles error checking and retrieval for data entry
    private Ride setFields(Ride currentRide) {
        boolean cleanData = true;
        EditText distanceView = findViewById(R.id.mod_data_distance);
        EditText speedView = findViewById(R.id.mod_data_speed);
        EditText cadenceView = findViewById(R.id.mod_data_cadence);
        EditText commentView = findViewById(R.id.mod_data_comment);

        if(commentView.getText().toString().length() > 20) {
            findViewById(R.id.error_comment).setVisibility(View.VISIBLE);
            cleanData = false;
        } else {
            this.getRide().setComment((commentView.getText().toString()));
            findViewById(R.id.error_comment).setVisibility(View.GONE);
        }

        try {
            this.getRide().setDistance(Double.parseDouble(distanceView.getText().toString()));
            findViewById(R.id.error_distance).setVisibility(View.GONE);
        } catch (NumberFormatException e) {
            findViewById(R.id.error_distance).setVisibility(View.VISIBLE);
            cleanData = false;
        }
        try {
            this.getRide().setSpeed(Double.parseDouble(speedView.getText().toString()));
            findViewById(R.id.error_speed).setVisibility(View.GONE);
        } catch (NumberFormatException e) {
            findViewById(R.id.error_speed).setVisibility(View.VISIBLE);
            cleanData = false;
        }
        try {
            this.getRide().setCadence(Double.parseDouble(cadenceView.getText().toString()));
            findViewById(R.id.error_cadence).setVisibility(View.GONE);
        } catch (NumberFormatException e) {
            findViewById(R.id.error_cadence).setVisibility(View.VISIBLE);
            cleanData = false;
        }
        if (!cleanData) {
            return null;
        }
        if (currentRide == null) {
            return this.getRide();
        } else {
            currentRide.setDistance(this.getRide().getDistance());
            currentRide.setDatetime(this.getRide().getDatetime());
            currentRide.setSpeed(this.getRide().getSpeed());
            currentRide.setComment(this.getRide().getComment());
            return currentRide;
        }
    }

    // from https://www.journaldev.com/9976/android-date-time-picker-dialog
    // Author: Anupam Chugh
    // configures date and time picker
    @Override
    public void onClick(View v) {
        if (v == this.btnDatePicker) {

            mYear = getRide().getDatetime().get(Calendar.YEAR);
            mMonth = getRide().getDatetime().get(Calendar.MONTH);
            mDay = getRide().getDatetime().get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            getRide().getDatetime().set(
                                    year, monthOfYear, dayOfMonth,
                                    getRide().getDatetime().get(Calendar.HOUR_OF_DAY),
                                    getRide().getDatetime().get(Calendar.MINUTE),
                                    getRide().getDatetime().get(Calendar.SECOND));
                            TextView dateView = findViewById(R.id.mod_data_date);
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            dateView.setText(dateFormat.format(getRide().getDatetime().getTime()));

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == this.btnTimePicker) {

            // Get Current Time
            mHour = getRide().getDatetime().get(Calendar.HOUR_OF_DAY);
            mMinute = getRide().getDatetime().get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            getRide().getDatetime().set(
                                    getRide().getDatetime().get(Calendar.YEAR),
                                    getRide().getDatetime().get(Calendar.MONTH),
                                    getRide().getDatetime().get(Calendar.DAY_OF_MONTH),
                                    hourOfDay, minute, 0);
                            TextView timeView = findViewById(R.id.mod_data_time);
                            DateFormat dateFormat = new SimpleDateFormat("hh:mm");
                            timeView.setText(dateFormat.format(getRide().getDatetime().getTime()));
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
}
