package com.jb.smartsetting.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jb.smartsetting.Common_Utility.CustomDialog;
import com.jb.smartsetting.Common_Utility.IDialogCallback;
import com.jb.smartsetting.Common_Utility.ObjectReaderWriter;
import com.jb.smartsetting.GPS_Utility.CustomLocation;
import com.jb.smartsetting.R;

import java.util.ArrayList;

public class Sub_ItemSetting_Activity extends AppCompatActivity implements
        View.OnClickListener,
        TextWatcher {

    private int MODE_CURRENT = 0;
    private final int MODE_WRITE = 1;
    private final int MODE_MODIFY = 2;

    private ArrayList<CustomLocation> arrStubLocation;
    private CustomLocation stubLocation;
    private double mRestoreIndentificationNumber;

    private Bundle bundle;
    private ObjectReaderWriter objectReaderWriter;
    private FloatingActionButton btn_save;

    private Toolbar toolbar;
    private EditText etLocationName;
    private TextView txSoundMode, txBluetoothMode, txWiFiMode;

    private CollapsingToolbarLayout toolbarLayout;
    private ImageView locationThumnail;
    private LinearLayout item_setting_sound;
    private LinearLayout item_setting_bluetooth;
    private LinearLayout item_setting_wifi;

    private CustomDialog customDialog;
    private IDialogCallback callback;

    private SharedPreferences pref;
    private boolean isDebug = false;
    private String TAG = getClass().getName();

    private void getPreference() {
        SharedPreferences pref = getSharedPreferences("settings", MODE_PRIVATE);
        isDebug = pref.getBoolean("setting_dev_mode", false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub__item_setting_);
        initView();

        setSupportActionBar(toolbar);
        toolbarLayout.setTitle("Location Name");
        btn_save.setOnClickListener(this);
        item_setting_sound.setOnClickListener(this);
        item_setting_bluetooth.setOnClickListener(this);
        item_setting_wifi.setOnClickListener(this);
        etLocationName.addTextChangedListener(this);


        objectReaderWriter = new ObjectReaderWriter(getApplicationContext());
        arrStubLocation = new ArrayList<>();
        arrStubLocation = objectReaderWriter.readObject();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (onDisplayTypeCheck(bundle) == MODE_WRITE) {
                stubLocation = (CustomLocation) bundle.getSerializable("Location");
                MODE_CURRENT = MODE_WRITE;
            } else if (onDisplayTypeCheck(bundle) == MODE_MODIFY) {
                MODE_CURRENT = MODE_MODIFY;
                mRestoreIndentificationNumber = bundle.getDouble("indentificationNumber");
                for (int i = 0; i < arrStubLocation.size(); i++) {
                    if (arrStubLocation.get(i).indentificationNumber == mRestoreIndentificationNumber) {
                        stubLocation = arrStubLocation.get(i);
                        fillRestoreData();
                        break;
                    }
                }
            }
        }
        locationThumnail.setAlpha(70);
        locationThumnail.setImageBitmap(BitmapFactory.decodeFile(stubLocation.objFilePath+"crop_"+stubLocation.imgFileName));
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btn_save = (FloatingActionButton) findViewById(R.id.fab);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        locationThumnail = (ImageView) findViewById(R.id.collapsing_thumnail);
        etLocationName = (EditText) findViewById(R.id.location_name);
        item_setting_sound = (LinearLayout) findViewById(R.id.item_set_sound);
        item_setting_bluetooth = (LinearLayout) findViewById(R.id.item_set_bluetooth);
        item_setting_wifi = (LinearLayout) findViewById(R.id.item_set_wifi);
        txSoundMode = (TextView) findViewById(R.id.sound_mode);
        txBluetoothMode = (TextView) findViewById(R.id.bluetooth_mode);
        txWiFiMode = (TextView) findViewById(R.id.wifi_mode);
    }

    private void move_LocationList_Activity() {
        Intent intent = new Intent(this, Main_LocationList_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    // 사용자가 입력한 LocationName이 공백을 검사
    private boolean onValidateForm() {
        String inputedLocationName = etLocationName.getText().toString();
        if (inputedLocationName.isEmpty() && inputedLocationName.equals("")) {
            Toast.makeText(getApplicationContext(), "1글자 이상 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void onReadyToModify() {
        stubLocation.locationName = etLocationName.getText().toString();
        stubLocation.SoundType = txSoundMode.getText().toString();
        stubLocation.BluetoothType = txBluetoothMode.getText().toString();
        stubLocation.WiFiType = txWiFiMode.getText().toString();

        // stub객체 저장
        objectReaderWriter.saveObject(stubLocation);
        move_LocationList_Activity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(MODE_CURRENT == MODE_WRITE){
            boolean isTempFile = true;
            for(int i=0; i<arrStubLocation.size(); i++){
                if(arrStubLocation.get(i).indentificationNumber == stubLocation.indentificationNumber){
                    isTempFile = false;
                    break;
                }
            }
            if(isTempFile){
                objectReaderWriter.deleteObject(stubLocation);
            }
            Intent intent = new Intent(this, Main_LocationList_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_set_sound:
                callback = new IDialogCallback() {
                    @Override
                    public void onDialogEventCallback(String SelectedItem) {
                        txSoundMode.setText(SelectedItem);
                    }
                };
                customDialog = new CustomDialog(this, "SETTING_SOUND", callback, txSoundMode.getText().toString());
                customDialog.show();
                break;

            case R.id.item_set_bluetooth :
                callback = new IDialogCallback() {
                    @Override
                    public void onDialogEventCallback(String SelectedItem) {
                        txBluetoothMode.setText(SelectedItem);
                    }
                };
                customDialog = new CustomDialog(this, "SETTING_BT", callback, txBluetoothMode.getText().toString());
                customDialog.show();
                break;

            case R.id.item_set_wifi :
                callback = new IDialogCallback() {
                    @Override
                    public void onDialogEventCallback(String SelectedItem) {
                        txWiFiMode.setText(SelectedItem);
                    }
                };
                customDialog = new CustomDialog(this, "SETTING_WIFI", callback, txWiFiMode.getText().toString());
                customDialog.show();
                break;

            case R.id.fab:
                // 입력한 데이터를 stub객체에 삽입
                if (MODE_CURRENT == MODE_MODIFY) {
                    onReadyToModify();
                    break;
                } else if (MODE_CURRENT == MODE_WRITE) {
                    // MODE_WRITE && 기존 저장된 데이터와 위도,경도가 겹칠경우
                    if (isOverlap()) {
                        break;
                        // MODE_WRITE && 기존 저장된 데이터와 중복이 없을경우
                    } else if (!onValidateForm()) {
                        break;
                    } else {
                        stubLocation.locationName = etLocationName.getText().toString();
                        // stub객체 저장
                        objectReaderWriter.saveObject(stubLocation);
                        move_LocationList_Activity();
                        break;
                    }
                }
            case R.id.btn_cancel :
                onBackPressed();
                break;
        }
    }

    public int onDisplayTypeCheck(Bundle bundle) {
        if (bundle.getString("DISPLAY_MODE").equals("MODIFY")) {
            return MODE_MODIFY;
        } else {
            return MODE_WRITE;
        }
    }

    private void fillRestoreData() {
        Log.d("TEST", "fillRestoreData()");
        // 수정모드(MODE_MODIFY)의 경우 객체의 정보를 뷰에 채워준다.
        etLocationName.setText(stubLocation.getLocationName());
        txSoundMode.setText(stubLocation.SoundType);
        txBluetoothMode.setText(stubLocation.BluetoothType);
        txWiFiMode.setText(stubLocation.WiFiType);
    }

    private boolean isOverlap() {
        if (MODE_CURRENT == MODE_WRITE) {
            for (int i = 0; i < arrStubLocation.size(); i++) {
                if (arrStubLocation.get(i).indentificationNumber == stubLocation.indentificationNumber) {
                    Toast.makeText(getApplicationContext(), "기존 등록된 위치와 중복됩니다", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.toString().isEmpty()){
            toolbarLayout.setTitle("Location Name");
        }else{
            toolbarLayout.setTitle(s.toString());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
