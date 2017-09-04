package com.jb.smartsetting.Common_Utility;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.jb.smartsetting.GPS_Utility.CustomLocation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by jeongbin.son on 2017-07-18.
 * Location Item Thumbnail 생성을 위해 MapView에서 받아온 Bitmap을 File저장, 해상도별 SnapShot의 중앙부분을 Crop한다.
 */

public class BitmapCropManager {

    private Context context;
    private Intent cropIntent;
    private Uri uri;
    private File isExistTempFile;

    private Bitmap beforeBitmap;
    private Bitmap afterBitmap;
    private FileOutputStream fos;
    private SharedPreferences pref;
    private boolean isDebug = false;
    private String TAG = getClass().getName();
    private ObjectReaderWriter objectReaderWriter;

    public BitmapCropManager(Context context) {
        this.context = context;
        objectReaderWriter = new ObjectReaderWriter(context);
        SharedPreferences pref = context.getSharedPreferences("settings", MODE_PRIVATE);
        isDebug = pref.getBoolean("setting_dev_mode", false);
    }

    public Bitmap cropBitmap(Bitmap bitmap, CustomLocation location) {
        try {
            if (onCheckDistanceLimit(location) == true) {
                isExistTempFile = new File(location.objFilePath + "crop_" + location.imgFileName);
                if (!isExistTempFile.exists()) {
                    beforeBitmap = Bitmap.createBitmap(bitmap);
                    // Width가 Height보다 긴경우 --> 태블릿
                    if (beforeBitmap.getWidth() >= beforeBitmap.getHeight()) {
                        afterBitmap = Bitmap.createBitmap(
                                beforeBitmap,
                                beforeBitmap.getWidth() / 4 - beforeBitmap.getHeight() / 4,
                                0,
                                beforeBitmap.getHeight(),
                                beforeBitmap.getHeight());
                    } else {
                        afterBitmap = Bitmap.createBitmap(
                                beforeBitmap,
                                0,
                                beforeBitmap.getHeight() / 3 - beforeBitmap.getWidth() / 3,
                                beforeBitmap.getWidth(),
                                beforeBitmap.getWidth()
                        );
                    }
                    fos = new FileOutputStream(new File(location.objFilePath + "crop_" + location.imgFileName));
                    afterBitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                    fos.close();
                }
            } else if (!onCheckDistanceLimit(location)) {
                afterBitmap = null;
            } else {
                afterBitmap = null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return afterBitmap;
    }

    private boolean onCheckDistanceLimit(CustomLocation location) {
        boolean result = true;
        ArrayList<CustomLocation> temp = objectReaderWriter.readObject();
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).toDistance(location.Latitude, location.Longitude) <= 200) {
                if(SettingValues.getInstance().IsDebug()){
                    Log.d(TAG, "저장할 수 없습니다\n현재위치와 "+temp.get(i).getLocationName()+"이(가) 200m이내입니다 ");
                }
                temp.clear();
                result = false;
                break;
            }
        }
        return result;
    }
}
