package com.example.dell.attendancesystem.myapplication.SQLiteDatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;

import com.example.dell.attendancesystem.myapplication.Models.EmployeeRecord;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "BiometricEAttendence.db";
    // Table Names
    private static final String DB_TABLE_Employee = "Employee_Table";
    // column Name T1
    private static final String Employee_ID = "Employee_ID";
    private static final String Employee_PID = "Employee_PID";
    private static final String Employee_FName = "Father_Name";
    private static final String Employee_Name = "Employee_Name";
    private static final String Gender = "Gender";
    private static final String Employee_CNIC = "Employee_CNIC";
    private static final String Employee_DateOFBirth = "DateofBirth";
    private static final String Employee_Occup = "Employee_Occup";
    private static final String Employee_Scale = "Employee_Scale";
    private static final String Emailaddress = "Email_Address";
    private static final String Phoneno = "Phone_No";
    private static final String EmployeeAddress = "Employee_Address";
    private static final String Employee_Joining_Date = "Joining_Date";
    private static final String CurrentDepartment = "CD";
    private static final String ProvinceName = "Province";
    private static final String DistrictName = "District";
    private static final String TehsilName = "Tehsil";
    private static final String Village = "Village";
    private static final String UnionCouncil = "UnionCouncil";
    private static final String FINGER_PRINT_ID = "FingerPrintID";
    private static final String Finger_Print = "FingerPrint";
    //column Name T2
    private static final String Institude_Table = "Institude_Table";
    private static final String id = "ID";
    private static final String Radius = "Radius";
    private static final String Latitudee = "Latitude";
    private static final String Longitudee = "Longitude";

    //index                empID 0                                             PID  1                     Fname 2                        name 3
    //                     gender 4    cnic 5                         dob 6                             occup 7                     scale 8
    //                    emailaddress 9        phone 10       emailadd 11                joining date 12                       currentdepartemtn 13
    //                 provice 14        district 15       tehsilname 16         unioncouncil 17        village 18      fingerprintid 19  fingerprint 20


    // Table create statement
    private static final String EmployeeTable_Statement =
            "CREATE TABLE " + DB_TABLE_Employee + "("
                    + Employee_ID + " INTEGER primary key autoincrement," + Employee_PID + " Text," + Employee_FName + " Text," + Employee_Name + " Text,"
                    + Gender + " Text," + Employee_CNIC + " Text," + Employee_DateOFBirth + " Text," + Employee_Occup + " Text," + Employee_Scale + " Text,"
                    + Emailaddress + " Text," + Phoneno + " Text," + EmployeeAddress + " Text, " + Employee_Joining_Date + " Text, " + CurrentDepartment + " Text,"
                    + ProvinceName + " Text," + DistrictName + " Text," + TehsilName + " Text," + UnionCouncil + " Text," + Village + " Text," + FINGER_PRINT_ID + " TEXT,"
                    + Finger_Print + " BLOB)";
    //Table Create Statement
    private static final String DepartmentTable = "CREATE TABLE " + Institude_Table + "(" + id + " INTEGER primary key autoincrement," + Radius + " integer," + Latitudee + " double," + Longitudee + " double)";
    //Column name T3
    private static final String Attendance_Table = "Attendance_Table";
    private static final String Attendance_ID = "AttendanceID";
    private static final String EmpPID = "Employee_PID";
    private static final String TimeIn = "TimeIn";
    private static final String TimeOut = "TimeOut";
    private static final String Datee = "Date";
    private static final String Status = "Status";
    //Table Create Statment
    private static final String AttendanceTable = "Create Table " + Attendance_Table + "(" + Attendance_ID + " INTEGER primary key autoincrement," + EmpPID + " Text," + TimeIn + " Text," + TimeOut + " Text," + Datee + " Text," + Status + " Text)";

    //Column Name T4
    private static final String Explanation_Table = "Explanation_Table";
    private static final String EmpPPIIDD = "EmpPID";
    private static final String EmpStatus = "Status";
    private static final String Timestamp = "Timestamp";
    private static final String Explanation = "Explanation";

    private static final String ExplanationTableStatement =
            "Create Table " + Explanation_Table + "(" + EmpPPIIDD + " text," + EmpStatus + " text," + Timestamp + " text," + Explanation + " text)";


    SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        // db=getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating table
        db.execSQL(DepartmentTable);
        db.execSQL(EmployeeTable_Statement);
        db.execSQL(AttendanceTable);
        db.execSQL(ExplanationTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + Attendance_Table);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_Employee);
        db.execSQL("DROP TABLE IF EXISTS " + Institude_Table);
        db.execSQL("DROP TABLE IF EXISTS " + Explanation_Table);

        // db.execSQL(" Drop Table " + DB_TABLE_Employee);
        //db.execSQL(CREATE_TABLE_IMAGE);

        onCreate(db);
    }


    public boolean addEntryofEmpInfo(String employee_Name, String fathername, String employee_Cnic,
                                     String dob, String gender, String address, String province, String district, String tehsil, String village, String unioncouncil,
                                     String phonenoo, String Email, String empPID, String employee_Occup, String scale, String joiningdate, String currentDepartment,
                                     String fingerprintid, byte[] fingerprint) throws SQLiteException {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Employee_Name, employee_Name);
        cv.put(Employee_FName, fathername);
        cv.put(Employee_CNIC, employee_Cnic);
        cv.put(Employee_DateOFBirth, dob);
        cv.put(Gender, gender);
        cv.put(EmployeeAddress, address);
        cv.put(ProvinceName, province);
        cv.put(DistrictName, district);
        cv.put(TehsilName, tehsil);
        cv.put(Village, village);
        cv.put(UnionCouncil, unioncouncil);
        cv.put(Phoneno, phonenoo);
        cv.put(Emailaddress, Email);
        cv.put(Employee_PID, empPID);
        cv.put(Employee_Occup, employee_Occup);
        cv.put(Employee_Scale, scale);
        cv.put(Employee_Joining_Date, joiningdate);
        cv.put(CurrentDepartment, currentDepartment);
        cv.put(FINGER_PRINT_ID, fingerprintid);
        cv.put(Finger_Print, fingerprint);
        long result = db.insert(DB_TABLE_Employee, null, cv);
        if (result == 0) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getemppidbyname(String en){
        db=this.getReadableDatabase();
        Cursor cur=db.rawQuery("Select "+Employee_PID+" from "+ DB_TABLE_Employee +" where Employee_Name="+"'"+en+"'", null);
        return cur;
    }
    public boolean addInstituteInfo(double Radius1, double Latitude1, double Longitude) throws SQLiteException {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Radius, Radius1);
        cv.put(Latitudee, Latitude1);
        cv.put(Longitudee, Longitude);
        long result = db.insert(Institude_Table, null, cv);
        if (result == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateInstituteInfo(double Radius1, double Latitude1, double Longitude1) throws SQLiteException {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Radius, Radius1);
        cv.put(Latitudee, Latitude1);
        cv.put(Longitudee, Longitude1);
        //String id="1";
        long result = db.update(Institude_Table, cv, id, null);
        if (result == 0) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getInstitude_Table() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select  * from " + Institude_Table, null);
        return cursor;
    }

    //TimeIn
    public boolean addEntryForAttendance(String Employeepid, String timein, String todaydate, String Status1) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(EmpPID, Employeepid);
        cv.put(TimeIn, timein);
        cv.put(Datee, todaydate);
        cv.put(Status, Status1);
        long result = db.insert(Attendance_Table, null, cv);
        if (result == 0) {
            return false;
        } else {
            return true;
        }
    }

    //TimeOut
    public boolean UpdateAttendance(String Employeepid, String timeout, String todaydate) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(EmpPID, Employeepid);
        cv.put(TimeOut, timeout);
        cv.put(Datee, todaydate);
        long result = db.update(Attendance_Table, cv, Employee_PID + "='" + Employeepid + "'", null);
        if (result == 0) {
            return false;
        } else {
            return true;
        }
    }
    public String CheckTodayTimInTime(String date, String PID) {
        db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("Select * from " + Attendance_Table + " where " + Datee + "=" + "'" + date + "' and " + EmpPID + "=" + "'" + PID + "'", null);
        cur.moveToFirst();
        String timeinn = cur.getString(2);
        return timeinn;
    }

    //CheckTimeIn
    public boolean CheckTodayTimIn(String date, String PID) {
        db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("Select * from " + Attendance_Table + " where " + Datee + "=" + "'" + date + "' and " + EmpPID + "=" + "'" + PID + "'", null);
        if (cur.moveToFirst()) {
            String timeinn = cur.getString(2);
            if (timeinn.isEmpty()) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }


    //CheckTimeOut
    public boolean CheckTodayTimOut(String date, String PID) {
        db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("Select * from " + Attendance_Table + " where " + Datee + "='" + date + "' and " + EmpPID + "=" + "'" + PID + "'", null);
        if (cur.moveToFirst()) {
            String timeout = cur.getString(3);
            if (timeout==null) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }

    }


    //All Record
    public Cursor GetAttendanceRecord() {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + Attendance_Table, null);
        return cursor;
    }

    //Today Record
    public Cursor GetAttendanceRecordByOnlyTodayDate(String todaydate1) {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + Attendance_Table + " where " + Datee + "='" + todaydate1 + "'", null);
        return cursor;
    }

    //For weekly monthly Yearly
    public Cursor GetAttendanceRecordBetweenTwoDate(String Fromdate, String Todate) {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + Attendance_Table + " where " + Datee + " Between '" + Fromdate + "' AND '" + Todate + "'", null);
        return cursor;
    }

    public Cursor DistinctAllAttendanceRec() {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select Distinct Employee_PID from " + Attendance_Table, null);
        return cursor;
    }
    public Cursor DistinctTodayAttendanceRec(String todaydate1) {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select Distinct Employee_PID from " + Attendance_Table+ " where " + Datee + "='" + todaydate1 + "'", null);
        return cursor;
    }
    public Cursor DistinctBetweenAttendanceRec(String Fromdate, String Todate) {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select Distinct Employee_PID from " + Attendance_Table+ " where " + Datee + " Between '" + Fromdate + "' AND '" + Todate + "'", null);
        return cursor;
    }
