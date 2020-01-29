package com.example.dell.attendancesystem.myapplication.AdminRole.UploadAttendance;

public class DataModel {
    public DataModel() {
    }

    public DataModel(String Emppid, String timinn, String timout, String date, String status) {
        this.Date = date;
        this.empid = Emppid;
        this.timein = timinn;
        this.timeout = timout;
        this.status = status;

    }

    String Date;

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getTimein() {
        return timein;
    }

    public void setTimein(String timein) {
        this.timein = timein;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String empid;
    String timein;
    String timeout;
    String status;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {

        this.Date = date;
    }
}
