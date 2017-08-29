package com.jb.smartsetting.Common_Utility;

import android.content.SharedPreferences;

import com.jb.smartsetting.View.Sub_Setting_Activity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by jeongbin.son on 2017-08-29.
 */

public class SettingValues {
    private static SettingValues settingValues;
    private boolean isDebug;
    private boolean isShowNotification;

    public static synchronized SettingValues getInstance() {
        if(settingValues == null){
            settingValues = new SettingValues();
        }
        return settingValues;
    }

    public void setIsDebug(boolean isDebug){
        this.isDebug = isDebug;
    }
    public void setIsShowNotification(boolean isShowNotification){
        this.isShowNotification = isShowNotification;
    }

    public boolean IsDebug(){
        return isDebug;
    }

    public boolean isShowNotification(){
        return isShowNotification;
    }


}
