package com.example.dell.attendancesystem.myapplication.AdminRole.MainAdminActivity;


import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.example.dell.attendancesystem.R;
import com.example.dell.attendancesystem.myapplication.AdminRole.AttendanceRecord.AttendanceRecord;
import com.example.dell.attendancesystem.myapplication.AdminRole.EmployeeRecord.EmployeeList;
import com.example.dell.attendancesystem.myapplication.AdminRole.EntireReports.EntireReport;
import com.example.dell.attendancesystem.myapplication.AdminRole.SetBoundries.MapsActivity;
import com.example.dell.attendancesystem.myapplication.AdminRole.UploadAttendance.UploadAttendanceRecord;
import com.example.dell.attendancesystem.myapplication.AdminRole.onLeave.onleaveActivity;
import com.example.dell.attendancesystem.myapplication.GPSLocation.GPS;
import com.example.dell.attendancesystem.myapplication.GPSLocation.LocationService;
import com.example.dell.attendancesystem.myapplication.Models.GeofenceRadiusModel;
import com.example.dell.attendancesystem.myapplication.SQLiteDatabaseHelper.DatabaseHelper;
import com.example.dell.attendancesystem.myapplication.UI.EmployeeRegistration1;
import com.example.dell.attendancesystem.myapplication.UI.MainActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


public class AdminRole extends AppCompatActivity implements View.OnClickListener {
    CardView EmployeeRecord, AttendanceRecordd, UpdateAttendace, SetBoundries, AddEmployee, OnLeave, reports;
    ImageView backpressbtn;
    GeofenceRadiusModel geofenceRadiusModel;// = new GeofenceRadiusModel();
    CardView mGeofenceCardView;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        geofenceRadiusModel = new GeofenceRadiusModel();
        setContentView(R.layout.activity_admin_role);
        backpressbtn = findViewById(R.id.ad_r_bck_btn_id);
        EmployeeRecord = findViewById(R.id.emp_record);
        AttendanceRecordd = findViewById(R.id.attendance_record);
        UpdateAttendace = findViewById(R.id.update_record);
        SetBoundries = findViewById(R.id.set_boundries);
        AddEmployee = findViewById(R.id.add_employee);
        OnLeave = findViewById(R.id.on_Leave_id);
        reports = findViewById(R.id.reports_card);
        EmployeeRecord.setOnClickListener(this);
        AttendanceRecordd.setOnClickListener(this);
        UpdateAttendace.setOnClickListener(this);
        SetBoundries.setOnClickListener(this);
        AddEmployee.setOnClickListener(this);
        OnLeave.setOnClickListener(this);
        backpressbtn.setOnClickListener(this);
        reports.setOnClickListener(this);
        mGeofenceCardView = findViewById(R.id.geofencecard_id1);
        mGeofenceCardView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == EmployeeRecord.getId()) {
            Intent EmployeeRecord = new Intent(AdminRole.this, EmployeeList.class);
            startActivity(EmployeeRecord);
        } else if (id == AttendanceRecordd.getId()) {
            Intent homeToAttendenceRecord = new Intent(this, AttendanceRecord.class);
            startActivity(homeToAttendenceRecord);

        } else if (id == UpdateAttendace.getId()) {
            startActivity(new Intent(AdminRole.this, UploadAttendanceRecord.class));


        } else if (id == SetBoundries.getId()) {

            Intent setboundries = new Intent(AdminRole.this, MapsActivity.class);
            startActivity(setboundries);

        } else if (id == AddEmployee.getId()) {
            Intent addEmployee = new Intent(AdminRole.this, EmployeeRegistration1.class);
            startActivity(addEmployee);
        } else if (id == OnLeave.getId()) {
            Intent onleaveintent = new Intent(AdminRole.this, onleaveActivity.class);
            startActivity(onleaveintent);
        } else if (id == backpressbtn.getId()) {
            startActivity(new Intent(AdminRole.this, MainActivity.class));
        } else if (id == reports.getId()) {
            startActivity(new Intent(AdminRole.this, EntireReport.class));

        }else if(id==mGeofenceCardView.getId()){
            databaseHelper = new DatabaseHelper(this);
            Cursor cur = databaseHelper.getInstitude_Table();
            if (cur.moveToFirst()) {

                int radius = cur.getInt(1);
                double lat = cur.getDouble(2);
                double lon = cur.getDouble(3);
                geofenceRadiusModel.setRadius(radius);
                geofenceRadiusModel.setLatitude(lat);
                geofenceRadiusModel.setLongitude(lon);
            }

            Intent serviceIntent = new Intent(this, LocationService.class);
            serviceIntent.putExtra("lat", geofenceRadiusModel.getLatitude());
            serviceIntent.putExtra("lan",geofenceRadiusModel.getLongitude());
            serviceIntent.putExtra("radius",geofenceRadiusModel.getRadius());
            startService(serviceIntent);



        }

    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AdminRole.this, MainActivity.class));
        super.onBackPressed();
    }
}
