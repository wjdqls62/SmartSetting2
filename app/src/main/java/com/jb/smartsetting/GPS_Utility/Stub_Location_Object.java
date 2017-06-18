package com.jb.smartsetting.GPS_Utility;

import java.io.Serializable;

/**
 * Created by wjdql on 2017-06-17.
 */

public class Stub_Location_Object implements Serializable {

    public double Latitude, Longitude, Altitude;
    public float Accuracy;

    public void parseLocation(android.location.Location location){
        Latitude = location.getLatitude();
        Longitude = location.getLongitude();
        Accuracy = location.getAccuracy();
        Altitude = location.getAltitude();
    }


}
