package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public static final String SPLIT_TEXT = " of ";
    public EarthquakeAdapter(@NonNull Context context, @NonNull List<Earthquake> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentView = convertView;
        if (currentView == null) {
            currentView = LayoutInflater.from(getContext()).
                    inflate(R.layout.list_item, parent, false);
        }
        Earthquake earthquake = getItem(position);


        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        TextView magnitude = (TextView) currentView.findViewById(R.id.magnitude);
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();
        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(earthquake.getMagnitue());
        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        DecimalFormat formater = new DecimalFormat("0.00");
        String mag = formater.format(earthquake.getMagnitue());
        magnitude.setText(mag);

        String place = earthquake.getPlace();
        String offset ;
        String primary ;
       /*

       ** split String using indexof method **


       int split=place.indexOf(SPLIT_TEXT);
        if(split!= -1)
        {
            offset = place.substring(0,split+SPLIT_TEXT.length());
            primary = place.substring(split+SPLIT_TEXT.length(),place.length());

        }
        else {
            primary = place;
            offset = "Near the";
        }*/

        /**
         * split String by split method
         * contains method find whether specified part exists in String or not
         */
        if (place.contains(SPLIT_TEXT)) {
            String[] parts = place.split(SPLIT_TEXT);
            offset = parts[0] + SPLIT_TEXT;
            primary = parts[1];
        } else {
            offset = "Near the";
            primary = place;
        }

        TextView Oplace = (TextView) currentView.findViewById(R.id.o_place);
        Oplace.setText(offset);

        TextView Pplace = (TextView) currentView.findViewById(R.id.p_place);
        Pplace.setText(primary);

        /**
         * convert milliseconds to object because format method required object type
         */
        Date dateObject = new Date(earthquake.getDate());


        TextView date = (TextView) currentView.findViewById(R.id.date);
        String formattedDate = formatDate(dateObject);
        date.setText(formattedDate);

        TextView time = (TextView) currentView.findViewById(R.id.time);
        String formattedtime = formatTime(dateObject);
        time.setText(formattedtime);


        return currentView;


    }

    /**
     * @param dateObject converted result of millisecond
     * @return date in specified format(String type);
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    /**
     *
     * @param dateObject converted result of millisecond
     *      * @return time in specified format(String type);
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
