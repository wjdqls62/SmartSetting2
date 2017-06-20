package com.jb.smartsetting;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jb.smartsetting.GPS_Utility.Stub_Location_Object;

import java.io.Serializable;

public class Sub_ItemSetting_Activity extends AppCompatActivity{
    private Stub_Location_Object location;
    private Bundle bundle;

    CollapsingToolbarLayout toolbarLayout;
    BitmapDrawable bitmapDrawable;
    Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub__item_setting_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        location = (Stub_Location_Object) getIntent().getExtras().getSerializable("Location");
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);



        Toast.makeText(getApplicationContext(), "Lat:"+location.Latitude+ " Long:"+location.Longitude, Toast.LENGTH_LONG).show();






    }
}
