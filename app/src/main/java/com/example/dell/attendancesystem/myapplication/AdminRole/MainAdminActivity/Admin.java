package com.example.dell.attendancesystem.myapplication.AdminRole.MainAdminActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.example.dell.attendancesystem.R;
import com.example.dell.attendancesystem.myapplication.UI.EmployeeRegistration1;
import com.example.dell.attendancesystem.myapplication.UI.MainActivity;

import androidx.appcompat.app.AppCompatActivity;

public class Admin extends AppCompatActivity {
    Button Loginbtn;
    EditText EmailAdd, Password;
    ImageView backpress;
    TelephonyManager telephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //            //    Activity#requestPermissions
            //            // here to request the missing permissions, and then overriding
            //            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //            //                                          int[] grantResults)
            //            // to handle the case where the user grants the permission. See the documentation
            //            // for Activity#requestPermissions for more details.
            return;
        }
      //final String IMEI_Number_Holder = telephonyManager.getDeviceId();

        backpress = findViewById(R.id.ok_btn_id);
        backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Admin.this, MainActivity.class));
            }
        });
        EmailAdd = findViewById(R.id.input_email);
        //EmailAdd.setText(IMEI_Number_Holder);

        Password = findViewById(R.id.input_password);
        Loginbtn = findViewById(R.id.admin_login);
        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = EmailAdd.getText().toString();
                String pass = Password.getText().toString();

                Bundle bn = getIntent().getExtras();
                if (bn != null) {
                    int key = bn.getInt("key");
                    if (key == 1) {
                        if (email.equals("admin") && (pass.equals("123"))) {
                            Intent intent = new Intent(Admin.this, EmployeeRegistration1.class);
                            startActivity(intent);
                        } else {
                            if (!email.equals("admin")) {
                                EmailAdd.setText("Error Email");
                            } else if (email.equals("admin")) {
                                Password.setText("Error Password");
                            }
                        }

                    } else {
                        if (email.equals("admin") && (pass.equals("123"))) {
                            Intent adminroll = new Intent(Admin.this, AdminRole.class);
                            startActivity(adminroll);
                        } else {
                            if (!email.equals("admin")) {
                                EmailAdd.setText("Error Email");
                            } else if (email.equals("admin")) {
                                Password.setText("Error Password");
                            }
                        }
                    }
                } else {
                    if (email.equals("admin") && (pass.equals("123"))) {
                        Intent adminroll = new Intent(Admin.this, AdminRole.class);
                        startActivity(adminroll);
                    } else {
                        if (!email.equals("admin")) {
                            EmailAdd.setText("Error Email");
                        } else if (email.equals("admin")) {
                            Password.setText("Error Password");
                        }
                    }
                }
            }

        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Admin.this, MainActivity.class));
        super.onBackPressed();
    }
}
