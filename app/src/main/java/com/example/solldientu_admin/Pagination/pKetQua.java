package com.example.solldientu_admin.Pagination;

import java.util.HashMap;

public class pKetQua {
    String maSv, maMh;
    HashMap<String, String> hm=new HashMap<>();

    public pKetQua(String maSv, String maMh) {
        this.maSv = maSv;
        this.maMh = maMh;
        this.hm.put("maSv", maSv);
        this.hm.put("maMh", maMh);
    }

    public String getMaSv() {
        return maSv;
    }

    public void setMaSv(String maSv) {
        this.maSv = maSv;
    }

    public String getMaMh() {
        return maMh;
    }

    public void setMaMh(String maMh) {
        this.maMh = maMh;
    }

    public HashMap<String, String> getHm() {
        return hm;
    }

    public void setHm(HashMap<String, String> hm) {
        this.hm = hm;
    }
}
