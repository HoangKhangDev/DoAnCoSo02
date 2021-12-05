package com.rin1903.bookstoremanager.SQLite;

import java.io.Serializable;

public class NHACUNGCAP implements Serializable {
    public String SQL_createtable(){
        return "create table if not exists NHACUNGCAP (" +
                "   MANHACUNGCAP         char(50)             not null," +
                "   TENNHACUNGCAP        varchar(100)         null," +
                "   DIACHI_NCC           varchar(100)         null," +
                "   SDT_NCC              char(50)             null," +
                " HINH_NHACUNGCAP            BLOB         null,"+
                "   constraint PK_NHACUNGCAP primary key (MANHACUNGCAP)" +
                ")";
    }
    public String get_TENBANG(){
        return "NHACUNGCAP";
    }
    private String MANHACUNGCAP,TENNHACUNGCAP,DIACHI_NCC,SDT_NCC;

    public byte[] getHINH_NHACUNGCAP() {
        return HINH_NHACUNGCAP;
    }

    public void setHINH_NHACUNGCAP(byte[] HINH_NHACUNGCAP) {
        this.HINH_NHACUNGCAP = HINH_NHACUNGCAP;
    }

    private byte[] HINH_NHACUNGCAP;

    public String getMANHACUNGCAP() {
        return MANHACUNGCAP;
    }

    public void setNHACUNGCAP(String MANHACUNGCAP) {
        this.MANHACUNGCAP = MANHACUNGCAP;
    }

    public String getTENNHACUNGCAP() {
        return TENNHACUNGCAP;
    }

    public void setTENNHACUNGCAP(String TENNHACUNGCAP) {
        this.TENNHACUNGCAP = TENNHACUNGCAP;
    }

    public String getDIACHI_NCC() {
        return DIACHI_NCC;
    }

    public void setDIACHI_NCC(String DIACHI_NCC) {
        this.DIACHI_NCC = DIACHI_NCC;
    }

    public String getSDT_NCC() {
        return SDT_NCC;
    }

    public void setSDT_NCC(String SDT_NCC) {
        this.SDT_NCC = SDT_NCC;
    }

    public NHACUNGCAP() {
    }

    public NHACUNGCAP(String MANHACUNGCAP) {
        this.MANHACUNGCAP = MANHACUNGCAP;
    }

    public NHACUNGCAP(String MANHACUNGCAP, String TENNHACUNGCAP, String DIACHI_NCC, String SDT_NCC, byte[] HINH_NHACUNGCAP) {
        this.MANHACUNGCAP = MANHACUNGCAP;
        this.TENNHACUNGCAP = TENNHACUNGCAP;
        this.DIACHI_NCC = DIACHI_NCC;
        this.SDT_NCC = SDT_NCC;
        this.HINH_NHACUNGCAP=HINH_NHACUNGCAP;
    }
}
