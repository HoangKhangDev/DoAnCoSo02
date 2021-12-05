package com.rin1903.bookstoremanager.SQLite;

import java.io.Serializable;


public class KHACHHANG implements Serializable {
    public String getTENBANG(){
        return "KHACHHANG";
    }
    public String SQL_createtable(){
        return "create table if not exists KHACHHANG (" +
                "   MAKHACHHANG          char(50)             not null," +
                "   TENKHACHHANG         varchar(100)         null," +
                "   GIOITINH_KH          char(3)              null," +
                "   NGAYSINH_KH          char(50)             null," +
                "   SDT_KH               char(10)          null," +
                "   DIACHI_KH            varchar(100)         null," +
                " HINH_KHACHHANG            BLOB         null,"+
                "   constraint PK_KHACHHANG primary key (MAKHACHHANG)" +
                ")";
    }


    public String getMAKHACHHANG() {
        return MAKHACHHANG;
    }

    public void setMAKHACHHANG(String MAKHACHHANG) {
        this.MAKHACHHANG = MAKHACHHANG;
    }

    public String getTENKHACHHANG() {
        return TENKHACHHANG;
    }

    public void setTENKHACHHANG(String TENKHACHHANG) {
        this.TENKHACHHANG = TENKHACHHANG;
    }

    public String getGIOITINH_KH() {
        return GIOITINH_KH;
    }

    public void setGIOITINH_KH(String GIOITINH_KH) {
        this.GIOITINH_KH = GIOITINH_KH;
    }

    public String getNGAYSINH_KH() {
        return NGAYSINH_KH;
    }

    public void setNGAYSINH_KH(String NGAYSINH_KH) {
        this.NGAYSINH_KH = NGAYSINH_KH;
    }

    public String getSDT_KH() {
        return SDT_KH;
    }

    public void setSDT_KH(String SDT_KH) {
        this.SDT_KH = SDT_KH;
    }

    public String getDIACHI_KH() {
        return DIACHI_KH;
    }

    public void setDIACHI_KH(String DIACHI_KH) {
        this.DIACHI_KH = DIACHI_KH;
    }

    public byte[] getHINH_KH() {
        return HINH_KH;
    }

    public void setHINH_KH(byte[] HINH_KH) {
        this.HINH_KH = HINH_KH;
    }

    public KHACHHANG(String MAKHACHHANG) {
        this.MAKHACHHANG = MAKHACHHANG;
    }

    public KHACHHANG(String MAKHACHHANG, String TENKHACHHANG, String GIOITINH_KH, String NGAYSINH_KH, String SDT_KH, String DIACHI_KH, byte[] HINH_KH) {
        this.MAKHACHHANG = MAKHACHHANG;
        this.TENKHACHHANG = TENKHACHHANG;
        this.GIOITINH_KH = GIOITINH_KH;
        this.NGAYSINH_KH = NGAYSINH_KH;
        this.SDT_KH = SDT_KH;
        this.DIACHI_KH = DIACHI_KH;
        this.HINH_KH = HINH_KH;
    }

    private String MAKHACHHANG,TENKHACHHANG,GIOITINH_KH;
    private String NGAYSINH_KH;
    private String SDT_KH,DIACHI_KH;
    private byte[] HINH_KH;

    public KHACHHANG() {
    }
}

