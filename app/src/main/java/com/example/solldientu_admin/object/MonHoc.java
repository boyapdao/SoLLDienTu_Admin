package com.example.solldientu_admin.object;

public class MonHoc {
    private String maMh, tenMh;
    private int soTc,kyhoc;

    public MonHoc(String maMh, String tenMh, int soTc, int kyhoc) {
        this.maMh = maMh;
        this.tenMh = tenMh;
        this.soTc = soTc;
        this.kyhoc = kyhoc;
    }

    public MonHoc(String tenMh, int soTc, int kyhoc) {
        this.tenMh = tenMh;
        this.soTc = soTc;
        this.kyhoc = kyhoc;
    }

    public MonHoc() {
    }

    public String getMaMh() {
        return maMh;
    }

    public void setMaMh(String maMh) {
        this.maMh = maMh;
    }

    public String getTenMh() {
        return tenMh;
    }

    public void setTenMh(String tenMh) {
        this.tenMh = tenMh;
    }

    public int getSoTc() {
        return soTc;
    }

    public void setSoTc(int soTc) {
        this.soTc = soTc;
    }

    public int getKyhoc() {
        return kyhoc;
    }

    public void setKyhoc(int kyhoc) {
        this.kyhoc = kyhoc;
    }
}
