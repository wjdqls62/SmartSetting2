package com.jb.smartsetting.View;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jb.smartsetting.Common_Utility.ObjectReaderWriter;
import com.jb.smartsetting.GPS_Utility.Stub_Location_Object;
import com.jb.smartsetting.R;

import java.util.ArrayList;

public class Sub_ItemSetting_Activity extends AppCompatActivity implements View.OnClickListener{
    private int MODE_CURRENT = 0;
    private final int MODE_WRITE = 1;
    private final int MODE_MODIFY = 2;

    private ArrayList<Stub_Location_Object> arrStubLocation;
    private Stub_Location_Object stubLocation;
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
        arrStubLocation = new ArrayList<>();
        arrStubLocation = objectReaderWriter.readObject();
        bundle = getIntent().getExtras();

        if(bundle != null){
            if(onDisplayTypeCheck(bundle) == MODE_WRITE){
                stubLocation = (Stub_Location_Object) bundle.getSerializable("Location");
                MODE_CURRENT = MODE_WRITE;
                Toast.makeText(getApplicationContext(), "MODE_WRITE", Toast.LENGTH_SHORT).show();
            }else if(onDisplayTypeCheck(bundle) == MODE_MODIFY){
                mRestoreIndentificationNumber = bundle.getDouble("indentificationNumber");
                for(int i=0; i< arrStubLocation.size(); i++){
                    if(arrStubLocation.get(i).indentificationNumber == mRestoreIndentificationNumber){
                        stubLocation = arrStubLocation.get(i);
                        fillRestoreData();
                        break;
                    }
                }
                MODE_CURRENT = MODE_MODIFY;
                Toast.makeText(getApplicationContext(), "MODE_MODIFY", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), stubLocation.getLocationName(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "MODE_CURRENT : "+MODE_CURRENT, Toast.LENGTH_SHORT).show();
                // 입력한 데이터를 stub객체에 삽입
                if(MODE_CURRENT == MODE_MODIFY){
                    stubLocation.locationName = etLocationName.getText().toString();
                    stubLocation.setSoundVolume(
                            sbRingtoneSound.getProgress(),
                            sbNotificationSound.getProgress(),
                            sbTouch_feedbackSound.getProgress(),
                            sbMediaSound.getProgress());
                    // stub객체 저장
                    objectReaderWriter.saveObject(stubLocation);
                    move_LocationList_Activity();
                    break;
                }else if(MODE_CURRENT == MODE_WRITE){
                    // MODE_WRITE && 기존 저장된 데이터와 위도,경도가 겹칠경우
                    if(isOverlap()){
                        Toast.makeText(getApplicationContext(), "Overlap", Toast.LENGTH_SHORT).show();
                        break;
                    // MODE_WRITE && 기존 저장된 데이터와 중복이 없을경우
                    }else{
                        stubLocation.locationName = etLocationName.getText().toString();
                        stubLocation.setSoundVolume(
                                sbRingtoneSound.getProgress(),
                                sbNotificationSound.getProgress(),
                                sbTouch_feedbackSound.getProgress(),
                                sbMediaSound.getProgress());
                        // stub객체 저장
                        objectReaderWriter.saveObject(stubLocation);
                        move_LocationList_Activity();
                    }
                }
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
        Log.d("TEST","fillRestoreData()");
        // 수정모드(MODE_MODIFY)의 경우 객체의 정보를 뷰에 채워준다.
        etLocationName.setText(stubLocation.getLocationName());
    }

    private boolean isOverlap(){
        if(MODE_CURRENT == MODE_WRITE){
            for(int i=0; i<arrStubLocation.size(); i++){
                if(arrStubLocation.get(i).indentificationNumber == stubLocation.indentificationNumber){
                    return true;
                }
            }
        }
        return false;
    }

}
