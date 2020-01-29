package com.example.dell.attendancesystem.myapplication.AdminRole.EntireReports;
import androidx.appcompat.app.AppCompatActivity;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dell.attendancesystem.R;
import com.example.dell.attendancesystem.myapplication.AdminRole.MainAdminActivity.AdminRole;
import com.example.dell.attendancesystem.myapplication.SQLiteDatabaseHelper.DatabaseHelper;
import com.google.android.material.chip.Chip;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class EntireReport extends AppCompatActivity implements View.OnClickListener {
    TextView totalemp;
    ImageView pressbutton;
    PieChartView pieChartView;
    List<SliceValue> pieData;
    PieChartData pieChartData;
    Chip chiptoday, chipweekly, chipmonthly, chipall;
    ArrayList<String> Status;
    int presentcounter = 0, absentcounter = 0, onleavecounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entire_rreport);
        pieChartView = findViewById(R.id.piechart_id);
        pressbutton=findViewById(R.id.obtn_id);
        pressbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EntireReport.this, AdminRole.class));
            }
        });
        pieData = new ArrayList<>();
        Status = new ArrayList<>();
        chiptoday = findViewById(R.id.todaychip);
        chipweekly = findViewById(R.id.weeklychip);
        chipmonthly = findViewById(R.id.monthlychip);
        chipall = findViewById(R.id.allchip);
        chiptoday.setOnClickListener(this);
        chipweekly.setOnClickListener(this);
        chipmonthly.setOnClickListener(this);
        chipall.setOnClickListener(this);
        totalemp = findViewById(R.id.totalemp_iid);
        int totalemployee = 0;
        ////////////////////////////////////
        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor2 = db.GetEmplooyeeRecord();
        if (cursor2 != null) {
            if (cursor2.moveToNext()) {
                do {
                    totalemployee++;
                }
                while (cursor2.moveToNext());

                cursor2.close();
                totalemp.setText(totalemployee + "");
            }
        }
        Cursor cursor = db.GetAttendanceRecord();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    Status.add(cursor.getString(5));
                    if (Status.contains("present")) {
                        presentcounter++;
                    }
                    if (Status.contains("absent")) {
                        absentcounter++;
                    }
                    if (Status.contains("leave")) {
                        onleavecounter++;
                    }
                }
                while (cursor.moveToNext());
                cursor.close();


            } else {
                // title.setText("No Record is Found");
            }
        } else {
            Toast.makeText(this, "cursor Null", Toast.LENGTH_SHORT).show();
        }

        pieData.add(new SliceValue(presentcounter, Color.GREEN).setLabel("Present\n" + presentcounter));
        pieData.add(new SliceValue(absentcounter, Color.RED).setLabel("Absent\n" + absentcounter));
        pieData.add(new SliceValue(onleavecounter, Color.YELLOW).setLabel("Leave\n" + onleavecounter));
        pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
       // pieChartData.setHasCenterCircle(true).setCenterText1("All\nReport").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
        pieChartView.setPieChartData(pieChartData);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == chiptoday.getId()) {
            String todaydate = TodayDate();
            String ctxttitle = chiptoday.getText().toString();
            pieData.clear();
            GetTodayReport(todaydate, ctxttitle);
        } else if (id == chipweekly.getId()) {
            String ctxttitle = chipweekly.getText().toString();
            String todaydate = TodayDate();
            String lastweekdate = LastWeekDayDate();
            pieData.clear();
            GetWeeklyReport(lastweekdate, todaydate, ctxttitle);

        } else if (id == chipmonthly.getId()) {
            String todaydate = TodayDate();
            String lastmonthdate = LastMonthDayDate();
            String ctxttitle = chipmonthly.getText().toString();
            pieData.clear();
            GetMonthlyReport(lastmonthdate, todaydate, ctxttitle);
        } else if (id == chipall.getId()) {
            GetAllReport();

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

    public void GetAllReport() {
        int presentcounter = 43;
        int absentcounter = 4;
        int onleavecounter = 7;

        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.GetAttendanceRecord();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Status.add(cursor.getString(5));
                    if (Status.contains("present")) {
                        presentcounter++;
                    }
                    if (Status.contains("absent")) {
                        absentcounter++;
                    }
                    if (Status.contains("leave")) {
                        onleavecounter++;
                    }
                }
                while (cursor.moveToNext());
                cursor.close();


            } else {
                // title.setText("No Record is Found");
            }
        } else {
            Toast.makeText(this, "cursor Null", Toast.LENGTH_SHORT).show();
        }
        pieData.clear();
        pieData.add(new SliceValue(presentcounter, Color.GREEN).setLabel("Present\n" + presentcounter));
        pieData.add(new SliceValue(absentcounter, Color.RED).setLabel("Absent\n" + absentcounter));
        pieData.add(new SliceValue(onleavecounter, Color.YELLOW).setLabel("Leave\n" + onleavecounter));
        pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
       // pieChartData.setHasCenterCircle(true).setCenterText1("All\nReport").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
        pieChartView.setPieChartData(pieChartData);
    }

    public void GetTodayReport(String todaydate, String ctxttitle) {
        int presentcounter = 43;
        int absentcounter = 55;
        int onleavecounter = 7;

        DatabaseHelper db = new DatabaseHelper(this);
            Cursor cursor = db.GetAttendanceRecordByOnlyTodayDate(todaydate);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        Status.add(cursor.getString(5));
                        if (Status.contains("present")) {
                            presentcounter++;
                        }
                        if (Status.contains("absent")) {
                            absentcounter++;
                        }
                        if (Status.contains("leave")) {
                            onleavecounter++;
                        }
                    }
                    while (cursor.moveToNext());
                    cursor.close();


                } else {
                    // title.setText("No Record is Found");
                }
        } else {
            Toast.makeText(this, "cursor Null", Toast.LENGTH_SHORT).show();
        }
        pieData.clear();
        pieData.add(new SliceValue(presentcounter, Color.GREEN).setLabel("Present\n" + presentcounter));
        pieData.add(new SliceValue(absentcounter, Color.RED).setLabel("Absent\n" + absentcounter));
        pieData.add(new SliceValue(onleavecounter, Color.YELLOW).setLabel("Leave\n" + onleavecounter));
        pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
       // pieChartData.setHasCenterCircle(true).setCenterText1(ctxttitle + "\nReport").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
        pieChartView.setPieChartData(pieChartData);

    }

    public void GetWeeklyReport(String lastweekdate, String todaydate, String ctxttitle) {
        int presentcounter = 65;
        int absentcounter = 22;
        int onleavecounter = 50;
        Status = new ArrayList<>();
        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.GetAttendanceRecordBetweenTwoDate(lastweekdate, todaydate);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Status.add(cursor.getString(5));
                    if (Status.contains("present")) {
                        presentcounter++;
                    }
                    if (Status.contains("absent")) {
                        absentcounter++;
                    }
                    if (Status.contains("leave")) {
                        onleavecounter++;
                    }
                }
                while (cursor.moveToNext());
                cursor.close();
                pieData.clear();
                pieData.add(new SliceValue(presentcounter, Color.GREEN).setLabel("Present\n" + presentcounter));
                pieData.add(new SliceValue(absentcounter, Color.RED).setLabel("Absent\n" + absentcounter));
                pieData.add(new SliceValue(onleavecounter, Color.YELLOW).setLabel("Leave\n" + onleavecounter));
                pieChartData = new PieChartData(pieData);
                pieChartData.setHasLabels(true).setValueLabelTextSize(14);
               // pieChartData.setHasCenterCircle(true).setCenterText1(ctxttitle + "\nReport").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
                pieChartView.setPieChartData(pieChartData);

            }
        } else {
            Toast.makeText(this, "Cursor Null", Toast.LENGTH_SHORT).show();
        }
    }

    public void GetMonthlyReport(String lastmonthdate, String todaydate, String ctxttitle) {
        int presentcounter = 0;
        int absentcounter = 0;
        int onleavecounter = 0;
        Status = new ArrayList<>();
        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.GetAttendanceRecordBetweenTwoDate(lastmonthdate, todaydate);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Status.add(cursor.getString(5));
                    if (Status.contains("present")) {
                        presentcounter++;
                    }
                    if (Status.contains("absent")) {
                        absentcounter++;
                    }
                    if (Status.contains("leave")) {
                        onleavecounter++;
                    }
                }
                while (cursor.moveToNext());
                cursor.close();
                pieData.clear();
                pieData.add(new SliceValue(presentcounter, Color.GREEN).setLabel("Present\n" + presentcounter));
                pieData.add(new SliceValue(absentcounter, Color.RED).setLabel("Absent\n" + absentcounter));
                pieData.add(new SliceValue(onleavecounter, Color.YELLOW).setLabel("Leave\n" + onleavecounter));
                pieChartData = new PieChartData(pieData);
                pieChartData.setHasLabels(true).setValueLabelTextSize(14);
             //   pieChartData.setHasCenterCircle(true).setCenterText1(ctxttitle + "\nReport").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
                pieChartView.setPieChartData(pieChartData);
            }
        } else {
            Toast.makeText(this, "Cursor Null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EntireReport.this, AdminRole.class));
        super.onBackPressed();
    }
}
