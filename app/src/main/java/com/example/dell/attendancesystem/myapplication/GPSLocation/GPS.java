package com.example.dell.attendancesystem.myapplication.GPSLocation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class GPS implements LocationListener {
    Context contextl;
    Location location1;




    public GPS(Context c) {

        contextl = c;
    }

    public Location getlocation() {
        if (ContextCompat.checkSelfPermission(contextl, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contextl, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(contextl, "Permission not Granted", Toast.LENGTH_SHORT).show();
            return null;
        }
        LocationManager lm = (LocationManager) contextl.getSystemService(contextl.LOCATION_SERVICE);
        boolean isgpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isnetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isnetworkEnabled) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
            location1 = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            return location1;
        }
       else if (isgpsEnabled) {


            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
            location1 = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            return location1;
        } else {
            Toast.makeText(contextl, " Please Enable GPS", Toast.LENGTH_SHORT).show();
        }
        return null;
    }


    @Override
    public void onLocationChanged(Location location) {
                location1=location;
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
}
