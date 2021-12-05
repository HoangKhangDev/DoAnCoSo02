package com.rin1903.bookstoremanager.SQLite;

import java.io.Serializable;

public class THELOAI implements Serializable {
    public String getTENBANG(){
        return "THELOAI";
    }
    public String SQL_createtable(){
        return "create table if not exists THELOAI (" +
                "   MALOAI               char(50)             not null," +
                "   TENLOAI              varchar(100)         null," +
                "   constraint PK_THELOAI primary key (MALOAI)" +
                ")";
    }

    public THELOAI(String MALOAI, String TENLOAI) {
        this.MALOAI = MALOAI;
        this.TENLOAI = TENLOAI;
    }

    public THELOAI(String MALOAI) {
        this.MALOAI = MALOAI;
    }

    public THELOAI() {
    }

    public String getMALOAI() {
        return MALOAI;
    }

    public void setMALOAI(String MALOAI) {
        this.MALOAI = MALOAI;
    }

    public String getTENLOAI() {
        return TENLOAI;
    }

    public void setTENLOAI(String TENLOAI) {
        this.TENLOAI = TENLOAI;
    }

    private String MALOAI,TENLOAI;


}
