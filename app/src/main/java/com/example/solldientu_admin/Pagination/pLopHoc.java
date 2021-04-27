package com.example.solldientu_admin.Pagination;

import com.example.solldientu_admin.object.LopHoc;

import java.util.ArrayList;

public class pLopHoc {
    private  int page,pageSize,totalLop;
    ArrayList<LopHoc> data;

    public pLopHoc(int page, int pageSize, int totalLop, ArrayList<LopHoc> data) {
        this.page = page;
        this.pageSize = pageSize;
        this.totalLop = totalLop;
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

    public int getTotalLop() {
        return totalLop;
    }

    public void setTotalLop(int totalLop) {
        this.totalLop = totalLop;
    }

    public ArrayList<LopHoc> getData() {
        return data;
    }

    public void setData(ArrayList<LopHoc> data) {
        this.data = data;
    }
}
