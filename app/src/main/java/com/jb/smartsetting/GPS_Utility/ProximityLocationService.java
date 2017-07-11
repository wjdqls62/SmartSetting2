package com.jb.smartsetting.GPS_Utility;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.jb.smartsetting.Common_Utility.ObjectReaderWriter;

import java.util.ArrayList;

/**
 * Created by jeongbin.son on 2017-06-21.
 */

public class ProximityLocationService extends Service {
    private LocationManager locationManager;
    private GPS_Receiver gpsReceiver;
    private ObjectReaderWriter objectReaderWriter;
    private ArrayList<Stub_Location_Object> mEnabledTargetLocation;

    private Context context;

    private Intent intent;
    private IntentFilter intentFilter;
    private PendingIntent pIntent;
    private ArrayList<PendingIntent> enabledPendingIntent;

    private String TAG = getClass().getName();
    private boolean isDebug = true;

    @Override
    public void onCreate() {

        context = getApplicationContext();
        mEnabledTargetLocation = new ArrayList<Stub_Location_Object>();
        enabledPendingIntent = new ArrayList<PendingIntent>();

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        gpsReceiver = new GPS_Receiver();

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



        mEnabledTargetLocation = objectReaderWriter.readObject();
        intentFilter = new IntentFilter();

        try {
            for (int i = 0; i < mEnabledTargetLocation.size(); i++) {
                if (mEnabledTargetLocation.get(i).isEnabled) {
                    this.intent = new Intent(String.valueOf(mEnabledTargetLocation.get(i).indentificationNumber));
                    pIntent = PendingIntent.getBroadcast(context, i, this.intent, 0);

                    enabledPendingIntent.add(pIntent);
                    intentFilter.addAction(String.valueOf(mEnabledTargetLocation.get(i).indentificationNumber));

                    locationManager.addProximityAlert(
                            mEnabledTargetLocation.get(i).Latitude,
                            mEnabledTargetLocation.get(i).Longitude,
                            100,
                            -1,
                            pIntent);

                    if (isDebug) {
                        Log.d(TAG, "========================== addProximityAlert =============================");
                        Log.d(TAG, "Enable Location Name : " + mEnabledTargetLocation.get(i).locationName);
                        Log.d(TAG, "Lat : " + mEnabledTargetLocation.get(i).Latitude);
                        Log.d(TAG, "Long : " + mEnabledTargetLocation.get(i).Longitude);
                        Log.d(TAG, "Pending Intent Action : " + String.valueOf(mEnabledTargetLocation.get(i).indentificationNumber));
                        Log.d(TAG, "==========================================================================");
                    }
                }
            }

        } catch (SecurityException e) {

        } finally {
            registerReceiver(gpsReceiver, intentFilter);
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
        unregisterReceiver(gpsReceiver);
        super.onDestroy();
    }


    public class GPS_Receiver extends BroadcastReceiver {
        private String Current_Location = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isEntering = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);
            if(isEntering){
                for(int i=0; i<mEnabledTargetLocation.size(); i++){
                    if(String.valueOf(mEnabledTargetLocation.get(i).indentificationNumber).equals(intent.getAction())){
                        Toast.makeText(getApplicationContext(), mEnabledTargetLocation.get(i).getLocationName()+"에 접근", Toast.LENGTH_SHORT).show();
                        Current_Location = mEnabledTargetLocation.get(i).getLocationName();
                        break;
                    }
                }
            }else{
                Toast.makeText(getApplicationContext(), Current_Location +"에 멀어짐", Toast.LENGTH_SHORT).show();
                Current_Location = null;
            }

            if(isDebug){
            Log.d(TAG, "========================== GPS_Receiver.onReceive =============================");
            Log.d(TAG, "isEntering : " + isEntering);
            Log.d(TAG, "Intent.getAction : " + intent.getAction());
            Log.d(TAG, "==============================================================================");
        }
        }
    }
}