//2

    public Cursor DistinctAttendanceRecord(String Emppid) {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select Distinct Employee_PID from " + Attendance_Table+  " where "+Employee_PID + "='" + Emppid + "'", null);
        return cursor;
    }
 //3
    public Cursor EmployeeAttendanceRecord(String Emppid,String status) {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select Status from " + Attendance_Table+  " where "+Employee_PID + "='" + Emppid + "' and "+Status + "='"
                + status + "'", null);
        return cursor;
    }

    //Today Record
    public Cursor EmployeeAllAttendanceRecord(String Emppid,String todaydate1) {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select Status,Employee_PID from " + Attendance_Table + " where " + Datee + "='" + todaydate1 + "' and "+ Employee_PID + "='" + Emppid + "'", null);
        return cursor;
    }

    //For weekly monthly Yearly
    public Cursor GetAttendanceRecordBetweenTwoDate(String Emppid,String Fromdate, String Todate) {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select Status,Employee_PID from " + Attendance_Table + " where "+ Employee_PID + "='" + Emppid + "' and " + Datee + " Between '" + Fromdate + "' AND '" + Todate + "'", null);
        return cursor;
    }

    public boolean Deleteattendace(String empPID) {
        db = this.getWritableDatabase();
        String pid = empPID;
        long result = db.delete(Attendance_Table, Employee_PID + "=" + "'" + empPID + "'", null);
        if (result == 0) {
            return false;
        } else {
            return true;
        }
    }
    public boolean UploadtoServer() {
        return true;
    }


