package com.example.dell.attendancesystem.myapplication.AdminRole.EmployeeRecord;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dell.attendancesystem.R;
import com.example.dell.attendancesystem.myapplication.AdminRole.MainAdminActivity.AdminRole;
import com.example.dell.attendancesystem.myapplication.BiometricStuff.GetAttendanceActivity;
import com.example.dell.attendancesystem.myapplication.SQLiteDatabaseHelper.DatabaseHelper;

public class EditEmployeeRecord extends AppCompatActivity {
    EditText EmpName, PhoneNo, EmailAddress, CNIC, EmployeePID,Designation;
    EditText fname,address,joiningdate,province,district,tehsil,village,unioncouncil,dob,scale;
    TextView Gender,CurrentDepartment;
    Button button;
    ImageView bckbtn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee_record);
       bckbtn2=findViewById(R.id.actvity_empedit_bck_btn_id);
        bckbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditEmployeeRecord.this, EmployeeList.class));

            }
        });

        EmpName = findViewById(R.id.e_n_id1);
        PhoneNo = findViewById(R.id.Phone_no_id1);
        EmailAddress = findViewById(R.id.email_addres_id1);
        CurrentDepartment = findViewById(R.id.Current_Dept_id1);
        CNIC = findViewById(R.id.cnic_id1);
        Designation=findViewById(R.id.des_id1);
        EmployeePID = findViewById(R.id.pid_id1);
        Gender = findViewById(R.id.gender_id1);
        fname=findViewById(R.id.Fnameid);
        address=findViewById(R.id.employee_Address_id);
        joiningdate=findViewById(R.id.jd_id);
        province=findViewById(R.id.provinceidid);
        district=findViewById(R.id.District_id);
        tehsil=findViewById(R.id.ttehsil_id);
        village=findViewById(R.id.villageid);
        unioncouncil=findViewById(R.id.unioncouncilid);
        dob=findViewById(R.id.dob11);
        scale=findViewById(R.id.employee_scale_id);




        model model1=new model();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String empname = bundle.getString("E_name");
            String phoneno = bundle.getString("phone");
            String email = bundle.getString("email");
            String cd = bundle.getString("cd");
            String cnic = bundle.getString("cnic");
            String empPID = bundle.getString("emppid");
            String gender = bundle.getString("gender");
            String des=bundle.getString("Designation");

            String fname1=bundle.getString("fname");
            String dob1=bundle.getString("dob");
            String scale1=bundle.getString("scale");
            String address1=bundle.getString("address");
            String joiningdate1=bundle.getString("joiningdate");
            String pprovince = bundle.getString("province");
            String district1 = bundle.getString("district1");
            String tehsil1 = bundle.getString("tehsil1");
            String unioncouncil1 = bundle.getString("unioncouncil");
            String village1=bundle.getString("village1");
           // String fname1,dob1,scale1,address1,joiningdate1,pprovince,district1, tehsil1,unioncouncil1,village1;



            fname.setText(fname1);
            address.setText(address1);
            joiningdate.setText(joiningdate1);
            dob.setText(dob1);
            scale.setText(scale1);
            province.setText(pprovince);
            district.setText(district1);
            tehsil.setText(tehsil1);
            unioncouncil.setText(unioncouncil1);
            village.setText(village1);

            Designation.setText(des);
            EmpName.setText(empname);
            PhoneNo.setText(phoneno);
            EmailAddress.setText(email);
            CurrentDepartment.setText(cd);
            CNIC.setText(cnic);
            EmployeePID.setText(empPID);
            Gender.setText(gender);





        button=findViewById(R.id.btn_id1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model1.setDesignation(Designation.getText().toString());
                model1.setName(EmpName.getText().toString());
                model1.setPhoneno(PhoneNo.getText().toString());
                model1.setCd(CurrentDepartment.getText().toString());
                model1.setCnic(CNIC.getText().toString());
                model1.setGender(Gender.getText().toString());
                model1.setEmpPID(EmployeePID.getText().toString());
                model1.setEmail(EmailAddress.getText().toString());
                DatabaseHelper databaseHelper=new DatabaseHelper(EditEmployeeRecord.this);

               if(databaseHelper.UpdateEmployee(model1.getEmpPID(),model1.getName(),model1.getCnic(),model1.getDesignation(),
                       model1.getCd(),model1.getGender(),model1.getEmail(),model1.getPhoneno(),
                        fname1,dob1,scale1,address1,joiningdate1,pprovince,district1, tehsil1,unioncouncil1,village1)==true){
                   Toast.makeText(EditEmployeeRecord.this, "Edited success", Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(EditEmployeeRecord.this, AdminRole.class));
               }
            }
        });
        }else {
            Toast.makeText(this, "Data Is Null", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EditEmployeeRecord.this, AdminRole.class));
    }
}
