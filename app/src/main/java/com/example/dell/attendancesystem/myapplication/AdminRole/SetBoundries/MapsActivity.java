package com.example.dell.attendancesystem.myapplication.AdminRole.SetBoundries;

import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.attendancesystem.R;
import com.example.dell.attendancesystem.myapplication.AdminRole.MainAdminActivity.AdminRole;
import com.example.dell.attendancesystem.myapplication.GPSLocation.GPS;
import com.example.dell.attendancesystem.myapplication.Models.GeofenceRadiusModel;
import com.example.dell.attendancesystem.myapplication.SQLiteDatabaseHelper.DatabaseHelper;
import com.example.dell.attendancesystem.myapplication.UI.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SeekBar seekBar;

    GeofenceRadiusModel
    grmodel=new GeofenceRadiusModel();
    TextView pertext,setbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setbutton=findViewById(R.id.setbtn);
        final DatabaseHelper databaseHelper=new DatabaseHelper(this);
        final Cursor cursor= databaseHelper.getInstitude_Table();
        setbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog=new ProgressDialog(MapsActivity.this);
                alertDialog.setTitle("Waiting....");
                alertDialog.create();
                double a=grmodel.getRadius();
                double lat=grmodel.getLatitude();
                double lon=grmodel.getLongitude();
                if(cursor.getCount()==0) {
               alertDialog.dismiss();
                    databaseHelper.addInstituteInfo(grmodel.getRadius(), grmodel.getLatitude(), grmodel.getLongitude());
                    startActivity(new Intent(MapsActivity.this, AdminRole.class));
                }else{
                    alertDialog.dismiss();
                    databaseHelper.updateInstituteInfo(grmodel.getRadius(),grmodel.getLatitude(),grmodel.getLongitude());
                    startActivity(new Intent(MapsActivity.this, AdminRole.class));
                }
               }
        });



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        GPS gps = new GPS(this);
        Location location = gps.getlocation();
        if (location != null) {
            final double lat = location.getLatitude();
            final double lon = location.getLongitude();
            grmodel.setLatitude(lat);
            grmodel.setLongitude(lon);



            // Add a marker in Sydney and move the camera
            final LatLng sydney = new LatLng(lat, lon);
            // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            mMap.addMarker(new MarkerOptions().position(sydney).title("My Location"));

            //zoom to current location
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(lat, lon))      // Sets the center of the map to location user
                    .zoom(20)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            seekBar = findViewById(R.id.seetkbar);
            pertext = findViewById(R.id.percent_text_id);
            final Circle circle = googleMap.addCircle(new CircleOptions()
                    .center(new LatLng(lat, lon))
                    .radius(1)
                    .strokeColor(Color.YELLOW)
                    .fillColor(androidx.constraintlayout.widget.R.color.abc_btn_colored_borderless_text_material));

            circle.setCenter(sydney);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    seekBar.setMax(50);
                    pertext.setText("" + i + " m");
                    seekBar.animate();
                    circle.setRadius(i);
                    grmodel.setRadius(i);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        }else{
            Toast.makeText(this, "Location is null", Toast.LENGTH_SHORT).show();
        }
        }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MapsActivity.this, MainActivity.class));
        super.onBackPressed();
    }
}

