package com.example.solldientu_admin.Pagination;


import com.example.solldientu_admin.object.SinhVien;

import java.util.ArrayList;

public class pSinhVien {
    private  int page,pageSize,total;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<SinhVien> getData() {
        return data;
    }

    public void setData(ArrayList<SinhVien> data) {
        this.data = data;
    }

    public pSinhVien(int page, int pageSize, int total, ArrayList<SinhVien> data) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
        this.data = data;
    }
}
