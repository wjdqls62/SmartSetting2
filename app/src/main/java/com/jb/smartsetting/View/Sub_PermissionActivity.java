package com.jb.smartsetting.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;

import com.jb.smartsetting.Common_Utility.PermissionManager;
import com.jb.smartsetting.R;

/**
 * Created by jeongbin.son on 2017-07-12.
 */

public class Sub_PermissionActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_ok, btn_cancel;
    private ListView permission_list;

    private PermissionManager permissionManager;

    private SharedPreferences pref;
    private boolean isDebug = false;
    private String TAG = getClass().getName();

    private void getPreference(){
        SharedPreferences pref = getSharedPreferences("settings", MODE_PRIVATE);
        isDebug = pref.getBoolean("setting_dev_mode", false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sub_permission);
        permissionManager = new PermissionManager(this, getApplicationContext());

        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
        getSupportActionBar().setTitle("권한안내");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(permissionManager.onPermissionResultTransaction(requestCode, permissions, grantResults)){
            finish();
        }
    }

    public void btn_ok_press(){
        permissionManager = new PermissionManager(this, getApplicationContext());
        permissionManager.onRequestPermission();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok :
                btn_ok_press();
                break;
        }
    }
}
