package com.jb.smartsetting.GPS_Utility;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.jb.smartsetting.R;

import org.w3c.dom.Text;

import java.io.Serializable;

/**
 * Created by wjdql on 2017-06-17.
 */

public class Stub_Location_Object implements Serializable {

    // 위치정보 변수 정의
    public String locationName;
    public double Latitude, Longitude, Altitude;
    public float Accuracy;
    public boolean isEnabled = false;
    public ImageView imageView;

    public void parseLocation(android.location.Location location){
        Latitude = location.getLatitude();
        Longitude = location.getLongitude();
        Accuracy = location.getAccuracy();
        Altitude = location.getAltitude();
        this.imageView = imageView;
    }

    public String getLocationName(){
        return locationName;
    }


}
