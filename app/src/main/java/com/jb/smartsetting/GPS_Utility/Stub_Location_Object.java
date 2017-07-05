package com.jb.smartsetting.GPS_Utility;

import android.graphics.Bitmap;
import java.io.Serializable;

/**
 * Created by wjdql on 2017-06-17.
 */

public class Stub_Location_Object implements Serializable {
    private static final long serialVersionUID = 8124905464753305656L;
    // 위치정보 변수 정의
    public double indentificationNumber = 0;

    public String locationName;
    public int ringtone = 0;
    public int notification = 0;
    public int touchfeedback = 0;
    public int media = 0;

    public double Latitude, Longitude, Altitude;
    public float Accuracy;


    public boolean isEnabled = false;
    public Bitmap bitmap;
    public String imgFileName;
    public String objFilePath;
    public String objFileName;



    public void parseLocation(android.location.Location location){
        Latitude = location.getLatitude();
        Longitude = location.getLongitude();
        Accuracy = location.getAccuracy();
        Altitude = location.getAltitude();
        indentificationNumber =Altitude+Latitude;
        objFileName = Altitude+Latitude+".sjb";
        imgFileName = Altitude+Latitude+".png";
        objFilePath = "/data/data/com.jb.smartsetting/files/";
    }

    public String getLocationName(){
        return locationName;
    }

    public void setEnabled(boolean value){
        isEnabled = value;
    }

    public void setSoundVolume(int ringtone, int notification, int touchfeedback, int media){
        this.ringtone = ringtone;
        this.notification = notification;
        this.touchfeedback = touchfeedback;
        this.media = media;
    }


}
