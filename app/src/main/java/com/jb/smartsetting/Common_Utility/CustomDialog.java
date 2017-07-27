package com.jb.smartsetting.Common_Utility;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;

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

    private IDialogCallback callback;

    public CustomDialog(@NonNull Context context, String DIALOG_MODE, IDialogCallback callback) {
        super(context);
        this.DIALOG_MODE = DIALOG_MODE;
        this.callback = callback;
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
    }

    private void init_SoundSetting_View() {
        radio_sound_none = (RadioButton) findViewById(R.id.set_sound_none);
        radio_sound_vibrate = (RadioButton) findViewById(R.id.set_sound_vibrate);
        radio_sound_sound = (RadioButton) findViewById(R.id.set_sound_sound);

        radio_sound_none.setOnClickListener(this);
        radio_sound_vibrate.setOnClickListener(this);
        radio_sound_sound.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_sound_none :
                USER_SELECTED_ITEM = "None";
                break;
            case R.id.set_sound_vibrate :
                USER_SELECTED_ITEM = "Vibrate";
                break;
            case R.id.set_sound_sound :
                USER_SELECTED_ITEM = "Sound";
                break;
        }
        callback.onDialogEventCallback(USER_SELECTED_ITEM);
        this.dismiss();
    }
}