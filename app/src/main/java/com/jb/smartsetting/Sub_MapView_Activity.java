package com.jb.smartsetting;

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
import com.jb.smartsetting.GPS_Utility.GPS_Manager;
import com.jb.smartsetting.GPS_Utility.Stub_Location_Object;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;


/**
 * Created by jeongbin.son on 2017-06-19.
 */

public class Sub_MapView_Activity extends AppCompatActivity implements View.OnClickListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GPS_Manager.GPS_Network_Callback,
        GoogleMap.OnMapLoadedCallback,
        LocationListener {

    private Bundle bundle;

    private GoogleMap map;
    private Bitmap bitmap;
    private ImageView imageView;
    private GoogleMap.SnapshotReadyCallback SnapshotCallback;

    private MarkerOptions options;
    private LatLng latLng;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;

    Stub_Location_Object stubLocation;

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

        SnapshotCallback = new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                try {
                    if(isDebug){
                        Log.d(TAG, "Create to "+stubLocation.objFilePath + stubLocation.imgFileName);
                    }
                    FileOutputStream out = new FileOutputStream(stubLocation.objFilePath + stubLocation.imgFileName);
                    //FileOutputStream out = new FileOutputStream("/sdcard/"+imgFileName+".png");
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void init_GoogleApiClientBuilder(){
        googleApiClient = new GoogleApiClient.Builder(this)
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
                onRequestPermission();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void move_ItemSetting_Activity() {
        Intent intent = new Intent(this, Sub_ItemSetting_Activity.class);

        stubLocation = new Stub_Location_Object();
        if(lastLocation != null){
            stubLocation.parseLocation(lastLocation);
            map.snapshot(SnapshotCallback);
        }
        bundle = new Bundle();
        bundle.putSerializable("Location", stubLocation);

        intent.putExtras(bundle);

        startActivity(intent);
        finish();
    }

    // View.OnClickListeexner Interface
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                move_ItemSetting_Activity();
                break;
            case R.id.btn_cancel:
                move_ItemSetting_Activity();
                break;
        }
    }

    // OnMapReadyCallback Interface
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
    }

    // GPS_Manager.GPS_Network_Callback Interface
    @Override
    public void onLocationChaged(Location location) {

    }

    @Override
    public void onRequestPermission() {

    }

    @Override
    public void onPanic() {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try{
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if(lastLocation != null){
                refreshMapView();
            }
        }catch (SecurityException e){
            onRequestPermission();
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
        map.snapshot(SnapshotCallback);
        Log.d("TEST", "Take Snapshot");
    }
}
