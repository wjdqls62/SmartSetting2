package com.jb.smartsetting.Common_Utility;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.AudioManager;
import android.net.wifi.WifiManager;

import com.jb.smartsetting.GPS_Utility.CustomLocation;

/**
 * Created by jeongbin.son on 2017-07-18.
 */

public class SettingsChangeManager {
   private AudioManager audioManager;
   private BluetoothAdapter bluetoothAdapter;
   private WifiManager wifiManager;
   private Context context;

    CustomLocation targetCustomLocation;

    public SettingsChangeManager(Context context){
        this.context = context;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }


    public void SettingChangeTrigger(){
        onSoundChange();
        onBluetoothChange();
        onWiFiChange();
    }

    // 소리설정이 되어있으면 소리값을 변경한다
    private void onSoundChange(){
        switch (targetCustomLocation.SoundType){
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

    private void onBluetoothChange(){
        if(bluetoothAdapter != null){
            switch (targetCustomLocation.BluetoothType){
                case "On" :
                    if(bluetoothAdapter.getState() == BluetoothAdapter.STATE_TURNING_OFF
                            || bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF){
                        bluetoothAdapter.enable();
                    }
                    break;
                case "Off" :
                    if(bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON
                            || bluetoothAdapter.getState() == BluetoothAdapter.STATE_TURNING_ON){
                        bluetoothAdapter.disable();
                    }
                    break;
            }
        }
    }

    private void onWiFiChange(){
        switch (targetCustomLocation.WiFiType){
            case "On" :
                if(wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED
                        || wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLING){
                    wifiManager.setWifiEnabled(true);
                }
                break;
            case "Off" :
                if(wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING
                        || wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED){
                    wifiManager.setWifiEnabled(false);
                }
                break;
        }
    }

    public void setSavedTargetLocation(CustomLocation targetLocationItem){
        this.targetCustomLocation = targetLocationItem;
    }
}
