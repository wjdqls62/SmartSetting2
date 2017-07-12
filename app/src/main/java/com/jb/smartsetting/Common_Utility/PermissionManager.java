package com.jb.smartsetting.Common_Utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
    }

    public boolean isPermissionCheck(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, "권한이 미수락되어있습니다", Toast.LENGTH_SHORT).show();
            movePermissionActivity();
            return false;
        }else{
            return true;
        }
    }

    public void onRequestPermission(){
        ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.WRITE_SETTINGS
                    , Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.ACCESS_FINE_LOCATION
                    , Manifest.permission.ACCESS_COARSE_LOCATION
                    , Manifest.permission.INTERNET}, MY_PERMISSION);
    }

    public boolean onPermissionResultTransaction(int requestCode, String[] permissions, int[] grantResults){
        boolean result = false;
        for(int i=0; i<permissions.length; i++){
            if(grantResults[i] == -1){
                Toast.makeText(context, "권한사용을 미수락하여 어플리케이션을 종료합니다", Toast.LENGTH_SHORT).show();
                android.os.Process.killProcess(android.os.Process.myPid());
            }else{
                result = true;
                break;
            }
        }
        return result;
    }
}