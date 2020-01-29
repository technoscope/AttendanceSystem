package com.example.dell.attendancesystem.myapplication.UI;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Toast;

import com.example.dell.attendancesystem.R;
import com.example.dell.attendancesystem.myapplication.AdminRole.MainAdminActivity.Admin;
import com.example.dell.attendancesystem.myapplication.BiometricStuff.GetAttendanceActivity;
import com.example.dell.attendancesystem.myapplication.GPSLocation.GPS;
import com.example.dell.attendancesystem.myapplication.GPSLocation.LocationService;
import com.example.dell.attendancesystem.myapplication.Models.GeofenceRadiusModel;
import com.example.dell.attendancesystem.myapplication.SQLiteDatabaseHelper.DatabaseHelper;
import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

import androidx.cardview.widget.CardView;

public class MainActivity extends Activity implements View.OnClickListener {
    GeofenceRadiusModel geofenceRadiusModel = new GeofenceRadiusModel();
    private static final String TAG = "SecuGen USB";
    public static boolean isInTheZone = false;
    CardView mRegView;
    CardView mVerifyEmpView;
    CardView mAdmin, Exit;
    GPS gpsTracker;
    Location location1;
    LocationManager lm;
    TextToSpeech t1;
    DatabaseHelper databaseHelper;


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Exit = findViewById(R.id.exit);
        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mVerifyEmpView = (CardView) findViewById(R.id.verify_me);
        mVerifyEmpView.setOnClickListener(this);
        mAdmin = findViewById(R.id.id_admin_cardview);
        mAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Adminintent = new Intent(MainActivity.this, Admin.class);
                startActivity(Adminintent);

            }
        });
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.getDefault());
                }
            }
        });
        databaseHelper = new DatabaseHelper(this);
        Cursor cur = databaseHelper.getInstitude_Table();

        if (cur.moveToFirst()) {

            double radius = cur.getDouble(1);
            double lat = cur.getDouble(2);
            double lon = cur.getDouble(3);
            geofenceRadiusModel.setRadius(radius);
            geofenceRadiusModel.setLatitude(lat);
            geofenceRadiusModel.setLongitude(lon);
        }

        Intent serviceIntent = new Intent(this, LocationService.class);
        serviceIntent.putExtra("lat", geofenceRadiusModel.getLatitude());
        serviceIntent.putExtra("lan", geofenceRadiusModel.getLongitude());
        serviceIntent.putExtra("radius", geofenceRadiusModel.getRadius());
        startService(serviceIntent);


    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void onClick(View v) {
        int id = v.getId();


        if (id == mVerifyEmpView.getId()) {


            if (isInTheZone == true) {
                String toSpeak = "You are successfully Entered in the Zone";
                Toast.makeText(this, toSpeak, Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                Intent verifyintent = new Intent(this, GetAttendanceActivity.class);
                startActivity(verifyintent);
            } else {
                String toSpeak = "Please Enter in a Zone";
                //  Toast.makeText(this, toSpeak, Toast.LENGTH_SHORT).show();

                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                // Toast.makeText(this, "You are not in the zone OR Create Geofence First", Toast.LENGTH_SHORT).show();
            }


        }

    }


}
