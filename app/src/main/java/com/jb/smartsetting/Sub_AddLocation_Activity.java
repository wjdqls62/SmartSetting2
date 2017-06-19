package com.jb.smartsetting;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;

/**
 * Created by jeongbin.son on 2017-06-19.
 */

public class Sub_AddLocation_Activity extends NMapActivity implements View.OnClickListener{
    private final String CLIENT_ID = "_8FNGPHUyEXgG3pNLL6Q";
    private Button btn_ok, btn_cancel;
    private NMapView mMapView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_location_item_setting);

        mMapView = (NMapView) findViewById(R.id.mapview);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_ok = (Button) findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);



    }

    public void getLocationSuccess(){
        Intent intent = new Intent(this, Sub_ItemSetting_Activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok :
                getLocationSuccess();
                break;
            case R.id.btn_cancel :
                getLocationSuccess();
                break;
        }
    }
}
