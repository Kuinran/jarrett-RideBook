package com.example.jarrett_ridebook;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

// Adapted from https://stackoverflow.com/questions/20985668/is-there-a-way-to-update-multiple-textview-with-arrayadapter written by Amulya Khare
// last edited Jan 8 2014
// used to customize listview for clean formatting
// rational: I didn't want to use a more complicated view. This adapter is important for ui
// design and cleanliness
public class RideAdapter extends ArrayAdapter<Ride> {
    private final Context context;
    private final ArrayList<Ride> data;
    private final int layoutResourceId;

    public RideAdapter(Context context, int layoutResourceId, ArrayList<Ride> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            //row = LayoutInflater.from(context).inflate(R.layout.list_content,parent,false);
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();

            holder.distanceView = (TextView)row.findViewById(R.id.list_content_distance);
            holder.dateView = (TextView)row.findViewById(R.id.list_content_date);
            holder.timeView = (TextView)row.findViewById(R.id.list_content_time);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        Ride ride = data.get(position);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("hh:mm");
        holder.dateView.setText(dateFormat.format(ride.getDatetime().getTime()));
        holder.timeView.setText(timeFormat.format(ride.getDatetime().getTime()));
        holder.distanceView.setText(String.format("%.2fkm", ride.getDistance()));

        return row;
    }

    static class ViewHolder {
        TextView distanceView;
        TextView dateView;
        TextView timeView;
    }
}
