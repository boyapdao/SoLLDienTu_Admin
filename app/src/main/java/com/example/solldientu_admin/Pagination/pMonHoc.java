package com.example.solldientu_admin.Pagination;

import com.example.solldientu_admin.object.GiaoVien;
import com.example.solldientu_admin.object.MonHoc;

import java.util.ArrayList;

public class pMonHoc {
    private int page, pageSize, total;
    ArrayList<MonHoc> data;

    public pMonHoc() {
    }

    public pMonHoc(int page, int pageSize, int totalMH, ArrayList<MonHoc> data) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = totalMH;
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

    public int getTotalMH() {
        return total;
    }

    public void setTotalMH(int totalMH) {
        this.total = totalMH;
    }

    public ArrayList<MonHoc> getData() {
        return data;
    }

    public void setData(ArrayList<MonHoc> data) {
        this.data = data;
    }
}
