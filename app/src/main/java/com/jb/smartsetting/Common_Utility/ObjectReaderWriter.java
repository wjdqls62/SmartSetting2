package com.jb.smartsetting.Common_Utility;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.jb.smartsetting.GPS_Utility.SavedCustomLocation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by jeongbin.son on 2017-06-21.
 */

public class ObjectReaderWriter {

    private Context context;
    private ArrayList<SavedCustomLocation> arrLoccationList;

    private String TAG = getClass().getName();
    private boolean isDebug = true;

    public ObjectReaderWriter(Context context){
        this.context = context;
        arrLoccationList = new ArrayList<SavedCustomLocation>();
    }

    public void saveObject(SavedCustomLocation location) {
        try {
            FileOutputStream fos = new FileOutputStream(location.objFilePath + location.objFileName, false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(location);
            fos.close();
            oos.close();
        } catch (IOException e) {
            Toast.makeText(context, "Object write to Fail!", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<SavedCustomLocation> readObject() {
        try {
            File[] searchFileList = new File("/data/data/com.jb.smartsetting/files/").listFiles();
            for (int i = 0; i < searchFileList.length; i++) {
                if (searchFileList[i].getName().contains(".sjb")) {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream("/data/data/com.jb.smartsetting/files/"+searchFileList[i].getName()));
                    SavedCustomLocation readLoaction = (SavedCustomLocation) ois.readObject();
                    arrLoccationList.add(readLoaction);
                    ois.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            Log.d(TAG, "Not yet created /data/data/com.jb.smartsetting/files/");
        }
        return arrLoccationList;
    }

}
