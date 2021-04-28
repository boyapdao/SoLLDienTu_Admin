package com.example.solldientu_admin.object;

public class SinhVien {
    private String maSv,tenSv,ngaySinh;
    private int gioiTinh;
    private String thuongTru,tamTru,sdt,anh,maLop;

    public SinhVien(String tenSv, String ngaySinh, int gioiTinh, String thuongTru, String tamTru, String sdt, String anh, String maLop) {
        this.tenSv = tenSv;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.thuongTru = thuongTru;
        this.tamTru = tamTru;
        this.sdt = sdt;
        this.anh = anh;
        this.maLop = maLop;
    }

    public String getMaSv() {
        return maSv;
    }

    public void setMaSv(String maSv) {
        this.maSv = maSv;
    }

    public String getTenSv() {
        return tenSv;
    }

    public void setTenSv(String tenSv) {
        this.tenSv = tenSv;
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

    public String getThuongTru() {
        return thuongTru;
    }

    public void setThuongTru(String thuongTru) {
        this.thuongTru = thuongTru;
    }

    public String getTamTru() {
        return tamTru;
    }

    public void setTamTru(String tamTru) {
        this.tamTru = tamTru;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public SinhVien(String maSv, String tenSv, String ngaySinh, int gioiTinh, String thuongTru, String tamTru, String sdt, String anh, String maLop) {
        this.maSv = maSv;
        this.tenSv = tenSv;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.thuongTru = thuongTru;
        this.tamTru = tamTru;
        this.sdt = sdt;
        this.anh = anh;
        this.maLop = maLop;
    }
}
