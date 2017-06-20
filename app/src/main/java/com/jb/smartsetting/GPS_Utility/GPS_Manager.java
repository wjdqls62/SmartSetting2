package com.jb.smartsetting.GPS_Utility;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by wjdql on 2017-06-17.
 */

public class GPS_Manager implements LocationListener{
    public final static int READY_GPS_NETWORK = 1;
    public final static int GPS_NETWORK_NOT_ENABLED = -1;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 0;


    private LocationManager locationManager;
    private Location location;
    private Criteria criteria;

    private Context context;
    private String locationBestProvider;

    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private int ReadyToWork = 0;

    private final String TAG = this.getClass().getName();
    private boolean isDebug = true;

    public GPS_Network_Callback callback;

    public interface GPS_Network_Callback{
        public void onLocationChaged(Location location);
        public void onRequestPermission();
        public void onPanic();
    }

    public GPS_Manager(Context context){
        this.context = context;
    }

    public void setCallback(GPS_Network_Callback callback){
        this.callback = callback;
    }

    public void init(){
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(true);
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
        locationBestProvider = locationManager.getBestProvider(criteria, true);

        if(isDebug){
            Log.d(TAG, "isGPSEnabled : "+ isGPSEnabled);
            Log.d(TAG, "isNetworkEnabled : "+ isNetworkEnabled);
            Log.d(TAG, "locationBestProvider : "+ locationBestProvider);
        }
    }

    public int onLocation_Precondition_Check(){
        if(!isNetworkEnabled && !isGPSEnabled){
            callback.onPanic();
            return GPS_NETWORK_NOT_ENABLED;
        }else{
            return READY_GPS_NETWORK;
        }
    }

    public LocationManager getLocationManager(){
        return locationManager;
    }

    public Location getLocation(){
        if(onLocation_Precondition_Check() == READY_GPS_NETWORK){
            try{
                if(isGPSEnabled){
                    locationManager.requestLocationUpdates(locationBestProvider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    location = locationManager.getLastKnownLocation(locationBestProvider);
                }else{
                    locationManager.requestLocationUpdates(locationBestProvider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    location = locationManager.getLastKnownLocation(locationBestProvider);
                }
            }catch (SecurityException e){
                callback.onRequestPermission();
            }
        }
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        callback.onLocationChaged(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
