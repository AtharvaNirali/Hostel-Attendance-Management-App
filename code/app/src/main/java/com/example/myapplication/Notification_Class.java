package com.example.myapplication;

import android.util.Log;

public class Notification_Class
{
    String Subject,Message,Date,Time,Name,Days,Userid,DateApproved,Dateid;
    int Status;
    int Attachments;


    //USER SIDE
    public Notification_Class(String name,String subject, String message, String dateFromWhenStudentNotInHostel, String days,int status, String dateWhenApprovedByAdmimn, String timeWhenApprovedByAdmin){
        Log.e("classUser","Cntructor call");

        Name = name;
        Subject = subject;
        Message = message;
        Date = dateFromWhenStudentNotInHostel;
        Days = days;
        Status = status;
        DateApproved = dateWhenApprovedByAdmimn;
        Time = timeWhenApprovedByAdmin;
    }

    //ADMIN SIDE
    public Notification_Class(String name, String subject, String message, String dateFromWhenStudentNotInHostel, String days,String userid,int status,String dateid){
        Log.e("class","Cntructor call");
        Name = name;
        Subject = subject;
        Message = message;
        Date = dateFromWhenStudentNotInHostel;
        Days = days;
        Status = status;
        Userid = userid;
        Dateid = dateid;
    }

    //ADMIN SIDE
    public Notification_Class( String subject, String message, String dateFromWhenStudentNotInHostel, String days){
        Log.e("class","Cntructor call");
        Subject = subject;
        Message = message;
        Date = dateFromWhenStudentNotInHostel;
        Days = days;
    }


    public String getDateid() {
        return Dateid;
    }

    public void setDateid(String dateid) {
        Dateid = dateid;
    }

    public String getDateApproved() {
        return DateApproved;
    }

    public void setDateApproved(String dateApproved) {
        DateApproved = dateApproved;
    }

    public String getUserId() {
        return Userid;
    }

    public void setUserId(String id) {
        this.Userid = id;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getDays() {
        return Days;
    }

    public void setDays(String days) {
        Days = days;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public int getAttachments() {
        return Attachments;
    }

    public void setAttachments(int attachments) {
        Attachments = attachments;
    }
}
