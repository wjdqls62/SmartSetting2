package com.jb.smartsetting;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jb.smartsetting.Common_Utility.ObjectReaderWriter;
import com.jb.smartsetting.GPS_Utility.Stub_Location_Object;

public class Sub_ItemSetting_Activity extends AppCompatActivity implements View.OnClickListener{
    private Stub_Location_Object stubLocation;
    private Bundle bundle;
    private ObjectReaderWriter objectReaderWriter;
    private FloatingActionButton btn_save;

    CollapsingToolbarLayout toolbarLayout;
    BitmapDrawable bitmapDrawable;
    Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub__item_setting_);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        btn_save = (FloatingActionButton) findViewById(R.id.fab);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        setSupportActionBar(toolbar);
        btn_save.setOnClickListener(this);

        objectReaderWriter = new ObjectReaderWriter(getApplicationContext());

        stubLocation = (Stub_Location_Object) getIntent().getExtras().getSerializable("Location");

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
                move_LocationList_Activity();
                objectReaderWriter.saveObject(stubLocation);
                break;
        }
    }
}
