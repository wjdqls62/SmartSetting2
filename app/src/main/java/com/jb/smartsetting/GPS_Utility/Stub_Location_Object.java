package com.jb.smartsetting.GPS_Utility;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jb.smartsetting.R;

import org.w3c.dom.Text;

import java.io.Serializable;

/**
 * Created by wjdql on 2017-06-17.
 */

public class Stub_Location_Object implements Serializable {

    public String locationName;
    public double Latitude, Longitude, Altitude;
    public float Accuracy;
    public boolean isEnabled = false;

    public void parseLocation(android.location.Location location){
        Latitude = location.getLatitude();
        Longitude = location.getLongitude();
        Accuracy = location.getAccuracy();
        Altitude = location.getAltitude();
    }

    public String getLocationName(){
        return locationName;
    }


}
