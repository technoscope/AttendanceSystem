package com.example.dell.attendancesystem.myapplication.UI;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.dell.attendancesystem.R;
import com.example.dell.attendancesystem.myapplication.AdminRole.MainAdminActivity.AdminRole;
import com.example.dell.attendancesystem.myapplication.BiometricStuff.BiometricRegistration;

import androidx.appcompat.app.AppCompatActivity;

public class EmployeeRegistration1 extends AppCompatActivity implements View.OnClickListener {

    private ImageView mButtonNext, backpress;
    //Bio info
    private EditText EmpName, EmpCnic, fathername,dateofbirth, address, province, district, tehsil, village, unioncouncil;
    private RadioGroup radioGroup;
    private RadioButton radiobtn;
    //Social info
    private EditText EmailAddress, phonenumber;
    //Service info
    private EditText personalIId, EmpOcup, CurrentDept, joiningdate, scale;

    //stringbuffers
    String Employeename, Empfathername, EmployeeCnic, EmployeeOccupation, CurrentDeptt, personalid, EmpEmailAddress, phonno;
    String sAddress, sProvince, sDistrict, sTehsil, sVillage, sJoiningdate, sScale, sUnioncouncil;
    private String Gender,sDateofbirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_registration1);
        dateofbirth=findViewById(R.id.dob);
        address = findViewById(R.id.address);
        province = findViewById(R.id.province_id);
        district = findViewById(R.id.district_nameid);
        tehsil = findViewById(R.id.tehsil_id);
        village = findViewById(R.id.village_id);
        joiningdate = findViewById(R.id.joining_date);
        scale = findViewById(R.id.scale_id);
        unioncouncil = findViewById(R.id.unioncouncil_id);

        EmailAddress = findViewById(R.id.emailadd);
        personalIId = findViewById(R.id.pid);
        mButtonNext = findViewById(R.id.nextactivity);
        mButtonNext.setOnClickListener(this);
        phonenumber = findViewById(R.id.phoneno);
        EmpName = findViewById(R.id.employee_name);
        fathername = findViewById(R.id.employee_fname);
        EmpCnic = findViewById(R.id.employee_cnic);
        EmpOcup = findViewById(R.id.employee_occupation);
        CurrentDept = findViewById(R.id.CurrentDept);
        radioGroup = findViewById(R.id.gender_radio_group);
        backpress = findViewById(R.id.backactivity);
        backpress.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == mButtonNext.getId()) {
            sDateofbirth=dateofbirth.getText().toString();
            sAddress = address.getText().toString();
            sProvince = province.getText().toString();
            sDistrict = district.getText().toString();
            sTehsil = tehsil.getText().toString();
            sVillage = village.getText().toString();
            sUnioncouncil=unioncouncil.getText().toString();
            sScale = scale.getText().toString();
            sJoiningdate = joiningdate.getText().toString();

            radiobtn = findViewById(radioGroup.getCheckedRadioButtonId());
            if(radiobtn!=null) {
                Gender = radiobtn.getText().toString();
            }
            Employeename = EmpName.getText().toString();
            EmployeeCnic = EmpCnic.getText().toString();
            CurrentDeptt = CurrentDept.getText().toString();
            EmployeeOccupation = EmpOcup.getText().toString();
            personalid = personalIId.getText().toString();
            Empfathername = fathername.getText().toString();
            EmpEmailAddress = EmailAddress.getText().toString();
            phonno = phonenumber.getText().toString();
            Toast.makeText(this, Employeename, Toast.LENGTH_SHORT).show();
            Intent nextintent = new Intent(EmployeeRegistration1.this, BiometricRegistration.class);
            nextintent.putExtra("name", Employeename);
            nextintent.putExtra("fname",Empfathername);

            nextintent.putExtra("Cnic", EmployeeCnic);
            nextintent.putExtra("Occupation", EmployeeOccupation);
            nextintent.putExtra("CurrentDept", CurrentDeptt);
            nextintent.putExtra("Gender", Gender);
            nextintent.putExtra("personalid", personalid);
            nextintent.putExtra("empfathername", Empfathername);
            nextintent.putExtra("emailaddress", EmpEmailAddress);
            nextintent.putExtra("phoneno", phonno);

            nextintent.putExtra("dateofbirth", sDateofbirth);
            nextintent.putExtra("Address", sAddress);
            nextintent.putExtra("provice", sProvince);
            nextintent.putExtra("district", sDistrict);
            nextintent.putExtra("tehsil", sTehsil);
            nextintent.putExtra("village", sVillage);
            nextintent.putExtra("uc", sUnioncouncil);
            nextintent.putExtra("scale", sScale);
            nextintent.putExtra("joiningdate", sJoiningdate);

            startActivity(nextintent);

        } else if (id == backpress.getId()) {
            startActivity(new Intent(EmployeeRegistration1.this, AdminRole.class));
        }

    }

}
