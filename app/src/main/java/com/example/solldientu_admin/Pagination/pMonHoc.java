package com.example.solldientu_admin.Pagination;

import com.example.solldientu_admin.object.GiaoVien;
import com.example.solldientu_admin.object.MonHoc;

import java.util.ArrayList;

public class pMonHoc {
    private int page, pageSize, total;
    ArrayList<MonHoc> data;

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

    public ArrayList<MonHoc> getData() {
        return data;
    }

    public void setData(ArrayList<MonHoc> data) {
        this.data = data;
    }

    public pMonHoc(int page, int pageSize, int total, ArrayList<MonHoc> data) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
        this.data = data;
    }

    public pMonHoc() {

    }


}
