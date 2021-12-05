package com.rin1903.bookstoremanager.SQLite;

import java.io.Serializable;

public class
CHI_TIET_HOA_DON implements Serializable {
    public int getMASACH() {
        return MASACH;
    }
    public String SQL_Createtable(){
        return "create table if not exists CHI_TIET_HOA_DON ("+
                "MASACH               int                  not null,"+
                "MAHOADON             char(50)             not null,"+
                "SOLUONG_HD           int                  null,"+
                "constraint PK_CHI_TIET_HOA_DON primary key (MASACH, MAHOADON))";
    }

    public void setMASACH(int MASACH) {
        this.MASACH = MASACH;
    }

    public String getMAHOADON() {
        return MAHOADON;
    }
    public String getTENBANG(){
        return "CHI_TIET_HOA_DON";
    }
    public void setMAHOADON(String MAHOADON) {
        this.MAHOADON = MAHOADON;
    }

    public int getSOLUONG_HD() {
        return SOLUONG_HD;
    }

    public void setSOLUONG_HD(int SOLUONG_HD) {
        this.SOLUONG_HD = SOLUONG_HD;
    }

    public CHI_TIET_HOA_DON(int MASACH, String MAHOADON, int SOLUONG_HD) {
        this.MASACH = MASACH;
        this.MAHOADON = MAHOADON;
        this.SOLUONG_HD = SOLUONG_HD;
    }

    public CHI_TIET_HOA_DON() {
    }

    public CHI_TIET_HOA_DON(int MASACH, String MAHOADON) {
        this.MASACH = MASACH;
        this.MAHOADON = MAHOADON;
    }

    private int MASACH;
    private String MAHOADON;
    private int SOLUONG_HD;
}

