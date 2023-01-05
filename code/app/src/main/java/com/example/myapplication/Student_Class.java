package com.example.myapplication;

import android.util.Log;

public class Student_Class {

    String name,phone;
    boolean status;
    int permit;

    public Student_Class(String name, String phone, boolean status) {
        Log.d("Construtor","CALL ALA");
        this.name = name;
        this.phone = phone;
        this.status = status;
        this.permit = 0;
    }

    public Student_Class() {}

    public int getPermit() {
        return permit;
    }

    public void setPermit(int permit) {
        this.permit = permit;
        Log.e("Orange","alaClass");

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
