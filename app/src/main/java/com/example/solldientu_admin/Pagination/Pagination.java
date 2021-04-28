package com.example.solldientu_admin.Pagination;

import java.util.HashMap;

public class Pagination {
    int page, pageSize;

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

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public HashMap<String, String> getHm() {
        return hm;
    }

    public void setHm(HashMap<String, String> hm) {
        this.hm = hm;
    }

    String ten;
    HashMap<String, String> hm = new HashMap();

    public Pagination(int page, int pageSize, String Ten) {
        this.page = page;
        this.pageSize = pageSize;
        hm.put("page", page + "");
        hm.put("pageSize", pageSize + "");
        hm.put("ten", Ten);
    }


}
