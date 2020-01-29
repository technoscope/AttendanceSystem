package com.example.dell.attendancesystem.myapplication.AdminRole.AttendanceRecord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.attendancesystem.R;
import com.example.dell.attendancesystem.myapplication.AdminRole.MainAdminActivity.AdminRole;
import com.example.dell.attendancesystem.myapplication.AdminRole.UploadAttendance.DataModel;
import com.example.dell.attendancesystem.myapplication.SQLiteDatabaseHelper.DatabaseHelper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AttendanceRecord extends AppCompatActivity {
    RecyclerView attendencerecord;
    AttendanceRecordAdapter attendanceRecordAdapter;
    ArrayList<AttendanceModel> EmployeeRecord;
    ArrayList<Integer> presentcounter1, absentcounter1, onleavecounter1;
    BottomSheetBehavior bottomSheetBehavior;
    ImageView toggleBottomSheet, bckbtn;
    RadioGroup radioGroup;
    RadioButton allrecord, todayradio, weeklyradio, monthlyradio;
    TextView selecttextview, title;
    DatePickerDialog.OnDateSetListener mDatSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_record);
        EmployeeRecord = new ArrayList<>();
        bckbtn = findViewById(R.id.actvity_attendance_bck_btn_id);
        bckbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AttendanceRecord.this, AdminRole.class));
            }
        });
        ConstraintLayout bottomSheetLayout = findViewById(R.id.bottom_sheet);
        toggleBottomSheet = findViewById(R.id.arraow_id);
        toggleBottomSheet.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
        selecttextview = findViewById(R.id.select_manual_date_id);
        radioGroup = findViewById(R.id.radiogroup_id);
        todayradio = findViewById(R.id.toady_date_radiobtn_id);
        weeklyradio = findViewById(R.id.last_week_radiobtn_id);
        monthlyradio = findViewById(R.id.last_month_radiobtn_id);
        title = findViewById(R.id.titleofrecord_id);
        attendencerecord = findViewById(R.id.attendence_record_recycleview_id);
        allrecord = findViewById(R.id.all_radiobtn_id);
        DatabaseHelper db = new DatabaseHelper(this);
        attendencerecord.setLayoutManager(new LinearLayoutManager(this));
        presentcounter1 = new ArrayList<>();
        absentcounter1 = new ArrayList<>();
        onleavecounter1 = new ArrayList<>();

        final SimpleDateFormat dateformat = new SimpleDateFormat("yyyy.MM.dd");
        final String currentDate = dateformat.format(new Date());
        final String todaydate = currentDate;
        /////////////////////////////////////////////////////////////////////////////////
        // get Calendar instance
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        // substract 7 days
        // If we give 7 there it will give 8 days back
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 6);
        // convert to dateformat
        Date PreviousWeekDate = cal.getTime();
        String lastweekdate = dateformat.format(PreviousWeekDate);
        //for 30 days
        //subtract 30 days
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 29);
        Date PreviousMonthDate = cal.getTime();
        String lastmonthdate = dateformat.format(PreviousMonthDate);

        GetAllAttendanceRecord1();
        allrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (attendanceRecordAdapter != null) {
                    attendanceRecordAdapter.clear();
                    title.setText("All Record");
                    GetAllAttendanceRecord1();
                }
            }
        });
        todayradio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title.setText("Today Record");
                GetTodayAttendanceRecord(todaydate);
            }
        });
        weeklyradio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setText("Last week Data");
                GetWeeklyAttendanceRecord(lastweekdate, todaydate);
            }


        });
        monthlyradio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetMonthlyAttendanceRecord(lastmonthdate, todaydate);
            }
        });
        selecttextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataModel dataModel1 = new DataModel();
                mDatSetListener = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int Year, int Month, int Day) {
                        Month = Month + 1;
                        String Date = Year + "/" + Month + "/" + Day;
                        dataModel1.setDate(Date);
                        GetManuallyAttendanceRecord(dataModel1.getDate());
                    }
                };
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                int year = calendar.get(java.util.Calendar.YEAR);
                int month = calendar.get(java.util.Calendar.MONTH);
                int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AttendanceRecord.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDatSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        toggleBottomSheet.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        toggleBottomSheet.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        // set listener on button click
        toggleBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    //                  toggleBottomSheet.dispatchDrawableHotspotChanged(180,8);
                    toggleBottomSheet.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);

                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    // toggleBottomSheet.dispatchDrawableHotspotChanged(90,90);
                    toggleBottomSheet.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                }
            }
        });
    }
    public void GetAllAttendanceRecord1() {
        if(attendanceRecordAdapter!=null){
            attendanceRecordAdapter.clear();
        }
        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.DistinctAllAttendanceRec();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Cursor cursorr = db.DistinctAttendanceRecord(cursor.getString(0));
                    cursorr.moveToFirst();
                    String sdad=cursorr.getString(0);
                    Cursor cursor2 = db.GGGetEmplyeeByID(sdad);
                    cursor2.moveToFirst();
                    Cursor cur=db.EmployeeAttendanceRecord(cursor2.getString(1),"present");
                    int present=cur.getCount();
                    Cursor cur2=db.EmployeeAttendanceRecord(cursor2.getString(1),"absent");
                    int absent=cur2.getCount();
                    Cursor cur3=db.EmployeeAttendanceRecord(cursor2.getString(1),"leave");
                    int leave=cur3.getCount();
                    EmployeeRecord.add(new AttendanceModel(cursor2.getString(3), present, absent, leave));
                }
                while (cursor.moveToNext());
                cursor.close();
                attendanceRecordAdapter = new AttendanceRecordAdapter(this, EmployeeRecord);
                attendencerecord.setAdapter(attendanceRecordAdapter);
            } else {
                EmployeeRecord.add(new AttendanceModel(" No Record found",  0, 0, 0));
                attendanceRecordAdapter = new AttendanceRecordAdapter(this, EmployeeRecord);
                attendencerecord.setAdapter(attendanceRecordAdapter);
            }
        } else {
            Toast.makeText(this, "cursor Null", Toast.LENGTH_SHORT).show();
        }
    }


    public void GetTodayAttendanceRecord(String todaydate) {
        attendanceRecordAdapter.clear();
        String ttitle = todayradio.getText().toString();
        title.setText(ttitle);
        DatabaseHelper db = new DatabaseHelper(this);

        Cursor cursor = db.DistinctTodayAttendanceRec(todaydate);
        if (cursor.moveToFirst()) {
            do {
                Cursor cursorr = db.DistinctAttendanceRecord(cursor.getString(0));
                cursorr.moveToFirst();
                String sdad=cursorr.getString(0);
                Cursor cursor2 = db.GGGetEmplyeeByID(sdad);
                cursor2.moveToFirst();
                Cursor cur=db.EmployeeAttendanceRecord(cursor2.getString(1),"present");
                int present=cursorr.getCount();
                Cursor cur2=db.EmployeeAttendanceRecord(cursor2.getString(1),"absent");
                int absent=cursorr.getCount();
                Cursor cur3=db.EmployeeAttendanceRecord(cursor2.getString(1),"leave");
                int leave=cursorr.getCount();
                EmployeeRecord.add(new AttendanceModel(cursor2.getString(3), present, absent, leave));
            }

            while (cursor.moveToNext());
            cursor.close();
            attendanceRecordAdapter = new AttendanceRecordAdapter(this, EmployeeRecord);
            attendencerecord.setAdapter(attendanceRecordAdapter);
        } else {
            EmployeeRecord.add(new AttendanceModel(" No Record found", 0, 0, 0));
            attendanceRecordAdapter = new AttendanceRecordAdapter(this, EmployeeRecord);
            attendencerecord.setAdapter(attendanceRecordAdapter);

        }    }

    public void GetWeeklyAttendanceRecord(String lastweekdate, String todaydate) {
        if (attendanceRecordAdapter == null) {
           Toast.makeText(this, "Record Empty", Toast.LENGTH_SHORT).show();
        } else {
            attendanceRecordAdapter.clear();
            String ttitle = weeklyradio.getText().toString();
            title.setText(ttitle);
            DatabaseHelper db = new DatabaseHelper(this);
            Cursor cursor = db.DistinctBetweenAttendanceRec(lastweekdate,todaydate);
            if (cursor.moveToFirst()) {
                do {
                    Cursor cursorr = db.DistinctAttendanceRecord(cursor.getString(0));
                    cursorr.moveToFirst();
                    String sdad=cursorr.getString(0);
                    Cursor cursor2 = db.GGGetEmplyeeByID(sdad);
                    cursor2.moveToFirst();
                    Cursor cur=db.EmployeeAttendanceRecord(cursor2.getString(1),"present");
                    int present=cur.getCount();
                    Cursor cur2=db.EmployeeAttendanceRecord(cursor2.getString(1),"absent");
                    int absent=cur2.getCount();
                    Cursor cur3=db.EmployeeAttendanceRecord(cursor2.getString(1),"leave");
                    int leave=cur3.getCount();
                    EmployeeRecord.add(new AttendanceModel(cursor2.getString(3), present, absent, leave));
                }

                while (cursor.moveToNext());
                cursor.close();
                attendanceRecordAdapter = new AttendanceRecordAdapter(this, EmployeeRecord);
                attendencerecord.setAdapter(attendanceRecordAdapter);
            } else {
                EmployeeRecord.add(new AttendanceModel(" No Record found", 0, 0, 0));
                attendanceRecordAdapter = new AttendanceRecordAdapter(this, EmployeeRecord);
                attendencerecord.setAdapter(attendanceRecordAdapter);
            }
            }
    }
    public void GetMonthlyAttendanceRecord(String lastmonthdate, String todaydate) {
        attendanceRecordAdapter.clear();
        String ttitle = monthlyradio.getText().toString();
        title.setText(ttitle);
        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.DistinctBetweenAttendanceRec(lastmonthdate, todaydate);
        if (cursor.moveToFirst()) {
            do {
                Cursor cursorr = db.DistinctAttendanceRecord(cursor.getString(0));
                cursorr.moveToFirst();
                String sdad = cursorr.getString(0);
                Cursor cursor2 = db.GGGetEmplyeeByID(sdad);
                cursor2.moveToFirst();
                Cursor cur = db.EmployeeAttendanceRecord(cursor2.getString(1), "present");
                int present = cur.getCount();
                Cursor cur2 = db.EmployeeAttendanceRecord(cursor2.getString(1), "absent");
                int absent = cur2.getCount();
                Cursor cur3 = db.EmployeeAttendanceRecord(cursor2.getString(1), "leave");
                int leave = cur3.getCount();
                EmployeeRecord.add(new AttendanceModel(cursor2.getString(3), present, absent, leave));
            }

            while (cursor.moveToNext());
            cursor.close();
            attendanceRecordAdapter = new AttendanceRecordAdapter(this, EmployeeRecord);
            attendencerecord.setAdapter(attendanceRecordAdapter);
        } else {
            EmployeeRecord.add(new AttendanceModel(" No Record found", 0, 0, 0));
            attendanceRecordAdapter = new AttendanceRecordAdapter(this, EmployeeRecord);
            attendencerecord.setAdapter(attendanceRecordAdapter);
        }
    }



    public void GetManuallyAttendanceRecord(String SelectedDate) {
            attendanceRecordAdapter.clear();
            String ttitle = todayradio.getText().toString();
            title.setText(ttitle);
            DatabaseHelper db = new DatabaseHelper(this);

        Cursor cursor = db.DistinctTodayAttendanceRec(SelectedDate);
        if (cursor.moveToFirst()) {
            do {
                Cursor cursorr = db.DistinctAttendanceRecord(cursor.getString(0));
                cursorr.moveToFirst();
                String sdad=cursorr.getString(0);
                Cursor cursor2 = db.GGGetEmplyeeByID(sdad);
                cursor2.moveToFirst();
                Cursor cur=db.EmployeeAttendanceRecord(cursor2.getString(1),"present");
                int present=cur.getCount();
                Cursor cur2=db.EmployeeAttendanceRecord(cursor2.getString(1),"absent");
                int absent=cur2.getCount();
                Cursor cur3=db.EmployeeAttendanceRecord(cursor2.getString(1),"leave");
                int leave=cur3.getCount();
                EmployeeRecord.add(new AttendanceModel(cursor2.getString(3), present, absent, leave));
            }

            while (cursor.moveToNext());
            cursor.close();
            attendanceRecordAdapter = new AttendanceRecordAdapter(this, EmployeeRecord);
            attendencerecord.setAdapter(attendanceRecordAdapter);
        } else {
            EmployeeRecord.add(new AttendanceModel(" No Record found", 0, 0, 0));
            attendanceRecordAdapter = new AttendanceRecordAdapter(this, EmployeeRecord);
            attendencerecord.setAdapter(attendanceRecordAdapter);

        }
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(AttendanceRecord.this, AdminRole.class));
        super.onBackPressed();
    }


}
