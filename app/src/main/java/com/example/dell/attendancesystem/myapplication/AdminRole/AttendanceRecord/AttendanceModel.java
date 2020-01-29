package com.example.dell.attendancesystem.myapplication.AdminRole.AttendanceRecord;
public class AttendanceModel {
    public String getEmplyeename() {
        return emplyeename;
    }

    public void setEmplyeename(String emplyeename) {
        this.emplyeename = emplyeename;
    }

    public String getEmpPID() {
        return empPID;
    }

    public void setEmpPID(String empPID) {
        this.empPID = empPID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTimein() {
        return Timein;
    }

    public void setTimein(String timein) {
        Timein = timein;
    }

    public String getTimeout() {
        return Timeout;
    }

    public void setTimeout(String timeout) {
        Timeout = timeout;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getPresent() {
        return present;
    }

    public void setPresent(int present) {
        this.present = present;
    }

    public int getAbsent() {
        return absent;
    }

    public void setAbsent(int absent) {
        this.absent = absent;
    }

    public int getLeave() {
        return leave;
    }

    public void setLeave(int leave) {
        this.leave = leave;
    }

    public AttendanceModel(String emplyeename, int present, int absent, int leave) {
        this.emplyeename = emplyeename;
        this.present = present;
        this.absent = absent;
        this.leave = leave;
    }


    public AttendanceModel(String empPID, String status, String timein, String timeout, String date) {
        this.empPID = empPID;
        Status = status;
        Timein = timein;
        Timeout = timeout;
        Date = date;
    }

    private String emplyeename;
    private String empPID;
    private String Status;
    private String Timein;
    private String Timeout;
    private String Date;
    private int present;
    private int absent;
    private int leave;
}
