package com.example.solldientu_admin.Pagination;

import com.example.solldientu_admin.object.LopHoc;

import java.util.ArrayList;

public class pLopHoc {
    private  int page,pageSize,total;
    ArrayList<LopHoc> data;

    public pLopHoc(int page, int pageSize, int totalLop, ArrayList<LopHoc> data) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = totalLop;
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

    public ArrayList<LopHoc> getData() {
        return data;
    }

    public void setData(ArrayList<LopHoc> data) {
        this.data = data;
    }
}
