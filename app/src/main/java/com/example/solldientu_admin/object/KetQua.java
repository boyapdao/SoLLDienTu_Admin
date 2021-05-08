package com.example.solldientu_admin.object;

public class KetQua {
    private String maSv, maMh;
    private int diemLd, diemTl;

    public KetQua(String maSv, String maMh, int diemLd, int diemTl) {
        this.maSv = maSv;
        this.maMh = maMh;
        this.diemLd = diemLd;
        this.diemTl = diemTl;
    }

    public String getMaSv() {
        return maSv;
    }

    public void setMaSv(String maSv) {
        this.maSv = maSv;
    }

    public String getMaMh() {
        return maMh;
    }

    public void setMaMh(String maMh) {
        this.maMh = maMh;
    }

    public int getDiemLd() {
        return diemLd;
    }

    public void setDiemLd(int diemLd) {
        this.diemLd = diemLd;
    }

    public int getDiemTl() {
        return diemTl;
    }

    public void setDiemTl(int diemTl) {
        this.diemTl = diemTl;
    }
}
