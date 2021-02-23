package com.example.android.earthquakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {

    /**
     * Array adapter to show the details of each earthquake
     * @param context the activity where the list is shown
     * @param Earthquakes the array list containing the information avout the earthquakes
     */
    public EventAdapter(Activity context, ArrayList<Event> Earthquakes){
        super(context,0,Earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Event currentEvent = getItem(position);

        // Text view for Location
        TextView locationTextView = listItemView.findViewById(R.id.location_text_view);
        locationTextView.setText(currentEvent.getLocation());
        TextView offsetTextView = listItemView.findViewById(R.id.offset_text_view);
        offsetTextView.setText(currentEvent.getOffset());

        // Text view for Magnitude
        TextView magnitudeTextView = listItemView.findViewById(R.id.magnitude_text_view);
        magnitudeTextView.setText(formatMagnitude(currentEvent.getMagnitude()));

        GradientDrawable magnitudeCircle =(GradientDrawable) magnitudeTextView.getBackground();
        int magnitudeColor = getMagnitudeColor(currentEvent.getMagnitude());
        magnitudeCircle.setColor(ContextCompat.getColor(getContext(),magnitudeColor));

        // Text view for Date
        TextView dateTextView = listItemView.findViewById(R.id.date_text_view);
        dateTextView.setText(currentEvent.getDate());

        // Text view for time
        TextView timeTextView = listItemView.findViewById(R.id.time_text_view);
        timeTextView.setText(currentEvent.getTime());



        return listItemView;
    }
    private String formatMagnitude(double magnitude){
        DecimalFormat magFormatter = new DecimalFormat("0.0");
        return magFormatter.format(magnitude);
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
        return magnitudeColorResourceId;
    }

}
