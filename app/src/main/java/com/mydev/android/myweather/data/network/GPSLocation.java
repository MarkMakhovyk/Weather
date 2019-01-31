package com.mydev.android.myweather.data.network;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class GPSLocation {
    private static final int REQUEST_LOCATION_PERMISSIONS = 0;
    private static final String TAG = "GPS";
    private static final int SERVICE_DISABLE = -1;
    private MyLocation myLocation;
    private GoogleApiClient mClient;
    private Context context;
    private static final String[] LOCATION_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };

    public GPSLocation(Context context, final MyLocation myLocation) {
        this.myLocation = myLocation;
        this.context = context;
    }

    public void addClient() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int errorCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (errorCode != ConnectionResult.SUCCESS) {
            Log.e(TAG, "addClient: service not found ");
            return;
        }

        mClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        if (hasLocationPermission()) {
                            Log.e(TAG, "onConnected: ");
                            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                            boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                            if (!enabled) {
                                context.startActivity(new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                getGPS();
                            }
                        } else {
                            myLocation.permissions(LOCATION_PERMISSIONS, REQUEST_LOCATION_PERMISSIONS);
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                    }
                })
                .build();
        mClient.connect();


    }

    public boolean hasLocationPermission() {
        Log.e(TAG, "hasLocationPermission: ");
        int result = ContextCompat
                .checkSelfPermission(context, LOCATION_PERMISSIONS[0]);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void getGPS() {

        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(100);
        request.setInterval(0);

        LocationServices.FusedLocationApi
                .requestLocationUpdates(mClient, request, new com.google.android.gms.location.LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        myLocation.returnLocation(location);
                        mClient.disconnect();
                    }
                });

    }

    public interface MyLocation {
        public void returnLocation(Location location);

        public void permissions(String[] permissions, int requestCode);
    }


}