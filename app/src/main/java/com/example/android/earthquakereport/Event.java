package com.example.android.earthquakereport;

import android.support.v4.content.ContextCompat;

public class Event {

    /** Variables containing the info about the earthquake*/
    private String mLocation;
    private String mOffset;
    private double mMagnitude;
    private String mDate;
    private String mTime;
    private String mUrl;

    public Event(String Location,String Offset,double Magnitude,String Date,String Time,String url){
        mLocation = Location;
        mOffset = Offset;
        mMagnitude = Magnitude;
        mDate = Date;
        mTime = Time;
        mUrl = url;
    }

    /** Getters for the different variables*/

    public String getLocation() {
        return mLocation;
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public String getDate() {
        return mDate;
    }

    public String getTime(){
        return mTime;
    }

    public String getOffset(){
        return mOffset;
    }

    public String getUrl(){
        return mUrl;
    }

}
