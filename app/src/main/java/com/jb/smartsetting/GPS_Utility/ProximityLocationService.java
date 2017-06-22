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

/**
 * Created by jeongbin.son on 2017-06-21.
 */

public class ProximityLocationService extends Service {
    private LocationManager locationManager;
    private GPS_Receiver gpsReceiver;
    private ObjectReaderWriter objectReaderWriter;
    private Context context;

    private Intent intent;
    private IntentFilter intentFilter;
    private PendingIntent pendingIntent;

    private String TAG = getClass().getName();
    private boolean isDebug = true;

    PermissionCallback pCallback;
    SettingCallback sCallback;
    AddProximityAlertCallback aCallback;

    public interface AddProximityAlertCallback {
        public void addProximityAlert();
    }

    public interface PermissionCallback{
        public void requestPermission();
    }
    public interface SettingCallback{
        public void changeCustomSetting();
        public void restoreSetting();
    }

    public void setPermissionCallback(PermissionCallback callback){
        pCallback = callback;
    }

    public void setSettingCallback(SettingCallback callback){
        sCallback = callback;
    }

    public void setProximityCallback(AddProximityAlertCallback callback){
        Log.d(TAG, "AddProximityAlertCallback");
        aCallback = callback;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        gpsReceiver = new GPS_Receiver();
        intentFilter = new IntentFilter("com.jb.smartsetting");
        intent = new Intent("com.jb.smartsetting");
        pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);
        objectReaderWriter = new ObjectReaderWriter(getApplicationContext());
        Log.d(TAG, "onCreate");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Intent : "+intent.getAction());
        try{
            registerReceiver(gpsReceiver, intentFilter);
            aCallback.addProximityAlert();
        }catch (SecurityException e){
            pCallback.requestPermission();
        }

        return super.onStartCommand(intent, flags, startId);
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
        super.onDestroy();
    }
    private class GPS_Receiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
                boolean isEntering = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);
                if(isEntering){
                    Toast.makeText(context, "목표지점에 근접합니다.", Toast.LENGTH_SHORT).show();
                    //sCallback.changeCustomSetting();
                }else{
                    Toast.makeText(context, "목표지점에서 떨어졌습니다.", Toast.LENGTH_SHORT).show();
                    //sCallback.restoreSetting();
                }
            }
        }
    }

