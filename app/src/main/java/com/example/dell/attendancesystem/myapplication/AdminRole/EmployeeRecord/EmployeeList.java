package com.example.dell.attendancesystem.myapplication.AdminRole.EmployeeRecord;

import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.dell.attendancesystem.R;
import com.example.dell.attendancesystem.myapplication.AdminRole.MainAdminActivity.AdminRole;
import com.example.dell.attendancesystem.myapplication.SQLiteDatabaseHelper.DatabaseHelper;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EmployeeList extends AppCompatActivity {
    ArrayList<String> EmployeePID;
    ArrayList<String> Employeename;
    ArrayList<String> EmployeeFname;
    ArrayList<String> EmployeeDOB;
    ArrayList<String> EmployeeScale;
    ArrayList<String> EmployeeAddress;
    ArrayList<String> JoiningDate;
    ArrayList<String> EmployeeCNIC;
    ArrayList<String> Designation;
    ArrayList<String> CurrentDepartment;
    ArrayList<String> Gender;
    ArrayList<String> Emailaddress;
    ArrayList<String> Phoneno;
    private ArrayList<String> province;
    private ArrayList<String> village;
    private ArrayList<String> district;
    private ArrayList<String> tehsil;
    private ArrayList<String> uniocouncil;
    EmployeeListAdapter populateAdabter;
    RecyclerView recyclerView;
    ImageView bckbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);
        bckbtn = findViewById(R.id.actvity_emplist_bck_btn_id);
        bckbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmployeeList.this, AdminRole.class));
            }
        });
        recyclerView = findViewById(R.id.recycle_view_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        EmployeePID = new ArrayList<>();
        Employeename = new ArrayList<>();
        EmployeeFname=new ArrayList<>();
        EmployeeDOB=new ArrayList<>();
        EmployeeCNIC = new ArrayList<>();
        EmployeeScale=new ArrayList<>();
        EmployeeAddress=new ArrayList<>();
        JoiningDate=new ArrayList<>();
        Designation = new ArrayList<>();
        province=new ArrayList<>();
        village=new ArrayList<>();
        district=new ArrayList<>();
        tehsil=new ArrayList<>();
        uniocouncil=new ArrayList<>();
        CurrentDepartment = new ArrayList<>();
        Gender = new ArrayList<>();
        Emailaddress = new ArrayList<>();
        Phoneno = new ArrayList<>();
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor cursor = databaseHelper.GetEmployeeRecord();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Employee Record is Empty", Toast.LENGTH_SHORT).show();
        } else {
            if (cursor.moveToFirst()) {
                do {
                    EmployeePID.add(cursor.getString(1));
                    EmployeeFname.add(cursor.getString(2));
                    Employeename.add(cursor.getString(3));
                    Gender.add(cursor.getString(4));
                    EmployeeCNIC.add(cursor.getString(5));
                    EmployeeDOB.add(cursor.getString(6));
                    Designation.add(cursor.getString(7));
                    EmployeeScale.add(cursor.getString(8));
                    Emailaddress.add(cursor.getString(9));
                    Phoneno.add(cursor.getString(10));
                    EmployeeAddress.add(cursor.getString(11));
                    JoiningDate.add(cursor.getString(12));
                    CurrentDepartment.add(cursor.getString(13));
                    province.add(cursor.getString(14));
                    district.add(cursor.getString(15));
                    tehsil.add(cursor.getString(16));
                    uniocouncil.add(cursor.getString(17));
                    village.add(cursor.getString(18));


                    populateAdabter = new EmployeeListAdapter(EmployeePID, Employeename, EmployeeCNIC, Designation, CurrentDepartment, Gender,
                                          Emailaddress, Phoneno,province,village,district,tehsil,uniocouncil,this,EmployeeFname,EmployeeDOB,EmployeeScale,EmployeeAddress,JoiningDate);
                    recyclerView.setAdapter(populateAdabter);
                   }
                while (cursor.moveToNext());
            }
            cursor.close();
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EmployeeList.this, AdminRole.class));
        super.onBackPressed();
    }
}
