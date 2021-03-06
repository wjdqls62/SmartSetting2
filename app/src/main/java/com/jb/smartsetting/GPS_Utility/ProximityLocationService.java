package com.jb.smartsetting.GPS_Utility;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.jb.smartsetting.Common_Utility.ObjectReaderWriter;
import com.jb.smartsetting.Common_Utility.SettingValues;
import com.jb.smartsetting.Common_Utility.SettingsChangeManager;
import com.jb.smartsetting.R;

import java.util.ArrayList;

/**
 * Created by jeongbin.son on 2017-06-21.
 */

public class ProximityLocationService extends Service implements
        OnConnectionFailedListener,
        ConnectionCallbacks,
        LocationListener {
    // Thread 반복 Delay 주기(초 단위)
    private int SEARCH_LOCATION_DELAY_TIME = 60000 * 5;
    // 세부탐색 중 사용자 지정 위치와 현재위치간의 기준거리 (200m)
    private double PROXIMITY_ALERT_DISTANCE = 200;

    private ObjectReaderWriter objectReaderWriter;
    private LocationObserver locationObserver;
    private Location currentSearchedLocation, prevSearchedLocation;
    private ArrayList<CustomLocation> mEnabledTargetLocation;
    private Context context;

    private Bitmap bitmap;
    private Uri soundUri;
    private GoogleApiClient googleApiClient;

    private LocationRequest locationRequest;


    private SettingsChangeManager settingsChangeManager;

    private boolean isDebug, isShowNotification;
    private String TAG = getClass().getName();
    private boolean isRunning;
    private String lastProximityLocation = null;

    private void getPreference() {
        SharedPreferences pref = getSharedPreferences("settings", MODE_PRIVATE);
        isDebug = pref.getBoolean("setting_dev_mode", false);
        isShowNotification = pref.getBoolean("setting_common_noti", true);
        SettingValues.getInstance().setIsDebug(isDebug);
        SettingValues.getInstance().setIsShowNotification(isShowNotification);
    }

    @Override
    public void onCreate() {
        context = getApplicationContext();
        initGoogleApiClient();
        SettingValues.getInstance();
        mEnabledTargetLocation = new ArrayList<CustomLocation>();

        objectReaderWriter = new ObjectReaderWriter(getApplicationContext());
        if (SettingValues.getInstance().IsDebug()) {
            Log.d(TAG, "onCreate");
        }

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (SettingValues.getInstance().IsDebug()) {
            Log.d(TAG, "onStartCommand");
        }

        mEnabledTargetLocation = objectReaderWriter.readObject();
        settingsChangeManager = new SettingsChangeManager(getApplicationContext());
        getPreference();

        if (!(googleApiClient.isConnected()) || googleApiClient == null) {
            initGoogleApiClient();
        }

        try {
            for (int i = 0; i < mEnabledTargetLocation.size(); i++) {
                if (mEnabledTargetLocation.get(i).isEnabled) {
                    isRunning = true;
                    locationObserver = new LocationObserver();
                    locationObserver.execute();
                    break;
                }
            }
        } catch (SecurityException e) {
        }
        return START_STICKY;
    }

    private void initGoogleApiClient() {
        try {
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.connect();

            locationRequest = new LocationRequest();
            locationRequest.setFastestInterval(SEARCH_LOCATION_DELAY_TIME);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        } catch (SecurityException e) {

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return super.registerReceiver(receiver, filter);
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {
        super.unregisterReceiver(receiver);
    }

    @Override
    public void onDestroy() {
        if (SettingValues.getInstance().IsDebug()) Log.d(TAG, "onDestory");
        googleApiClient.disconnect();
        locationObserver.onCancelled(false);


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (SettingValues.getInstance().IsDebug()) Log.d(TAG, "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        currentSearchedLocation = location;
    }

    private class LocationObserver extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onCancelled(Boolean aBoolean) {
            if (SettingValues.getInstance().IsDebug()) Log.d(TAG, "onCancelled");
            isRunning = false;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            int refreshCount = 0;

            while (isRunning) {
                try {
                    mEnabledTargetLocation = objectReaderWriter.readObject();
                    prevSearchedLocation = currentSearchedLocation;
                    currentSearchedLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

                    if (prevSearchedLocation != null) {
                        // 마지막 근접했던 위치에 지속적으로 근접하고 있을경우
                        if (lastProximityLocation != null) {
                            if(objectReaderWriter.findObject(lastProximityLocation) != null){
                                if (objectReaderWriter.findObject(lastProximityLocation).toDistance(currentSearchedLocation.getLatitude(), currentSearchedLocation.getLongitude()) <= PROXIMITY_ALERT_DISTANCE) {
                                    if (SettingValues.getInstance().IsDebug()) {
                                        Log.d(TAG, "마지막 저장위치로부터 200m범위내에 있으므로 세부탐색을 생략합니다.");
                                    }
                                }else{
                                    lastProximityLocation = null;
                                }
                            }else{
                                lastProximityLocation = null;
                            }
                        } else {
                            for (int i = 0; i < mEnabledTargetLocation.size(); i++) {
                                // 등록지점과 근접한지 세부탐색
                                if(mEnabledTargetLocation.get(i).isEnabled){
                                    if (mEnabledTargetLocation.get(i).toDistance(currentSearchedLocation.getLatitude(), currentSearchedLocation.getLongitude()) <= PROXIMITY_ALERT_DISTANCE) {
                                        lastProximityLocation = mEnabledTargetLocation.get(i).getLocationName();
                                        settingsChangeManager.setSavedTargetLocation(mEnabledTargetLocation.get(i));
                                        settingsChangeManager.SettingChangeTrigger();
                                        showNotification(mEnabledTargetLocation.get(i).getLocationName());
                                        if (SettingValues.getInstance().IsDebug()) {
                                            Log.d(TAG, mEnabledTargetLocation.get(i).getLocationName() + "지점과 근접합니다! : " +
                                                    mEnabledTargetLocation.get(i).toDistance(currentSearchedLocation.getLatitude(), currentSearchedLocation.getLongitude()) + "m");
                                        }
                                        break;
                                    }else{
                                        lastProximityLocation = null;
                                    }
                                }
                            }
                        }
                    } else {
                        if (SettingValues.getInstance().IsDebug())
                            Log.d(TAG, "Fail getLastLocation... ");
                    }
                    refreshCount++;
                    Thread.sleep(SEARCH_LOCATION_DELAY_TIME);
                } catch (SecurityException e) {

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (SettingValues.getInstance().IsDebug()) {
                        if (currentSearchedLocation != null) {
                            Log.d(TAG, "========================== LocationObserver Thread is Runnig =============================");
                            Log.d(TAG, "Lat : " + currentSearchedLocation.getLatitude());
                            Log.d(TAG, "Long : " + currentSearchedLocation.getLongitude());
                            Log.d(TAG, "Refresh Count : " + refreshCount);
                            Log.d(TAG, "Lasted Proximity Location :" + lastProximityLocation);
                            Log.d(TAG, "==========================================================================================");
                        }
                    }
                }
            }
            return null;
        }

        public void showNotification(String targetLocationName) {
            if (SettingValues.getInstance().isShowNotification()) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(android.R.drawable.ic_menu_myplaces)
                        .setLargeIcon(bitmap)
                        .setContentTitle("SmartSetting")
                        .setContentText(targetLocationName + " 지점에 근접합니다!")
                        .setAutoCancel(true)
                        .setSound(soundUri);
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, notificationBuilder.build());
            }
        }
    }
}