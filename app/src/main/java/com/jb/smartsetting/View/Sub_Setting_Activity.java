package com.jb.smartsetting.View;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.jb.smartsetting.Common_Utility.SettingValues;
import com.jb.smartsetting.Common_Utility.SettingsChangeManager;
import com.jb.smartsetting.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wjdql on 2017-07-17.
 */

public class Sub_Setting_Activity extends PreferenceActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private SwitchPreference sw_setting_dev_mode;
    private SwitchPreference sw_setting_common_noti;
    private Preference setting_build_date;

    private boolean isDebug = false;
    private boolean isShowNotification = false;

    private String TAG = getClass().getName();

    private void getPreference() {
        SharedPreferences pref = getSharedPreferences("settings", MODE_PRIVATE);
        isDebug = pref.getBoolean("setting_dev_mode", false);
        isShowNotification = pref.getBoolean("setting_common_noti", false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.location_item_preference);

        SettingValues.getInstance();
        pref = getSharedPreferences("settings", MODE_PRIVATE);
        editor = pref.edit();

        // 개발자 모드
        sw_setting_dev_mode = (SwitchPreference) findPreference("setting_dev_mode");
        sw_setting_dev_mode.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (((SwitchPreference) preference).isChecked()) {
                    editor.putBoolean(((SwitchPreference) preference).getKey(), false).commit();
                    sw_setting_dev_mode.setChecked(false);
                    isDebug = false;
                } else {
                    editor.putBoolean(((SwitchPreference) preference).getKey(), true).commit();
                    sw_setting_dev_mode.setChecked(true);
                    isDebug = true;
                }
                SettingValues.getInstance().setIsDebug(isDebug);
                return false;
            }
        });

        // 위치근접 알림
        sw_setting_common_noti = (SwitchPreference) findPreference("setting_common_noti");
        sw_setting_common_noti.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (((SwitchPreference) preference).isChecked()) {
                    editor.putBoolean(((SwitchPreference) preference).getKey(), false).commit();
                    sw_setting_common_noti.setChecked(false);
                    isShowNotification = false;
                } else {
                    editor.putBoolean(((SwitchPreference) preference).getKey(), true).commit();
                    sw_setting_common_noti.setChecked(true);
                    isShowNotification = true;
                }
                SettingValues.getInstance().setIsShowNotification(isShowNotification);
                return false;
            }
        });

        setting_build_date = findPreference("setting_build_date");
        setting_build_date.setSummary(getBuildDate() + "");
    }

    private String getBuildDate() {
        String buildDate = null;
        try {
            ApplicationInfo applicationInfo = getApplicationContext().getPackageManager().getApplicationInfo(getApplicationContext().getPackageName(), 0);
            ZipFile zipFile = new ZipFile(applicationInfo.sourceDir);
            ZipEntry zipEntry = zipFile.getEntry("classes.dex");
            long time = zipEntry.getTime();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
            buildDate = simpleDateFormat.format(new java.util.Date(time));
            zipFile.close();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buildDate;
    }

}

