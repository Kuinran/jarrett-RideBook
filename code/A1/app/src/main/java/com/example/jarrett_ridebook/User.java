package com.example.jarrett_ridebook;

import java.util.ArrayList;

// stores user level data, primarily all rides and some calculations
public class User {
    protected ArrayList<Ride> rides;

    public User() {
        rides = new ArrayList<>();
    }

    public ArrayList<Ride> getRides() {
        return this.rides;
    }

    // just in case
    public void setRides(ArrayList<Ride> rides) {
        this.rides = rides;
    }

    // just in case
    public void addRide(Ride ride) {
        this.rides.add(ride);
    }

    public float getTotalDistance() {
        float total = 0;
        for(Ride ride:this.rides){
            total += ride.getDistance();
        }
        return total;
    }
}
