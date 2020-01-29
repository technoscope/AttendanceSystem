package com.example.dell.attendancesystem.myapplication.AdminRole.EmployeeRecord;

public class model {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCd() {
        return cd;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getEmpPID() {
        return empPID;
    }

    public void setEmpPID(String empPID) {
        this.empPID = empPID;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    String name;
    String phoneno;
    String email;
    String cd;
    String cnic;
    String empPID;
    String gender;
    String Designation;
    String fname;
    String dob;
    String scale;
    String address;
    String joiningdate;
    String province;
    String district;
    String tehsil;
    String unioncouncil;
    String village1;

    public model(){}

    public model(String name, String phoneno, String email, String cd, String cnic, String empPID, String gender, String designation, String fname, String dob, String scale, String address, String joiningdate, String province, String district, String tehsil, String unioncouncil, String village1) {
        this.name = name;
        this.phoneno = phoneno;
        this.email = email;
        this.cd = cd;
        this.cnic = cnic;
        this.empPID = empPID;
        this.gender = gender;
        Designation = designation;
        this.fname = fname;
        this.dob = dob;
        this.scale = scale;
        this.address = address;
        this.joiningdate = joiningdate;
        this.province = province;
        this.district = district;
        this.tehsil = tehsil;
        this.unioncouncil = unioncouncil;
        this.village1 = village1;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJoiningdate() {
        return joiningdate;
    }

    public void setJoiningdate(String joiningdate) {
        this.joiningdate = joiningdate;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTehsil() {
        return tehsil;
    }

    public void setTehsil(String tehsil) {
        this.tehsil = tehsil;
    }

    public String getUnioncouncil() {
        return unioncouncil;
    }

    public void setUnioncouncil(String unioncouncil) {
        this.unioncouncil = unioncouncil;
    }

    public String getVillage1() {
        return village1;
    }

    public void setVillage1(String village1) {
        this.village1 = village1;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }
}
