package com.example.dell.attendancesystem.myapplication.AdminRole.EmployeeRecord;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.dell.attendancesystem.R;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.ViewForEmployeeList> {

    private ArrayList<String> EName;
    private ArrayList<String> EDesg;
    private ArrayList<String> EmployeePID;
    private ArrayList<String> EmployeeCNIC;
    private ArrayList<String> CurrentDepartment;
    private ArrayList<String> Gender;
    private ArrayList<String> Emailaddress;
    private ArrayList<String> Phoneno;
    private ArrayList<String> EmployeeFname;
    private ArrayList<String> EmployeeDOB;
    private ArrayList<String> EmployeeScale;
    private ArrayList<String> EmployeeAddress;
    private ArrayList<String> JoiningDate;
    private ArrayList<String> province;
    private ArrayList<String> village;
    private ArrayList<String> district;
    private ArrayList<String> tehsil;
    private ArrayList<String> uniocouncil;
    Context context1;
    public EmployeeListAdapter(ArrayList<String> EmployeePID1,ArrayList<String> name, ArrayList<String> EmployeeCNIC1, ArrayList<String> desg,
                                       ArrayList<String> CurrentDepartment1,
                                       ArrayList<String> Gender1,ArrayList<String> Emailaddress1,ArrayList<String> Phoneno1,
                                       ArrayList<String> province,
                                       ArrayList<String> village,
                                       ArrayList<String> district,
                                       ArrayList<String> tehsil,
                                       ArrayList<String> uniocouncil, Context context,
                                       ArrayList<String> EmployeeFname1,
                                       ArrayList<String> EmployeeDOB1,
                                       ArrayList<String> EmployeeScale1,
                                       ArrayList<String> EmployeeAddress1,
                                       ArrayList<String> JoiningDate1) {
        this.EmployeePID=EmployeePID1;
        this.EName = name;
        this.EmployeeCNIC=EmployeeCNIC1;
        this.EDesg = desg;
        this.CurrentDepartment=CurrentDepartment1;
        this.Gender=Gender1;
        this.Emailaddress=Emailaddress1;
        this.Phoneno=Phoneno1;
        context1 = context;
        this.EmployeeFname=EmployeeFname1;
        this.EmployeeDOB=EmployeeDOB1;
        this.EmployeeScale=EmployeeScale1;
        this.EmployeeAddress=EmployeeAddress1;
        this.JoiningDate=JoiningDate1;
        this.province=province;
        this.village=village;
        this.district=district;
        this.tehsil=tehsil;
        this.uniocouncil=uniocouncil;

    }


    @NonNull
    @Override
    public ViewForEmployeeList onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem = layoutInflater.inflate(R.layout.employee_list, viewGroup, false);

        return new ViewForEmployeeList(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewForEmployeeList viewForEmployeeList, int i) {
        String name = EName.get(i);
        String Designation = EDesg.get(i);
        String phoNo=Phoneno.get(i);
        String email=Emailaddress.get(i);
        String cd=CurrentDepartment.get(i);
        String cnic=EmployeeCNIC.get(i);
        String empPID=EmployeePID.get(i);
        String gender=Gender.get(i);
        String fname=EmployeeFname.get(i);
        String dob=EmployeeDOB.get(i);
        String scale=EmployeeScale.get(i);
        String address=EmployeeAddress.get(i);
        String joiningdate=JoiningDate.get(i);
        String province1=province.get(i);
        String district1=district.get(i);
        String tehsil1=tehsil.get(i);
        String unioncouncil=uniocouncil.get(i);
        String village1=village.get(i);
        viewForEmployeeList.employeename.setText(name);
        viewForEmployeeList.designation.setText(Designation);
        viewForEmployeeList.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context1, EmployeeDetialActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                intent.putExtra("E_name",name);
                intent.putExtra("phone",phoNo);
                intent.putExtra("email",email);
                intent.putExtra("cd",cd);
                intent.putExtra("Designation",Designation);
                intent.putExtra("cnic",cnic);
                intent.putExtra("emppid",empPID);
                intent.putExtra("gender",gender);
                intent.putExtra("fname",fname);
                intent.putExtra("dob",dob);
                intent.putExtra("scale",scale);
                intent.putExtra("address",address);
                intent.putExtra("joiningdate",joiningdate);
                intent.putExtra("province",province1);
                intent.putExtra("district1",district1);
                intent.putExtra("tehsil1",tehsil1);
                intent.putExtra("unioncouncil",unioncouncil);
                intent.putExtra("village1",village1);
                intent.putExtra("main",3);
                context1.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return EName.size();
    }

    public class ViewForEmployeeList extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView employeename, designation;


        public ViewForEmployeeList(@NonNull View itemView) {

            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearlayout_employee_list);
            employeename = itemView.findViewById(R.id.emp_name);
            designation = itemView.findViewById(R.id.emp_desig);


        }
    }
}
