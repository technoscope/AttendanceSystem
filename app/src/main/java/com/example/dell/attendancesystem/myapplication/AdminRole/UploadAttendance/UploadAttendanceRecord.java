package com.example.dell.attendancesystem.myapplication.AdminRole.UploadAttendance;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dell.attendancesystem.R;
import com.example.dell.attendancesystem.myapplication.AdminRole.AttendanceRecord.AttendanceModel;
import com.example.dell.attendancesystem.myapplication.AdminRole.MainAdminActivity.AdminRole;
import com.example.dell.attendancesystem.myapplication.AdminRole.UploadAttendance.Services.CallSoapUpload;
import com.example.dell.attendancesystem.myapplication.SQLiteDatabaseHelper.DatabaseHelper;
import com.google.android.material.picker.MaterialStyledDatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class UploadAttendanceRecord extends AppCompatActivity implements View.OnClickListener {
    CardView today, lastweek, lastmonth, all, manually;
    AttendanceModel attendancemodel;
    ImageView backpressbtn;
    ArrayList<String> EmployeePID;
    ArrayList<String> TimeIn;
    ArrayList<String> TimeOut;
    ArrayList<String> Date;
    ArrayList<String> Status;
    MaterialStyledDatePickerDialog.OnDateSetListener mDatSetListener;
   // DatePickerDialog.OnDateSetListener mDatSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_attendance_record);
        EmployeePID = new ArrayList<>();
        TimeIn = new ArrayList<>();
        TimeOut = new ArrayList<>();
        Date = new ArrayList<>();
        Status = new ArrayList<>();
        backpressbtn = findViewById(R.id.ubck_btn_id);
        today = findViewById(R.id.today_record_id);
        lastweek = findViewById(R.id.last_7_days_id);
        lastmonth = findViewById(R.id.last_30_days_id);
        all = findViewById(R.id.all_record_cardview);
        manually = findViewById(R.id.select_manually_id);
        all.setOnClickListener(this);
        backpressbtn.setOnClickListener(this);
        today.setOnClickListener(this);
        lastweek.setOnClickListener(this);
        lastmonth.setOnClickListener(this);
        manually.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.today_record_id) {
            String todaydate = TodayDate();
            DatabaseHelper db = new DatabaseHelper(this);
            Cursor cursor = db.GetAttendanceRecordByOnlyTodayDate(todaydate);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        EmployeePID.add(cursor.getString(1));
                        TimeIn.add(cursor.getString(2));
                        TimeOut.add(cursor.getString(3));
                        Date.add(cursor.getString(4));
                        Status.add(cursor.getString(5));
                    }
                    while (cursor.moveToNext());
                    cursor.close();



                  //  CallAPIs(EmployeePID.get(0),TimeIn.get(0),TimeOut.get(0),Date.get(0),Status.get(0));

                }
            } else {
                Toast.makeText(this, "cursor Null", Toast.LENGTH_SHORT).show();
            }

        } else if (id == lastweek.getId()) {
            String todaydate = TodayDate();
            String lastweekdate = LastWeekDayDate();
            DatabaseHelper db = new DatabaseHelper(this);
            Cursor cursor = db.GetAttendanceRecordBetweenTwoDate(lastweekdate, todaydate);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        EmployeePID.add(cursor.getString(1));
                        TimeIn.add(cursor.getString(2));
                        TimeOut.add(cursor.getString(3));
                        Date.add(cursor.getString(4));
                        Status.add(cursor.getString(5));
                    }
                    while (cursor.moveToNext());
                    cursor.close();
                    attendancemodel=new AttendanceModel(EmployeePID.get(0),Status.get(0),TimeIn.get(0),TimeOut.get(0),Date.get(0));

                    new AsyncCallSoap().execute();
                    //CallAPIs(EmployeePID,TimeIn,TimeOut,Date,Status);

                }
            }

        } else if (id == lastmonth.getId()) {
            String todaydate = TodayDate();
            String lastmonthdate = LastMonthDayDate();
            DatabaseHelper db = new DatabaseHelper(this);
            Cursor cursor = db.GetAttendanceRecordBetweenTwoDate(lastmonthdate, todaydate);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        EmployeePID.add(cursor.getString(1));
                        TimeIn.add(cursor.getString(2));
                        TimeOut.add(cursor.getString(3));
                        Date.add(cursor.getString(4));
                        Status.add(cursor.getString(5));

                    }
                    while (cursor.moveToNext());
                    //CallAPIs(EmployeePID,TimeIn,TimeOut,Date,Status);

                }
            }
        } else if (id == all.getId()) {
            Toast.makeText(UploadAttendanceRecord.this, "Hiiiiii", Toast.LENGTH_SHORT).show();
            DatabaseHelper db = new DatabaseHelper(this);
            Cursor cursor = db.GetAttendanceRecord();
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        EmployeePID.add(cursor.getString(1));
                        TimeIn.add(cursor.getString(2));
                        TimeOut.add(cursor.getString(3));
                        Date.add(cursor.getString(4));
                        Status.add(cursor.getString(5));
                    }
                    while (cursor.moveToNext());
                    cursor.close();
                    //CallAPIs(EmployeePID,TimeIn,TimeOut,Date,Status);


                }
            }
        } else if (id == manually.getId()) {
            DataModel dataModel1 = new DataModel();
            mDatSetListener = new MaterialStyledDatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker datePicker, int Year, int Month, int Day) {
                    Month = Month + 1;
                    String Datee = Year + "/" + Month + "/" + Day;
                    dataModel1.setDate(Datee);
                    DatabaseHelper db = new DatabaseHelper(UploadAttendanceRecord.this);

                    Cursor cursor = db.GetAttendanceRecordByOnlyTodayDate(dataModel1.getDate());
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            do {
                                EmployeePID.add(cursor.getString(1));
                                TimeIn.add(cursor.getString(2));
                                TimeOut.add(cursor.getString(3));
                                Date.add(cursor.getString(4));
                                Status.add(cursor.getString(5));
                            }
                            while (cursor.moveToNext());
                            cursor.close();

                            //CallAPIs(EmployeePID,TimeIn,TimeOut,Date,Status);

                        }
                    } else {
                        Toast.makeText(UploadAttendanceRecord.this, "cursor Null", Toast.LENGTH_SHORT).show();
                    }

                }
            };
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            int year = calendar.get(java.util.Calendar.YEAR);
            int month = calendar.get(java.util.Calendar.MONTH);
            int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(UploadAttendanceRecord.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDatSetListener, year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();


        } else if (id == backpressbtn.getId()) {
            startActivity(new Intent(UploadAttendanceRecord.this, AdminRole.class));
        }
    }

    public String TodayDate() {
        final SimpleDateFormat dateformat = new SimpleDateFormat("yyyy.MM.dd");
        final String currentDate = dateformat.format(new Date());
        final String todaydate = currentDate;
        return todaydate;
    }

    public String LastWeekDayDate() {
        final SimpleDateFormat dateformat = new SimpleDateFormat("yyyy.MM.dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        // substract 7 days
        // If we give 7 there it will give 8 days back
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 6);
        // convert to dateformat
        Date PreviousWeekDate = cal.getTime();
        String lastweekdate = dateformat.format(PreviousWeekDate);
        return lastweekdate;
    }

    public String LastMonthDayDate() {
        final SimpleDateFormat dateformat = new SimpleDateFormat("yyyy.MM.dd");
        final String currentDate = dateformat.format(new Date());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        // substract 7 days
        // If we give 7 there it will give 8 days back
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 29);
        // convert to dateformat
        Date PreviousMonthDate = cal.getTime();
        String lastMdate = dateformat.format(PreviousMonthDate);
        return lastMdate;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(UploadAttendanceRecord.this, AdminRole.class));
        super.onBackPressed();
    }

        class AsyncCallSoap extends AsyncTask<String, Void, String> {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(UploadAttendanceRecord.this);
                progressDialog.setTitle("Uploading....");
                progressDialog.show();
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... strings) {
                CallSoapUpload callSoap = new CallSoapUpload();
                String response = callSoap.AttendanceRecord(attendancemodel.getEmpPID(), attendancemodel.getTimein(), attendancemodel.getTimeout(),attendancemodel.getDate(),attendancemodel.getStatus());
                return response;
            }

            @Override
            protected void onPostExecute(String s) {
                progressDialog.dismiss();
                Toast.makeText(UploadAttendanceRecord.this, "Response=" + s, Toast.LENGTH_SHORT).show();
                super.onPostExecute(s);
            }
        }

}
