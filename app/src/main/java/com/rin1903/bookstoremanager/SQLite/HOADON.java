package com.rin1903.bookstoremanager.SQLite;

import java.io.Serializable;

public class HOADON implements Serializable {
    public String getTENBANG(){
        return "HOADON";
    }
    public String SQL_createtable(){
        return "create table if not exists HOADON (" +
                "   MAHOADON             char(50)             not null," +
                "   MAKHACHHANG          char(50)             not null," +
                "   THANHTIEN_CTHD       numeric(10)          null," +
                "   NGAY_HD           DATETIME SECOND             null," +
                "   SOLUONG_HD           int                  null," +
                "   constraint PK_HOADON primary key (MAHOADON)" +
                ")";
    }

    public HOADON() {
    }

    public HOADON(String MAHOADON, String MAKHACHHANG) {
        this.MAHOADON = MAHOADON;
        this.MAKHACHHANG = MAKHACHHANG;
    }

    public String getMAHOADON() {
        return MAHOADON;
    }

    public void setMAHOADON(String MAHOADON) {
        this.MAHOADON = MAHOADON;
    }

    public String getMAKHACHHANG() {
        return MAKHACHHANG;
    }

    public void setMAKHACHHANG(String MAKHACHHANG) {
        this.MAKHACHHANG = MAKHACHHANG;
    }

    public int getTHANHTIEN_CTHD() {
        return THANHTIEN_CTHD;
    }

    public void setTHANHTIEN_CTHD(int THANHTIEN_CTHD) {
        this.THANHTIEN_CTHD = THANHTIEN_CTHD;
    }

    public String getNGAY_HD() {
        return NGAY_HD;
    }

    public void setNGAY_HD(String NGAY_HD) {
        this.NGAY_HD = NGAY_HD;
    }

    public int getSOLUONG_HD() {
        return SOLUONG_HD;
    }

    public void setSOLUONG_HD(int SOLUONG_HD) {
        this.SOLUONG_HD = SOLUONG_HD;
    }

    public HOADON(String MAHOADON, String MAKHACHHANG, int THANHTIEN_CTHD, String NGAY_HD, int SOLUONG_HD) {
        this.MAHOADON = MAHOADON;
        this.MAKHACHHANG = MAKHACHHANG;
        this.THANHTIEN_CTHD = THANHTIEN_CTHD;
        this.NGAY_HD = NGAY_HD;
        this.SOLUONG_HD = SOLUONG_HD;
    }

    private String MAHOADON,MAKHACHHANG;
    private int THANHTIEN_CTHD;
    private String NGAY_HD;
    private int SOLUONG_HD;
}

