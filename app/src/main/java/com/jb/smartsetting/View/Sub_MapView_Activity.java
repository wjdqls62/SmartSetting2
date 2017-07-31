package com.jb.smartsetting.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.jb.smartsetting.GPS_Utility.SavedCustomLocation;
import com.jb.smartsetting.R;


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

    private Intent intent;
    private Bitmap bitmap;
    private SavedCustomLocation savedCustomLocation;

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

    private void init_GoogleApiClientBuilder() {
        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void onRefreshMapView() {
        options = new MarkerOptions();
        latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        options.position(latLng);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        Marker marker = map.addMarker(options);
        marker.showInfoWindow();
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        Log.d(TAG, "애니메이션 완료");
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
            map.snapshot(this);
        } catch (Exception e) {
        }
    }

    // getMapAsync of Callback
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
        if (isDebug) {
            Log.d(TAG, "onMapReady");
        }
        this.map = map;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                onRefreshMapView();
            }
            if (isDebug) {
                Log.d(TAG, "onConnected");
            }
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
        this.bitmap = Bitmap.createBitmap(bitmap);
        if (bitmap != null) {
            LoadingDialog loadingDialog = new LoadingDialog();
            loadingDialog.execute();
        } else {
            Toast.makeText(getApplicationContext(), "SnapshotReady of Bitmap is Null", Toast.LENGTH_SHORT).show();
        }
    }

    private class LoadingDialog extends AsyncTask<Bitmap, Void, Void> {
        private ProgressDialog progressDialog;
        private GoogleMap.SnapshotReadyCallback snapshotReadyCallback;
        private BitmapCropManager bitmapCropManager;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bitmapCropManager = new BitmapCropManager(getApplicationContext());
            progressDialog = new ProgressDialog(Sub_MapView_Activity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시만 기다려주세요...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Bitmap... params) {
            bitmapCropManager.cropBitmap(bitmap, savedCustomLocation);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            startActivity(intent);
            finish();
        }
    }
}
