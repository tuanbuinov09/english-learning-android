package com.myapp.learnenglish.fragment.ranking;

public class UserRanking {

    private String hoTen;
    private String email;
    private String sdt;
    private long point;
    private String iduser;

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }
}
