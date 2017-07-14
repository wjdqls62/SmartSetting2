package com.jb.smartsetting.View;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.jb.smartsetting.Common_Utility.ObjectReaderWriter;
import com.jb.smartsetting.R;

import java.util.ArrayList;

public class Sub_ItemSetting_Activity extends AppCompatActivity implements View.OnClickListener{
    private int MODE_CURRENT = 0;
    private final int MODE_WRITE = 1;
    private final int MODE_MODIFY = 2;

    private ArrayList<Location> arrLocation;
    private android.location.Location takedLocation;
    private double mRestoreIndentificationNumber;

    private Bundle bundle;
    private ObjectReaderWriter objectReaderWriter;
    private FloatingActionButton btn_save;

    private Toolbar toolbar;
    private EditText etLocationName;
    private SeekBar sbRingtoneSound;
    private SeekBar sbNotificationSound;
    private SeekBar sbTouch_feedbackSound;
    private SeekBar sbMediaSound;

    private String TAG = this.getClass().getName();
    private boolean isDebug = true;


    CollapsingToolbarLayout toolbarLayout;
    BitmapDrawable bitmapDrawable;
    Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub__item_setting_);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btn_save = (FloatingActionButton) findViewById(R.id.fab);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        etLocationName = (EditText) findViewById(R.id.location_name);
        sbRingtoneSound = (SeekBar) findViewById(R.id.sb_ringtone);
        sbNotificationSound = (SeekBar) findViewById(R.id.sb_notification);
        sbTouch_feedbackSound = (SeekBar) findViewById(R.id.sb_touchfeedback);
        sbMediaSound = (SeekBar) findViewById(R.id.sb_media);


        setSupportActionBar(toolbar);
        btn_save.setOnClickListener(this);

        objectReaderWriter = new ObjectReaderWriter(getApplicationContext());
        arrLocation = new ArrayList<>();
        arrLocation = objectReaderWriter.readObject();
        bundle = getIntent().getExtras();

        if(bundle != null){
            if(onDisplayTypeCheck(bundle) == MODE_WRITE){
                takedLocation = getIntent().getParcelableExtra("Location");
                MODE_CURRENT = MODE_WRITE;
                if(isDebug){
                    Log.d(TAG, "objFileName :" + takedLocation.getExtras().getString("objFileName"));
                    Log.d(TAG, "imgFileName :" + takedLocation.getExtras().getString("imgFileName"));
                    Log.d(TAG, "indentificationNumber :" + takedLocation.getExtras().getDouble("indentificationNumber")+"");
                    Log.d(TAG, "objFilePath :" + takedLocation.getExtras().getString("objFilePath"));
                }
            }else if(onDisplayTypeCheck(bundle) == MODE_MODIFY){
                mRestoreIndentificationNumber = bundle.getDouble("indentificationNumber");
                for(int i = 0; i< arrLocation.size(); i++){
                    if(arrLocation.get(i).getExtras().getDouble("indentificationNumber") == mRestoreIndentificationNumber){
                        takedLocation = arrLocation.get(i);
                        fillRestoreData();
                        break;
                    }
                }
                MODE_CURRENT = MODE_MODIFY;
                Toast.makeText(getApplicationContext(), "MODE_MODIFY", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void move_LocationList_Activity(){
        Intent intent = new Intent(this, Main_LocationList_Activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fab :
                takedLocation.getExtras().putString("locationName", etLocationName.getText().toString());
                objectReaderWriter.saveObject(takedLocation);
                move_LocationList_Activity();
                break;
        }
    }

    public int onDisplayTypeCheck(Bundle bundle){
        if(bundle.getString("DISPLAY_MODE").equals("MODIFY")){
            return MODE_MODIFY;
        }else{
            return MODE_WRITE;
        }
    }

    private void fillRestoreData(){

    }

    private void isOverlap(){
    }

}
