package com.example.solldientu_admin.Pagination;

import com.example.solldientu_admin.object.GiaoVien;

import java.util.ArrayList;

public class pGiaoVien {
    private int page, pageSize, totalGV;
    ArrayList<GiaoVien> data;

    public pGiaoVien(int page, int pageSize, int totalGv, ArrayList<GiaoVien> data) {
        this.page = page;
        this.pageSize = pageSize;
        this.totalGV = totalGv;
        this.data = data;
    }

    public pGiaoVien() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalGV() {
        return totalGV;
    }

    public void setTotalGV(int totalGV) {
        this.totalGV = totalGV;
    }

    public ArrayList<GiaoVien> getData() {
        return data;
    }

    public void setData(ArrayList<GiaoVien> data) {
        this.data = data;
    }
}
