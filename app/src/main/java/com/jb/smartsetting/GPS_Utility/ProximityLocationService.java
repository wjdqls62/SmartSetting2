package com.jb.smartsetting.GPS_Utility;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.jb.smartsetting.Common_Utility.ObjectReaderWriter;
import com.jb.smartsetting.ILocationService;

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
    private PendingIntent pendingIntent;

    private String TAG = getClass().getName();
    private boolean isDebug = true;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        mEnabledTargetLocation = new ArrayList<Stub_Location_Object>();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        gpsReceiver = new GPS_Receiver();

        objectReaderWriter = new ObjectReaderWriter(getApplicationContext());
        Log.d(TAG, "onCreate");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 시작되면 활성화되어 있는 Location을 찾는다
        mEnabledTargetLocation = objectReaderWriter.readObject();

        for(int i=0; i<mEnabledTargetLocation.size(); i++){
            if(mEnabledTargetLocation.get(i).isEnabled){
                try{
                    this.intent = new Intent(String.valueOf(mEnabledTargetLocation.get(i).indentificationNumber));
                    intentFilter = new IntentFilter(String.valueOf(mEnabledTargetLocation.get(i).indentificationNumber));
                    pendingIntent = PendingIntent.getBroadcast(context, 1, this.intent, 0);
                    registerReceiver(gpsReceiver, intentFilter);
                    locationManager.addProximityAlert(
                            mEnabledTargetLocation.get(i).Latitude,
                            mEnabledTargetLocation.get(i).Longitude,
                            100f,
                            -1,
                            pendingIntent);
                    Toast.makeText(getApplicationContext(), "Start Service", Toast.LENGTH_SHORT).show();
                }catch (SecurityException e){
                }
            }
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
        Toast.makeText(getApplicationContext(), "Stop Service", Toast.LENGTH_SHORT).show();
        unregisterReceiver(gpsReceiver);
    }

    private class GPS_Receiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isEntering = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);
            for(int i=0; i<mEnabledTargetLocation.size(); i++){
                if(String.valueOf(mEnabledTargetLocation.get(i).indentificationNumber)== intent.getAction()){
                    if(isEntering){
                        Toast.makeText(context, mEnabledTargetLocation.get(i).getLocationName()+"과 근접합니다.", Toast.LENGTH_SHORT).show();
                        //sCallback.changeCustomSetting();
                    }else{
                        Toast.makeText(context, mEnabledTargetLocation.get(i).getLocationName()+"과 멀어졌습니다.", Toast.LENGTH_SHORT).show();
                        //sCallback.restoreSetting();
                    }
                }
            }


            }
        }
    }

