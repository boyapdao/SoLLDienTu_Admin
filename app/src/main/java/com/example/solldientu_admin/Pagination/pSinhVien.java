package com.example.solldientu_admin.Pagination;


import com.example.solldientu_admin.object.SinhVien;

import java.util.ArrayList;

public class pSinhVien {
    private  int page,pageSize,totalLop;
    ArrayList<SinhVien> data;

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

    public int getTotalLop() {
        return totalLop;
    }

    public void setTotalLop(int totalLop) {
        this.totalLop = totalLop;
    }

    public ArrayList<SinhVien> getData() {
        return data;
    }

    public void setData(ArrayList<SinhVien> data) {
        this.data = data;
    }

    public pSinhVien(int page, int pageSize, int totalLop, ArrayList<SinhVien> data) {
        this.page = page;
        this.pageSize = pageSize;
        this.totalLop = totalLop;
        this.data = data;
    }
}
