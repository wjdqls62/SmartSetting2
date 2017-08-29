package com.jb.smartsetting.Common_Utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.jb.smartsetting.View.Sub_PermissionActivity;

/**
 * Created by jeongbin.son on 2017-07-12.
 */

public class PermissionManager {

    private final int MY_PERMISSION = 0;
    private Activity activity;
    private Context context;
    private Intent intent;

    public PermissionManager(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    public void movePermissionActivity(){
        intent = new Intent(activity, Sub_PermissionActivity.class);
        activity.startActivity(intent);
        activity.finish();

    }

    public boolean isPermissionCheck(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            movePermissionActivity();
            return false;
        }else{
            return true;
        }
    }

    public void onRequestPermission(){
        ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.ACCESS_WIFI_STATE
                    , Manifest.permission.BLUETOOTH
                    , Manifest.permission.BLUETOOTH_ADMIN
                    , Manifest.permission.CHANGE_WIFI_STATE
                    , Manifest.permission.RECEIVE_BOOT_COMPLETED
                    , Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.ACCESS_FINE_LOCATION
                    , Manifest.permission.ACCESS_COARSE_LOCATION
                    , Manifest.permission.INTERNET}, MY_PERMISSION);

        onRequestSystemSettingPermission();
    }

    public void onRequestSystemSettingPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(!Settings.System.canWrite(context)){
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivity(intent);
            }
        }
    }

    public boolean onPermissionResultTransaction(int requestCode, String[] permissions, int[] grantResults){
        boolean result = true;
        if (SettingValues.getInstance().IsDebug()){
            for(int i=0; i<permissions.length; i++){
                Log.d("TEST", "======================================");
                Log.d("TEST", "Permissions : "+permissions[i]);
                Log.d("TEST", "grantResults : "+grantResults[i]);
                Log.d("TEST", "======================================");
            }
        }
        
        for(int i=0; i<permissions.length; i++){
            if(grantResults[i] == -1){
                result = false;
                break;
            }
        }
        return result;
    }
}