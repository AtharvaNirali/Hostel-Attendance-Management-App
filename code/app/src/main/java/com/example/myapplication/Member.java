package com.example.myapplication;

import android.util.Log;

public class Member
{
    private String Name,Email,Phone,ParentPhone,PermanentAddr,LocalPhone,LocalAddr,token_id,Image_uri,HostelID;
    Boolean status;

/*    public Member(String email,String phone,String token)
    {
        Email = email;
        Phone = phone;
        token_id = token;
    } */


    public Member(String hostelid,String name, String email, String phone, String parentPhone, String permanentAddr, String localPhone, String localAddr, String token, String image_uri, Boolean status) {
        HostelID = hostelid;
        Name = name;
        Email = email;
        Phone = phone;
        ParentPhone = parentPhone;
        PermanentAddr = permanentAddr;
        LocalPhone = localPhone;
        LocalAddr = localAddr;
        token_id = token;
        Image_uri = image_uri;
        status = status;
        Log.e("Member","CALL ALA");
    }

    public Member() {}

    public String getHostelID() {
        return HostelID;
    }

    public void setHostelID(String hostelID) {
        HostelID = hostelID;
    }


    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getImage_uri() {
        return Image_uri;
    }

    public void setImage_uri(String image_uri) {
        Image_uri = image_uri;
    }

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getParentPhone() {
        return ParentPhone;
    }

    public void setParentPhone(String parentPhone) {
        ParentPhone = parentPhone;
    }

    public String getPermanentAddr() {
        return PermanentAddr;
    }

    public void setPermanentAddr(String permanentAddr) {
        PermanentAddr = permanentAddr;
    }

    public String getLocalPhone() {
        return LocalPhone;
    }

    public void setLocalPhone(String localPhone) {
        LocalPhone = localPhone;
    }

    public String getLocalAddr() {
        return LocalAddr;
    }

    public void setLocalAddr(String localAddr) {
        LocalAddr = localAddr;
    }
}
