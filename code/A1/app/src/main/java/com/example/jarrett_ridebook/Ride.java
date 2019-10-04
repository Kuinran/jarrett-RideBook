package com.example.jarrett_ridebook;


import java.io.Serializable;
import java.util.Calendar;

// ride class for storing ride data
// rational: for clean data management
public class Ride implements Serializable {
    protected Calendar datetime;
    protected double distance;
    protected double speed;
    protected double cadence;
    protected String comment;

    public Ride() {
        setDatetime(Calendar.getInstance());
        setDistance(0);
        setSpeed(0);
        setCadence(0);
        setComment("");
    }

    public Calendar getDatetime() {return this.datetime;}
    public void setDatetime(Calendar datetime) {this.datetime = datetime;}

    public double getDistance() {return this.distance;}
    public void setDistance(double distance) {this.distance = distance;}

    public double getSpeed() {return this.speed;}
    public void setSpeed(double speed) {this.speed = speed;}

    public double getCadence() {return this.cadence;}
    public void setCadence(double cadence) {this.cadence = cadence;}

    public String getComment() {return this.comment;}
    public void setComment(String comment) {this.comment = comment;}

}
