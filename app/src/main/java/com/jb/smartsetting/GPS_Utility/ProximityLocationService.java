package com.jb.smartsetting.GPS_Utility;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.jb.smartsetting.Common_Utility.ObjectReaderWriter;
import com.jb.smartsetting.Common_Utility.SettingsChangeManager;
import com.jb.smartsetting.R;

import java.util.ArrayList;

/**
 * Created by jeongbin.son on 2017-06-21.
 */

public class ProximityLocationService extends Service implements OnConnectionFailedListener, ConnectionCallbacks {
    // Thread 반복 Delay 주기(초 단위)
    private int SEARCH_LOCATION_DELAY_TIME = 60000 * 5;
    //private int SEARCH_LOCATION_DELAY_TIME = 5000;
    // 근접알림 반경 설정(미터 단위)
    private double PROXIMITY_ALERT_DISTANCE = 200;

    private LocationManager locationManager;
    //private GPS_Receiver gpsReceiver;
    private ObjectReaderWriter objectReaderWriter;
    private LocationObserver locationObserver;
    private Location currentLocation, prevLocation;
    private ArrayList<CustomLocation> mEnabledTargetLocation;

    private Context context;

    private Intent intent;
    private IntentFilter intentFilter;
    private PendingIntent pIntent;
    private ArrayList<PendingIntent> enabledPendingIntent;

    private Bitmap bitmap;
    private Uri soundUri;
    private PendingIntent nIntent;
    private GoogleApiClient googleApiClient;

    private SettingsChangeManager settingsChangeManager;

    private String TAG = getClass().getName();
    private boolean isDebug = true;
    private boolean isRunning = false;


    @Override
    public void onCreate() {

        context = getApplicationContext();
        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();


        mEnabledTargetLocation = new ArrayList<CustomLocation>();
        enabledPendingIntent = new ArrayList<PendingIntent>();

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //gpsReceiver = new GPS_Receiver();

        objectReaderWriter = new ObjectReaderWriter(getApplicationContext());

        if (isDebug) {
            Log.d(TAG, "onCreate");
        }
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 시작되면 활성화되어 있는 Location을 찾는다
        if (isDebug) {
            Log.d(TAG, "onStartCommand");
        }

        googleApiClient.connect();
        mEnabledTargetLocation = objectReaderWriter.readObject();
        intentFilter = new IntentFilter();
        settingsChangeManager = new SettingsChangeManager(getApplicationContext());

        try {
            for (int i = 0; i < mEnabledTargetLocation.size(); i++) {
                if (mEnabledTargetLocation.get(i).isEnabled) {
                    isRunning = true;
                    locationObserver = new LocationObserver();
                    locationObserver.execute();
                    if (isDebug) {
                        Log.d(TAG, "Start LocationObserver Thread");
                    }
                    break;
                    //this.intent = new Intent(String.valueOf(mEnabledTargetLocation.get(i).indentificationNumber));
                    //pIntent = PendingIntent.getBroadcast(context, i, this.intent, 0);
                    //enabledPendingIntent.add(pIntent);
                    //intentFilter.addAction(String.valueOf(mEnabledTargetLocation.get(i).indentificationNumber));
                    //locationManager.addProximityAlert(
                    //        mEnabledTargetLocation.get(i).Latitude,
                    //        mEnabledTargetLocation.get(i).Longitude,
                    //        100,
                    //        -1,
                    //        pIntent);
                    //if (isDebug) {
                    //    Log.d(TAG, "========================== addProximityAlert =============================");
                    //    Log.d(TAG, "Enable Location Name : " + mEnabledTargetLocation.get(i).locationName);
                    //    Log.d(TAG, "Lat : " + mEnabledTargetLocation.get(i).Latitude);
                    //    Log.d(TAG, "Long : " + mEnabledTargetLocation.get(i).Longitude);
                    //    Log.d(TAG, "Pending Intent Action : " + String.valueOf(mEnabledTargetLocation.get(i).indentificationNumber));
                    //    Log.d(TAG, "==========================================================================");
                    //}
                }
            }

        } catch (SecurityException e) {

        }

        return START_STICKY;
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
        if (isDebug) Log.d(TAG, "onDestroy");
        isRunning = false;
        //unregisterReceiver(gpsReceiver);
        super.onDestroy();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "GoogleApi is Ready!");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private class LocationObserver extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            int refreshCount = 0;
            float distance = 0f;

            while (isRunning) {
                try {
                    prevLocation = currentLocation;
                    currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

                    if (prevLocation != null) {
                        distance = currentLocation.distanceTo(prevLocation);
                        // currentLocation 와 prevLocation의 거리를 계산해 반경300m이상 떨어질 경우 || 서비스 시작 후 Refresh 2회 이하의 경우
                        if (distance >= PROXIMITY_ALERT_DISTANCE || refreshCount <= 2) {
                            for (int i = 0; i < mEnabledTargetLocation.size(); i++) {
                                // 사용자 등록지점과 근접할 경우
                                if (mEnabledTargetLocation.get(i).toDistance(currentLocation.getLatitude(), currentLocation.getLongitude()) <= PROXIMITY_ALERT_DISTANCE) {

                                    settingsChangeManager.setSavedTargetLocation(mEnabledTargetLocation.get(i));
                                    settingsChangeManager.SettingChangeTrigger();

                                    showNotification(mEnabledTargetLocation.get(i).getLocationName());

                                    if (isDebug) {
                                        Log.d(TAG, mEnabledTargetLocation.get(i).getLocationName() + "지점과 근접합니다! : " +
                                                mEnabledTargetLocation.get(i).toDistance(currentLocation.getLatitude(), currentLocation.getLongitude()) + "m");
                                    }
                                }
                            }
                        }
                    } else {
                        if (isDebug) Log.d(TAG, "마지막 측정위치와 동일합니다. ");
                    }

                    refreshCount++;
                    Thread.sleep(SEARCH_LOCATION_DELAY_TIME);

                } catch (SecurityException e) {

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (isDebug) {
                        if (currentLocation != null) {
                            Log.d(TAG, "========================== LocationObserver Thread is Runnig =============================");
                            Log.d(TAG, "Lat : " + currentLocation.getLatitude());
                            Log.d(TAG, "Long : " + currentLocation.getLongitude());
                            Log.d(TAG, "Refresh Count : " + refreshCount);
                            Log.d(TAG, "==========================================================================================");
                        } else {
                            Log.d(TAG, "Fail the current location to get!");
                        }
                    }
                }
            }
            return null;
        }

        public void showNotification(String targetLocationName) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(android.R.drawable.ic_menu_myplaces)
                    .setLargeIcon(bitmap)
                    .setContentTitle("SmartSetting")
                    .setContentText(targetLocationName + "지점에 근접합니다.")
                    .setAutoCancel(true)
                    .setSound(soundUri);
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        }
    }
}