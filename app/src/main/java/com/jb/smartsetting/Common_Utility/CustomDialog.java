package com.jb.smartsetting.Common_Utility;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.Toast;

import com.jb.smartsetting.R;

/**
 * Created by jeongbin.son on 2017-07-26.
 */

public class CustomDialog extends Dialog implements View.OnClickListener {


    WindowManager.LayoutParams layoutParams;
    private String DIALOG_MODE = "";
    private String DIALOG_TYPE = "SETTING_SOUND";
    private String USER_SELECTED_ITEM = "";

    private RadioButton radio_sound_none;
    private RadioButton radio_sound_vibrate;
    private RadioButton radio_sound_sound;

    private RadioButton radio_bt_none;
    private RadioButton radio_bt_on;
    private RadioButton radio_bt_off;

    private RadioButton radio_wifi_none;
    private RadioButton radio_wifi_on;
    private RadioButton radio_wifi_off;

    private IDialogCallback callback;

    public CustomDialog(@NonNull Context context, String DIALOG_MODE, IDialogCallback callback, String USER_SELECTED_ITEM) {
        super(context);
        this.DIALOG_MODE = DIALOG_MODE;
        this.callback = callback;
        this.USER_SELECTED_ITEM = USER_SELECTED_ITEM;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.1f;
        getWindow().setAttributes(layoutParams);

        if(DIALOG_MODE.equals("SETTING_SOUND")){
            setContentView(R.layout.dialog_sound);
            init_SoundSetting_View();
        }
        else if(DIALOG_MODE.equals("SETTING_BT")){
            setContentView(R.layout.dialog_bluetooth);
            initBluetoothSetting_View();
        }
        else if(DIALOG_MODE.equals("SETTING_WIFI")){
            setContentView(R.layout.dialog_wifi);
            initWiFiSetting_View();

        }
    }

    private void init_SoundSetting_View() {
        radio_sound_none = (RadioButton) findViewById(R.id.set_sound_none);
        radio_sound_vibrate = (RadioButton) findViewById(R.id.set_sound_vibrate);
        radio_sound_sound = (RadioButton) findViewById(R.id.set_sound_sound);

        if(USER_SELECTED_ITEM.equals("None")){
            radio_sound_none.setChecked(true);
        }else if(USER_SELECTED_ITEM.equals("Vibrate")){
            radio_sound_vibrate.setChecked(true);
        }else if(USER_SELECTED_ITEM.equals("Sound")){
            radio_sound_sound.setChecked(true);
        }

        radio_sound_none.setOnClickListener(this);
        radio_sound_vibrate.setOnClickListener(this);
        radio_sound_sound.setOnClickListener(this);
    }

    private void initBluetoothSetting_View(){
        radio_bt_none = (RadioButton) findViewById(R.id.set_bt_none);
        radio_bt_on = (RadioButton) findViewById(R.id.set_bt_on);
        radio_bt_off = (RadioButton) findViewById(R.id.set_bt_off);

        if(USER_SELECTED_ITEM.equals("None")){
            radio_bt_none.setChecked(true);
        }else if(USER_SELECTED_ITEM.equals("On")){
            radio_bt_on.setChecked(true);
        }else if(USER_SELECTED_ITEM.equals("Off")){
            radio_bt_off.setChecked(true);
        }

        radio_bt_none.setOnClickListener(this);
        radio_bt_on.setOnClickListener(this);
        radio_bt_off.setOnClickListener(this);
    }

    private void initWiFiSetting_View(){
        radio_wifi_none = (RadioButton) findViewById(R.id.set_wifi_none);
        radio_wifi_on = (RadioButton) findViewById(R.id.set_wifi_on);
        radio_wifi_off = (RadioButton) findViewById(R.id.set_wifi_off);

        if(USER_SELECTED_ITEM.equals("None")){
            radio_wifi_none.setChecked(true);
        }else if(USER_SELECTED_ITEM.equals("On")){
            radio_wifi_on.setChecked(true);
        }else if(USER_SELECTED_ITEM.equals("Off")){
            radio_wifi_off.setChecked(true);
        }

        radio_wifi_none.setOnClickListener(this);
        radio_wifi_on.setOnClickListener(this);
        radio_wifi_off.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // Sound
            case R.id.set_sound_none :
                USER_SELECTED_ITEM = "None";
                radio_sound_none.setChecked(true);
                break;
            case R.id.set_sound_vibrate :
                USER_SELECTED_ITEM = "Vibrate";
                radio_sound_vibrate.setChecked(true);
                break;
            case R.id.set_sound_sound :
                USER_SELECTED_ITEM = "Sound";
                radio_sound_sound.setChecked(true);
                break;

            // Bluetooth
            case R.id.set_bt_none :
                USER_SELECTED_ITEM = "None";
                radio_bt_none.setChecked(true);
                break;
            case R.id.set_bt_on :
                USER_SELECTED_ITEM = "On";
                radio_bt_on.setChecked(true);
                break;
            case R.id.set_bt_off :
                USER_SELECTED_ITEM = "Off";
                radio_bt_off.setChecked(true);
                break;

            // Wi-Fi
            case R.id.set_wifi_none :
                USER_SELECTED_ITEM = "None";
                radio_wifi_none.setChecked(true);
                break;
            case R.id.set_wifi_on :
                USER_SELECTED_ITEM = "On";
                radio_wifi_on.setChecked(true);
                break;
            case R.id.set_wifi_off :
                USER_SELECTED_ITEM = "Off";
                radio_wifi_off.setChecked(true);
                break;

        }
        callback.onDialogEventCallback(USER_SELECTED_ITEM);
        this.dismiss();
    }
}
