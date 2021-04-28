package com.example.solldientu_admin.Pagination;

import com.example.solldientu_admin.object.GiaoVien;

import java.util.ArrayList;

public class pGiaoVien {
    private int page, pageSize, total;
    ArrayList<GiaoVien> data;

    public pGiaoVien(int page, int pageSize, int total, ArrayList<GiaoVien> data) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
        this.data = data;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<GiaoVien> getData() {
        return data;
    }

    public void setData(ArrayList<GiaoVien> data) {
        this.data = data;
    }
}
