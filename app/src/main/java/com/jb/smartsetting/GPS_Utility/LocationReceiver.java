package com.jb.smartsetting.GPS_Utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by wjdql on 2017-06-17.
 */

public class LocationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isEntering = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);
        if(isEntering){
            Toast.makeText(context, "목표지점 도착하여 설정값 변경", Toast.LENGTH_SHORT).show();
            Log.d("ProximityAlert", "Recede");
        }else{
            Toast.makeText(context, "목표와 멀어짐", Toast.LENGTH_SHORT).show();
            Log.d("ProximityAlert", "Proximity");
        }
    }
}
