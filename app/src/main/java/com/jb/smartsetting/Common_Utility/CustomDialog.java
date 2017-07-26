package com.jb.smartsetting.Common_Utility;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.WindowManager;

import com.jb.smartsetting.R;

/**
 * Created by jeongbin.son on 2017-07-26.
 */

public class CustomDialog extends Dialog {
    WindowManager.LayoutParams layoutParams;
    private String DIALOG_TYPE = "SETTING_SOUND";

    public CustomDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.1f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.dialog_sound);

    }
}
