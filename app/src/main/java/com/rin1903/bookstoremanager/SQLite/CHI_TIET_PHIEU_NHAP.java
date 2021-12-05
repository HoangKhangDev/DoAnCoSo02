package com.rin1903.bookstoremanager.SQLite;

import java.io.Serializable;

public class CHI_TIET_PHIEU_NHAP implements Serializable {
    public String getTENBANG(){
        return "CHI_TIET_PHIEU_NHAP";
    }
    public String SQL_createtable(){
        return  "create table if not exists CHI_TIET_PHIEU_NHAP (" +
                "   MASACH               int                  not null," +
                "   MAPHIEUNHAP          char(50)             not null," +
                "   SOLUONG_PN           int                  null," +
                "   constraint PK_CHI_TIET_PHIEU_NHAP primary key (MASACH, MAPHIEUNHAP)" +
                ")";
    }

    public CHI_TIET_PHIEU_NHAP(int MASACH, String MAPHIEUNHAP) {
        this.MASACH = MASACH;
        this.MAPHIEUNHAP = MAPHIEUNHAP;
    }

    public CHI_TIET_PHIEU_NHAP() {
    }

    public CHI_TIET_PHIEU_NHAP(int MASACH, String MAPHIEUNHAP, int SOLUONG_PN) {
        this.MASACH = MASACH;
        this.MAPHIEUNHAP = MAPHIEUNHAP;
        this.SOLUONG_PN = SOLUONG_PN;
    }

    public int getMASACH() {
        return MASACH;
    }

    public void setMASACH(int MASACH) {
        this.MASACH = MASACH;
    }

    public String getMAPHIEUNHAP() {
        return MAPHIEUNHAP;
    }

    public void setMAPHIEUNHAP(String MAPHIEUNHAP) {
        this.MAPHIEUNHAP = MAPHIEUNHAP;
    }

    public int getSOLUONG_PN() {
        return SOLUONG_PN;
    }

    public void setSOLUONG_PN(int SOLUONG_PN) {
        this.SOLUONG_PN = SOLUONG_PN;
    }

    private int MASACH;
    private String MAPHIEUNHAP;
    private int SOLUONG_PN;
}
