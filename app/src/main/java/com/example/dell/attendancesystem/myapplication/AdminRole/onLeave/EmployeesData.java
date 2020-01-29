package com.example.dell.attendancesystem.myapplication.AdminRole.onLeave;

import androidx.annotation.NonNull;

public class EmployeesData {
    public String getEmplyeeName() {
        return EmplyeeName;
    }

    public void setEmplyeeName(String emplyeeName) {
        EmplyeeName = emplyeeName;
    }

    public String getEmployeeDesignation() {
        return EmployeeDesignation;
    }

    public void setEmployeeDesignation(String employeeDesignation) {
        EmployeeDesignation = employeeDesignation;
    }

    public EmployeesData(String emplyeeName, String employeeDesignation) {
        EmplyeeName = emplyeeName;
        EmployeeDesignation = employeeDesignation;
    }

    public EmployeesData() {
    }


    String EmplyeeName;
    String EmployeeDesignation;
    String EmpPID;

    public EmployeesData(String emplyeeName, String employeeDesignation, String empPID) {
        EmplyeeName = emplyeeName;
        EmployeeDesignation = employeeDesignation;
        EmpPID = empPID;
    }

    public String getEmpPID() {
        return EmpPID;
    }

    @NonNull
    @Override
    public String toString() {
        return EmplyeeName;
    }

}
