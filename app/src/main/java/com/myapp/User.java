package com.myapp;

public class User {
    private String iduser;
    private String HoTen;
    private int Point;
    private String Email;
    private String SDT;

    public User() {
        //Nhận data từ Firebase
    }

    public User(String iduser, String hoTen, int point, String email, String SDT) {
        this.iduser = iduser;
        HoTen = hoTen;
        Point = point;
        Email = email;
        this.SDT = SDT;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public int getPoint() {
        return Point;
    }

    public void setPoint(int point) {
        Point = point;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }
}
