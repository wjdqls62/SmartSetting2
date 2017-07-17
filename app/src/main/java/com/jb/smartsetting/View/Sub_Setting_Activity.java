package com.jb.smartsetting.View;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;

import com.jb.smartsetting.R;

/**
 * Created by wjdql on 2017-07-17.
 */

public class Sub_Setting_Activity extends PreferenceActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.location_item_preference);
    }
}
