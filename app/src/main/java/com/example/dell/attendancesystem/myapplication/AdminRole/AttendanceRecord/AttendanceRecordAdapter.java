package com.example.dell.attendancesystem.myapplication.AdminRole.AttendanceRecord;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dell.attendancesystem.R;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AttendanceRecordAdapter extends RecyclerView.Adapter<AttendanceRecordAdapter.AttendanceView> {
    Context context;
    ArrayList<AttendanceModel> employeedata;
    public AttendanceRecordAdapter(Context context1, ArrayList<AttendanceModel> employeedata) {
        this.context = context1;
        this.employeedata = employeedata;
    }

    @NonNull
    @Override
    public AttendanceView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.attendencerecord_view, parent, false);
        return new AttendanceView(listItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AttendanceView holder, int position) {
        AttendanceModel attendancemodel = employeedata.get(position);
        String employeename = attendancemodel.getEmplyeename();
        holder.empname.setText(employeename);
        String statuss = attendancemodel.getStatus();
        String present = String.valueOf(attendancemodel.getPresent());
        holder.presentnumb.setText(present);
        String leave = String.valueOf(attendancemodel.getLeave());
        String absent = String.valueOf(attendancemodel.getAbsent());
        holder.leavenumb.setText(leave);
        holder.absentnumb.setText(absent);

        String pre=String.valueOf(attendancemodel.getPresent());
        String abs=String.valueOf(attendancemodel.getAbsent());

        holder.absentnumb.setText(String.valueOf(attendancemodel.getAbsent()));
        holder.presentnumb.setText(String.valueOf(attendancemodel.getPresent()));
        holder.leavenumb.setText(String.valueOf(attendancemodel.getLeave()));
//        if (attendancemodel.getPresent() > 0 || attendancemodel.getAbsent() >= 0 || attendancemodel.getLeave() >= 0) {
//            holder.absentnumb.setText(String.valueOf(attendancemodel.getAbsent()));
//            holder.presentnumb.setText(String.valueOf(attendancemodel.getPresent()));
//            holder.leavenumb.setText(String.valueOf(attendancemodel.getLeave()));
//        }
    }

    @Override
    public int getItemCount() {
        return employeedata.size();
    }

    public class AttendanceView extends RecyclerView.ViewHolder {
        TextView empname, presentnumb, absentnumb, leavenumb;
        ImageView presentimg, absentimg, leaveimg;
        LinearLayout linearLayout;

        public AttendanceView(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.listener_id);
            empname = itemView.findViewById(R.id.employee_name_id);
            presentnumb = itemView.findViewById(R.id.numbpresent_id);
            absentnumb = itemView.findViewById(R.id.numbabsent_id);
            leavenumb = itemView.findViewById(R.id.numbleave_id);
            presentimg = itemView.findViewById(R.id.present_imageid);
            absentimg = itemView.findViewById(R.id.apsent_imageid);
            leaveimg = itemView.findViewById(R.id.leave_imageid);
        }
    }

    public void clear() {
        employeedata.clear();
    }

}
