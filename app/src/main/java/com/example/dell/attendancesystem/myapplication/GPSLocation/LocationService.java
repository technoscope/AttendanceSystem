package com.example.dell.attendancesystem.myapplication.GPSLocation;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

import androidx.core.app.ActivityCompat;

import static com.example.dell.attendancesystem.myapplication.UI.MainActivity.isInTheZone;

public class LocationService extends Service {
    LatLng destinationStation;
    TextToSpeech tts;
    LocationModel locationModel;

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        startService(intent);
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationModel = new LocationModel();
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.getDefault());
                }

            }
        });


        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                float distance = distanceFormula(location.getLatitude(), location.getLongitude(), locationModel.getLatitude(), locationModel.getLongitide());
                if (distance <= locationModel.getRadius()) {
                    isInTheZone = true;
                } else {
                    isInTheZone = false;
                }
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                // TODO Auto-generated method stub
            }
        });
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                float distance = distanceFormula(location.getLatitude(), location.getLongitude(), locationModel.getLatitude(), locationModel.getLongitide());
                if (distance <= locationModel.getRadius()) {
                    isInTheZone = true;
                } else {
                    isInTheZone = false;
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });

    }

    public float distanceFormula(double curlat1, double curlng1, double dislat2, double dislng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(dislat2 - curlat1);
        double dLng = Math.toRadians(dislng2 - curlng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(curlat1)) * Math.cos(Math.toRadians(dislat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        int meterConversion = 1609;

        Float float1 = new Float(dist * meterConversion).floatValue();
        return float1;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent serviceIntent = new Intent(getApplicationContext(), LocationService.class);
        stopService(serviceIntent);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Bundle bundle = intent.getExtras();
        locationModel.setLatitude(bundle.getDouble("lat"));
        locationModel.setLongitide(bundle.getDouble("lan"));
        locationModel.setRadius(bundle.getDouble("radius"));

    }
}