//    public Cursor GetAllData() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("Select " + KEY_IMAGE + " from " + DB_TABLE_Employee, null);
//        return res;
//        }

    public Cursor GetEmplooyeeRecord() {
        db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("Select * from " + DB_TABLE_Employee, null);
        return cur;
    }

    public Cursor GetPIDBYBlob(byte[] image) {
        db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("Select " + Employee_PID + ", " + Finger_Print + " from " + DB_TABLE_Employee + " where " + Finger_Print + "='" + image + "'", null);
        return cur;
    }

      public Cursor GetEmplyeeByID(String PID) {
        db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("Select * from " + DB_TABLE_Employee + " where " + Employee_PID + "=" + "'" + PID + "'", null);
        return cur;
    }
    public Cursor GGGetEmplyeeByID(String PID) {
        db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("Select * from " + DB_TABLE_Employee + " where " + Employee_PID + "=" + "'" + PID + "'", null);
        return cur;
    }


    public boolean UpdateEmployee(String empPID, String employee_Name, String employee_Cnic,
                                  String employee_Occup, String currentDepartment, String gender, String Email, String phonenoo,
                                  String fname1, String dob1, String scale1, String address1, String joiningdate1, String pprovince,
                                  String district1, String tehsil1, String unioncouncil1, String village1) {

        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //cv.put(Employee_PID, empPID);
        cv.put(Employee_Name, employee_Name);
        cv.put(Employee_CNIC, employee_Cnic);
        cv.put(Employee_Occup, employee_Occup);
        cv.put(CurrentDepartment, currentDepartment);
        cv.put(Gender, gender);
        cv.put(Emailaddress, Email);
        cv.put(Phoneno, phonenoo);
        cv.put(Employee_FName, fname1);
        cv.put(Employee_DateOFBirth, dob1);
        cv.put(Employee_Scale, scale1);
        cv.put(EmployeeAddress, address1);
        cv.put(Employee_Joining_Date, joiningdate1);
        cv.put(ProvinceName, pprovince);
        cv.put(DistrictName, district1);
        cv.put(TehsilName, tehsil1);
        cv.put(UnionCouncil, unioncouncil1);
        cv.put(Village, village1);

        long result = db.update(DB_TABLE_Employee, cv, Employee_PID + "=" + "'" + empPID + "'", null);
        if (result == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean DeleteEmployee(String empPID) {
        db = this.getWritableDatabase();
        String pid = empPID;
        long result = db.delete(DB_TABLE_Employee, Employee_PID + "=" + "'" + empPID + "'", null);
        if (result == 0) {
            return false;
        } else {
            return true;
        }
    }


    public Cursor GetEmployeeRecord() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("Select  * from " + DB_TABLE_Employee, null);
        return res;

    }


    public Cursor GetFingerprintBuffer() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select " + Finger_Print + " from " + DB_TABLE_Employee, null);
        return res;
    }

    public boolean AddExplanationCall(String Emppid, String EmppStatus, String Timestamp, String Explanation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(EmpPPIIDD, Emppid);
        cv.put(EmpStatus, EmppStatus);
        cv.put(this.Timestamp, Timestamp);
        cv.put(this.Explanation, Explanation);
        long result = db.insert(Explanation_Table, "", cv);
        if (result == 0) {
            return true;
        } else {
            return false;
        }
    }


    public Cursor GetExplanation() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + Explanation_Table, null);
        return cursor;
    }
}

