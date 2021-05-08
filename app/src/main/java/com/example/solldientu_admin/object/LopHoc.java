package com.example.solldientu_admin.object;

public class LopHoc {
    private String maLop,tenLop,maGv;

    public LopHoc() {
    }

    public LopHoc(String tenLop, String maGv) {
        this.tenLop = tenLop;
        this.maGv = maGv;
    }

    public LopHoc(String maLop, String tenLop, String maGv) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.maGv = maGv;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public String getMaGv() {
        return maGv;
    }

    public void setMaGv(String maGv) {
        this.maGv = maGv;
    }

    @Override
    public String toString() {
        return maLop+" - "+tenLop;
    }
}
