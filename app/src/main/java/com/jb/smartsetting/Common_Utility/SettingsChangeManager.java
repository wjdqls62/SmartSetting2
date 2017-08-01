package com.jb.smartsetting.Common_Utility;

import android.content.Context;
import android.media.AudioManager;

import com.jb.smartsetting.GPS_Utility.SavedCustomLocation;
import com.jb.smartsetting.R;

/**
 * Created by jeongbin.son on 2017-07-18.
 */

public class SettingsChangeManager {
    AudioManager audioManager;
    Context context;

    SavedCustomLocation targetLocationItem;

    public SettingsChangeManager(Context context){
        this.context = context;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }


    public void SettingChangeTrigger(){
        onSoundChange();
    }

    // 소리설정이 되어있으면 소리값을 변경한다
    private void onSoundChange(){
        switch (targetLocationItem.SoundType){
            case "Vibrate" :
                if(audioManager.getRingerMode() != AudioManager.RINGER_MODE_VIBRATE || audioManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT){
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                }
                break;
            case "Sound" :
                if(audioManager.getRingerMode() != AudioManager.RINGER_MODE_NORMAL || audioManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT){
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }
                break;
        }
    }

    public void setSavedTargetLocation(SavedCustomLocation targetLocationItem){
        this.targetLocationItem = targetLocationItem;
    }
}
