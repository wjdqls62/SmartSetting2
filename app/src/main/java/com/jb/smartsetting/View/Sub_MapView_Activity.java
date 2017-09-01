package com.jb.smartsetting.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jb.smartsetting.Common_Utility.BitmapCropManager;
import com.jb.smartsetting.Common_Utility.SettingValues;
import com.jb.smartsetting.GPS_Utility.CustomLocation;
import com.jb.smartsetting.R;

import java.util.concurrent.ExecutionException;


/**
 * Created by jeongbin.son on 2017-06-19.
 */

public class Sub_MapView_Activity extends AppCompatActivity implements View.OnClickListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMapLoadedCallback,
        GoogleMap.SnapshotReadyCallback,
        com.google.android.gms.location.LocationListener
{

    private Bundle bundle;

    private GoogleMap map;

    private MarkerOptions markerOptions;
    private CircleOptions circleOptions;
    private LatLng latLng;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private CreateSnapShot loadingDialog;

    private Intent intent;
    private Bitmap bitmap;
    private CustomLocation savedCustomLocation;

    private Button btn_ok, btn_cancel;


    private boolean isDebug = true;
    private String TAG = getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_map_view);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapview);

        init_GoogleApiClientBuilder();
        mapFragment.getMapAsync(this);
        init_View();

        savedCustomLocation = new CustomLocation();


    }

    private void init_View(){
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_ok = (Button) findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    private void init_GoogleApiClientBuilder() {
        try{
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .enableAutoManage(this, this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            googleApiClient.connect();

            locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        }catch(SecurityException e){

        }
    }

    private void onRefreshMapView() {
        if (map != null) {
            map.clear();
        }

        if(lastLocation != null){
            markerOptions = new MarkerOptions();
            circleOptions = new CircleOptions();
            latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            circleOptions.center(latLng).radius(300).strokeWidth(1f).fillColor(Color.parseColor("#40C6FFFF"));

            Marker marker = map.addMarker(markerOptions);
            marker.showInfoWindow();
            map.addCircle(circleOptions);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        }
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
            try {
                lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                if (lastLocation != null) {
                    onRefreshMapView();
                }
            } catch (SecurityException e) {

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void moveItemSettingActivity() {
        try {
            intent = new Intent(this, Sub_ItemSetting_Activity.class);
            bundle = new Bundle();
            if (lastLocation != null) {
                savedCustomLocation.parseLocation(lastLocation);
            }
            bundle.putSerializable("Location", savedCustomLocation);
            bundle.putString("DISPLAY_MODE", "WRITE");
            intent.putExtras(bundle);
            map.clear();
            map.snapshot(this);
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                moveItemSettingActivity();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    // OnMapReadyCallback Interface
    @Override
    public void onMapReady(GoogleMap map) {
        if (SettingValues.getInstance().IsDebug()) {Log.d(TAG, "onMapReady");}
        this.map = map;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (SettingValues.getInstance().IsDebug()) {Log.d(TAG, "onConnected");}

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } catch (SecurityException e) {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLoaded() {
        if (SettingValues.getInstance().IsDebug()) Log.d(TAG, "onMapLoaded");
    }

    @Override
    public void onSnapshotReady(Bitmap bitmap) {
        this.bitmap = bitmap;
        if (bitmap != null) {
            loadingDialog = new CreateSnapShot();
            loadingDialog.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            Toast.makeText(getApplicationContext(), "SnapshotReady of Bitmap is Null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (SettingValues.getInstance().IsDebug()) {Log.d(TAG, "onLocationChanged");}
        lastLocation = location;
        onRefreshMapView();
    }


    private class CreateSnapShot extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog sProgressDialog;
        private BitmapCropManager bitmapCropManager;
        private Bitmap tempBitmap;

        @Override
        protected void onPreExecute() {
            bitmapCropManager = new BitmapCropManager(getApplicationContext());
            sProgressDialog = new ProgressDialog(Sub_MapView_Activity.this);
            sProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            sProgressDialog.setMessage("잠시만 기다려주세요...");
            sProgressDialog.setCancelable(false);
            sProgressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            savedCustomLocation.setAddress(getApplication());
            tempBitmap = bitmapCropManager.cropBitmap(bitmap, savedCustomLocation);
            if(tempBitmap != null){
                return true;
            }else{
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            sProgressDialog.dismiss();
            if(aBoolean){
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "기등록된 위치가 있습니다\n"+"위치이동 후 재시도 하세요", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
