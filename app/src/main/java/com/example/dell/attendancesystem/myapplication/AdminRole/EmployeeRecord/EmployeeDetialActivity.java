package com.example.dell.attendancesystem.myapplication.AdminRole.EmployeeRecord;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.attendancesystem.R;
import com.example.dell.attendancesystem.myapplication.AdminRole.EmployeeRecord.Services.CallSoap;
import com.example.dell.attendancesystem.myapplication.AdminRole.MainAdminActivity.AdminRole;
import com.example.dell.attendancesystem.myapplication.SQLiteDatabaseHelper.DatabaseHelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class EmployeeDetialActivity extends AppCompatActivity {
    TextView EmpName, PhoneNo, EmailAddress, CurrentDepartment, CNIC, EmployeePID, Gender, desig;
    TextView fname, address, joiningdate, province, district, tehsil, village, unioncouncil, dob, scale;
    ImageView backpress;
    Button button;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    model model1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_detial);
        preferences = getSharedPreferences("pref", MODE_PRIVATE);
        editor = preferences.edit();
        desig = findViewById(R.id.des_id);
        EmpName = findViewById(R.id.e_n_id);
        PhoneNo = findViewById(R.id.Phone_no_id);
        EmailAddress = findViewById(R.id.email_addres_id);
        CurrentDepartment = findViewById(R.id.Current_Dept_id);
        CNIC = findViewById(R.id.cnic_id);
        EmployeePID = findViewById(R.id.pid_id);
        Gender = findViewById(R.id.gender_id);
        fname = findViewById(R.id.Fnameid1);
        address = findViewById(R.id.address_emp_detial);
        joiningdate = findViewById(R.id.joiningdate_emp_detial);
        province = findViewById(R.id.provinceidid1);
        district = findViewById(R.id.District_id_detial_view);
        tehsil = findViewById(R.id.ttehsil_id_detialview);
        village = findViewById(R.id.villageid_detial_emp);
        unioncouncil = findViewById(R.id.unioncouncilid_detial);
        dob = findViewById(R.id.dobirth);
        scale = findViewById(R.id.employee_scale_id);
        button = findViewById(R.id.btn_idd);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int deletrecord = bundle.getInt("one");
            int updaterecord = bundle.getInt("two");
            int main = bundle.getInt("main");
            String empname = bundle.getString("E_name");
            String phoneno = bundle.getString("phone");
            String email = bundle.getString("email");
            String cd = bundle.getString("cd");
            String cnic = bundle.getString("cnic");
            String empPID = bundle.getString("emppid");
            String gender = bundle.getString("gender");
            String Designation = bundle.getString("Designation");
            String fname1 = bundle.getString("fname");
            String dob1 = bundle.getString("dob");
            String scale1 = bundle.getString("scale");
            String address1 = bundle.getString("address");
            String joiningdate1 = bundle.getString("joiningdate");
            String province1 = bundle.getString("province");
            String district1 = bundle.getString("district1");
            String tehsil1 = bundle.getString("tehsil1");
            String unioncouncil1 = bundle.getString("unioncouncil");
            String village11 = bundle.getString("village1");
            model1=new model(empname,phoneno, email,cd, cnic, empPID,gender, Designation, fname1,dob1, scale1,address1,joiningdate1,province1,district1,tehsil1, unioncouncil1,village11);

            editor.putString("Designation", Designation);
            editor.putString("name", empname);
            editor.putString("phone", phoneno);
            editor.putString("email", email);
            editor.putString("cd", cd);
            editor.putString("cnic", cnic);
            editor.putString("empPID", empPID);
            editor.putString("gender", gender);
            editor.putString("fname", fname1);
            editor.putString("dob", dob1);
            editor.putString("scale", scale1);
            editor.putString("address", address1);
            editor.putString("joiningdate", joiningdate1);
            editor.putString("province", province1);
            editor.putString("district1", district1);
            editor.putString("tehsil1", tehsil1);
            editor.putString("unioncouncil", unioncouncil1);
            editor.putString("village1", village11);

            editor.commit();

            button.setVisibility(View.INVISIBLE);
            fname.setText(fname1);
            address.setText(address1);
            joiningdate.setText(joiningdate1);
            dob.setText(dob1);
            scale.setText(scale1);
            province.setText(province1);
            district.setText(district1);
            tehsil.setText(tehsil1);
            unioncouncil.setText(unioncouncil1);
            village.setText(village11);

            EmpName.setText(empname);
            PhoneNo.setText(phoneno);
            EmailAddress.setText(email);
            CurrentDepartment.setText(cd);
            CNIC.setText(cnic);
            EmployeePID.setText(empPID);
            Gender.setText(gender);
            desig.setText(Designation);


        } else {
            Toast.makeText(this, "Bundle Is Empty", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 1, "Delete Record").setIcon(R.drawable.ic_delete);
        menu.add(1, 2, 2, "Upload Record").setIcon(R.drawable.ic_file_upload);
        menu.add(1, 3, 3, "Edit Record").setIcon(R.drawable.ic_edit_black_24dp);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == 1) {
            button.setVisibility(View.VISIBLE);
            button.setText("Delete Record");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog alertDialog = new AlertDialog.Builder(EmployeeDetialActivity.this).create();

                    alertDialog.setTitle(" Do you want to remove Employee ?");
                    alertDialog.setButton(alertDialog.BUTTON_POSITIVE, "confirm delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            DatabaseHelper db = new DatabaseHelper(EmployeeDetialActivity.this);
                            String pid = preferences.getString("empPID", "");
                            if (db.DeleteEmployee(pid) == true) {
                                startActivity(new Intent(EmployeeDetialActivity.this, AdminRole.class));
                                Toast.makeText(EmployeeDetialActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(EmployeeDetialActivity.this, AdminRole.class));
                                Toast.makeText(EmployeeDetialActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    alertDialog.setButton(alertDialog.BUTTON_NEUTRAL, "no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            alertDialog.setCancelable(true);

                        }
                    });

                    alertDialog.show();
                }
            });

        } else if (id == 2) {
            Intent intent = new Intent(this, EmployeeDetialActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("two", 2);
            String name = preferences.getString("name", "");
            String phone = preferences.getString("phone", "");
            String cd = preferences.getString("cd", "");
            String email = preferences.getString("email", "");
            String cnic = preferences.getString("cnic", "");
            String empPID = preferences.getString("empPID", "");
            String gender = preferences.getString("gender", "");
            String des = preferences.getString("Designation", "");
            String fname = preferences.getString("fname", "");
            String dob = preferences.getString("dob", "");
            String scale = preferences.getString("scale", "");
            String address = preferences.getString("address", "");
            String joiningdate = preferences.getString("joiningdate", "");
            String province = preferences.getString("province", "");
            String district = preferences.getString("district1", "");
            String tehsil = preferences.getString("tehsil1", "");
            String unioncouncil = preferences.getString("unioncouncil", "");
            String village1 = preferences.getString("village1", "");

            button.setVisibility(View.VISIBLE);
            button.setText("Upload Record");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Upload to Server
                   //CallAPIs(name,phone,cd,email,cnic,empPID,gender,des,fname,dob,scale,address,joiningdate,province,district,tehsil,unioncouncil,village1);
                   new AsyncCallSoap().execute();
                }
            });
        } else if (id == 3) {
            Intent intent = new Intent(this, EditEmployeeRecord.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("three", 3);
            String name = preferences.getString("name", "");
            String phone = preferences.getString("phone", "");
            String cd = preferences.getString("cd", "");
            String email = preferences.getString("email", "");
            String cnic = preferences.getString("cnic", "");
            String empPID = preferences.getString("empPID", "");
            String gender = preferences.getString("gender", "");
            String des = preferences.getString("Designation", "");
            String fname = preferences.getString("fname", "");
            String dob = preferences.getString("dob", "");
            String scale = preferences.getString("scale", "");
            String address = preferences.getString("address", "");
            String joiningdate = preferences.getString("joiningdate", "");
            String province = preferences.getString("province", "");
            String district = preferences.getString("district1", "");
            String tehsil = preferences.getString("tehsil1", "");
            String unioncouncil = preferences.getString("unioncouncil", "");
            String village1 = preferences.getString("village1", "");
            intent.putExtra("E_name", name);
            intent.putExtra("phone", phone);
            intent.putExtra("email", email);
            intent.putExtra("cd", cd);
            intent.putExtra("cnic", cnic);
            intent.putExtra("emppid", empPID);
            intent.putExtra("gender", gender);
            intent.putExtra("Designation", des);
            intent.putExtra("fname", fname);
            intent.putExtra("dob", dob);
            intent.putExtra("scale", scale);
            intent.putExtra("address", address);
            intent.putExtra("joiningdate", joiningdate);
            intent.putExtra("province", province);
            intent.putExtra("district1", district);
            intent.putExtra("tehsil1", tehsil);
            intent.putExtra("unioncouncil", unioncouncil);
            intent.putExtra("village1", village1);

            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void CallAPIs(String name,
                     String phone, String cd, String email, String cnic, String empPID, String gender, String des,
                     String fname,
                     String dob,
                     String scale,
                     String address, String joiningdate, String province, String district, String tehsil, String unioncouncil, String village1) {



    }
    class AsyncCallSoap extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
           // progressDialog = new ProgressDialog(EmployeeDetialActivity.this);
           // progressDialog.setMessage("Uploading....");
            //progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            CallSoap callSoap = new CallSoap();
            String response = callSoap.EmpRegistration(model1.getName(),model1.getPhoneno(),model1.cd,model1.email,model1.getCnic(),model1.empPID,model1.getGender(),model1.district
                    ,model1.fname,model1.getDob(),model1.getScale(),model1.getAddress(),model1.getJoiningdate(),model1.getProvince(),model1.getDistrict(),model1.getTehsil(),model1.getUnioncouncil(),model1.getVillage1());
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
           // progressDialog.dismiss();
            Toast.makeText(EmployeeDetialActivity.this, "Response="+s, Toast.LENGTH_SHORT).show();
            super.onPostExecute(s);
        }

    }


}
