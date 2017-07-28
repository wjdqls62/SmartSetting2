package com.jb.smartsetting.Common_Utility;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.jb.smartsetting.GPS_Utility.SavedCustomLocation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by jeongbin.son on 2017-07-18.
 */

public class BitmapCropManager {

    private Context context;
    private Intent cropIntent;
    private Uri uri;

    private Bitmap beforeBitmap;
    private Bitmap afterBitmap;
    private FileOutputStream fos;
    private SharedPreferences pref;
    private boolean isDebug = false;
    private String TAG = getClass().getName();

    public BitmapCropManager(Context context){
        this.context = context;
        SharedPreferences pref = context.getSharedPreferences("settings", MODE_PRIVATE);
        isDebug = pref.getBoolean("setting_dev_mode", false);
    }

    public Bitmap cropBitmap(Bitmap bitmap, SavedCustomLocation location) {
        try {
            beforeBitmap = Bitmap.createBitmap(bitmap);
            // Width가 Height보다 긴경우 --> 태블릿
            if(beforeBitmap.getWidth() >= beforeBitmap.getHeight()){
                afterBitmap = Bitmap.createBitmap(
                        beforeBitmap,
                        beforeBitmap.getWidth()/4 - beforeBitmap.getHeight()/4,
                        0,
                        beforeBitmap.getHeight(),
                        beforeBitmap.getHeight());
            }else{
                afterBitmap = Bitmap.createBitmap(
                        beforeBitmap,
                        0,
                        beforeBitmap.getHeight()/4 - beforeBitmap.getWidth()/4,
                        beforeBitmap.getWidth(),
                        beforeBitmap.getWidth()
                );
            }
            fos = new FileOutputStream(new File(location.objFilePath+"crop_"+location.imgFileName));
            afterBitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return afterBitmap;
    }
}
