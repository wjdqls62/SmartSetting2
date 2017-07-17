package com.jb.smartsetting.Common_Utility;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by jeongbin.son on 2017-07-13.
 */

public class ConnectedGoogleServiceCallback implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private String TAG = this.getClass().getName();


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "GoogleService onConnected.");
        Log.d(TAG, "Connected Success");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "GoogleService onConnectionSuspended");
        Log.d(TAG, "Suspended cause : "+i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "GoogleService onConnectionSuspended");
        Log.d(TAG, "Connected Failed : " + connectionResult.getErrorCode());
    }
}
