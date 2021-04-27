package com.example.solldientu_admin.object;

import java.util.Date;

public class GiaoVien {
    private String maGv, tenGv, queQuan, anh;
    private String ngaySinh;
    private int gioiTinh;

    public GiaoVien() {
    }

    public GiaoVien(String maGv, String tenGv, String queQuan, String anh, String ngaySinh, int gioiTinh) {
        this.maGv = maGv;
        this.tenGv = tenGv;
        this.queQuan = queQuan;
        this.anh = anh;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
    }

    public GiaoVien(String tenGv, String queQuan, String anh, String ngaySinh, int gioiTinh) {
        this.tenGv = tenGv;
        this.queQuan = queQuan;
        this.anh = anh;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
    }


    public String getMaGv() {
        return maGv;
    }

    public void setMaGv(String maGv) {
        this.maGv = maGv;
    }

    public String getTenGv() {
        return tenGv;
    }

    public void setTenGv(String tenGv) {
        this.tenGv = tenGv;
    }

    public String getTenGV() {
        return tenGv;
    }

    public void setTenGV(String tenGV) {
        this.tenGv = tenGV;
    }

    public String getQueQuan() {
        return queQuan;
    }

    public void setQueQuan(String queQuan) {
        this.queQuan = queQuan;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public int getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(int gioiTinh) {
        this.gioiTinh = gioiTinh;
    }
}
