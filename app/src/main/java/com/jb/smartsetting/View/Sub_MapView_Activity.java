package com.jb.smartsetting.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jb.smartsetting.Common_Utility.BitmapCropManager;
import com.jb.smartsetting.GPS_Utility.GPS_Manager;
import com.jb.smartsetting.GPS_Utility.SavedCustomLocation;
import com.jb.smartsetting.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by jeongbin.son on 2017-06-19.
 */

public class Sub_MapView_Activity extends AppCompatActivity implements View.OnClickListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMapLoadedCallback,
        GoogleMap.SnapshotReadyCallback,
        LocationListener {

    private Bundle bundle;

    private GoogleMap map;
    BitmapCropManager bitmapCropManager;
    private GoogleMap.SnapshotReadyCallback SnapshotCallback;

    private MarkerOptions options;
    private LatLng latLng;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;

    SavedCustomLocation savedCustomLocation;

    private Button btn_ok, btn_cancel;

    private boolean isDebug = true;
    private String TAG = getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_map_view);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapview);
        mapFragment.getMapAsync(this);
        init_GoogleApiClientBuilder();

        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        savedCustomLocation = new SavedCustomLocation();
        bitmapCropManager = new BitmapCropManager(getApplicationContext());
    }

    private void init_GoogleApiClientBuilder(){
        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void refreshMapView(){
        Toast.makeText(this,"Location"+String.valueOf("LastLocation"+lastLocation.getLatitude()+
                "::"+lastLocation.getLongitude()),Toast.LENGTH_SHORT).show();

        options = new MarkerOptions();
        latLng = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
        options.position(latLng);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        Marker marker = map.addMarker(options);
        marker.showInfoWindow();
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sub_mapview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_mapview_refresh) {
            try{
                lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                if(lastLocation != null){
                    refreshMapView();
                }
            }catch (SecurityException e){

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void move_ItemSetting_Activity() {
        try{
            Intent intent = new Intent(this, Sub_ItemSetting_Activity.class);
            if(lastLocation != null){
                savedCustomLocation.parseLocation(lastLocation);
            }
            map.snapshot(this);
            bitmapCropManager.cropBitmap(savedCustomLocation);
            bundle = new Bundle();
            bundle.putSerializable("Location", savedCustomLocation);
            bundle.putString("DISPLAY_MODE", "WRITE");
            intent.putExtras(bundle);

            startActivity(intent);
            finish();
        }catch (Exception e){

        }

    }

    // getMapAsync of Callback
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                move_ItemSetting_Activity();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    // OnMapReadyCallback Interface
    @Override
    public void onMapReady(GoogleMap map) {
        if(isDebug){
            Log.d(TAG, "onMapReady");
        }
        this.map = map;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try{
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if(lastLocation != null){
                refreshMapView();
            }
            if(isDebug){
                Log.d(TAG, "onConnected");
            }
        }catch (SecurityException e){

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapLoaded() {
        Log.d(TAG, "onMapLoaded");
    }

    @Override
    public void onSnapshotReady(Bitmap bitmap) {
        if(isDebug){
            Log.d(TAG, "Create to "+ savedCustomLocation.objFilePath + savedCustomLocation.imgFileName);
        }
        try {
            FileOutputStream out = new FileOutputStream(savedCustomLocation.objFilePath + savedCustomLocation.imgFileName);
            //FileOutputStream out = new FileOutputStream("/sdcard/"+imgFileName+".png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "FileNotFoundException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(TAG, "IOException");
            e.printStackTrace();
        }
    }
}
