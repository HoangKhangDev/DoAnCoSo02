package com.rin1903.bookstoremanager.SQLite;


import java.io.Serializable;

public class PHIEUNHAP implements Serializable {
    public String SQL_createtable(){
        return "create table if not exists PHIEUNHAP (" +
                "   MAPHIEUNHAP          char(50)             not null," +
                "   MANHACUNGCAP         char(50)             not null," +
                "   NGAY_PN              DATETIME SECOND              null," +
                "   THANHTIEN_PN         int          null," +
                "   constraint PK_PHIEUNHAP primary key (MAPHIEUNHAP)" +
                ")";
    }
    private String getTENBANG(){
        return "PHIEUNHAP";
    }
    private String MAPHIEUNHAP,MANHACUNGCAP;
    private String NGAY_PN;
    private int SOLUONG_PN,THANHTIEN_PN;

    public String getMAPHIEUNHAP() {
        return MAPHIEUNHAP;
    }

    public void setMAPHIEUNHAP(String MAPHIEUNHAP) {
        this.MAPHIEUNHAP = MAPHIEUNHAP;
    }

    public String getMANHACUNGCAP() {
        return MANHACUNGCAP;
    }

    public void setMANHACUNGCAP(String MANHACUNGCAP) {
        this.MANHACUNGCAP = MANHACUNGCAP;
    }

    public String getNGAY_PN() {
        return NGAY_PN;
    }

    public void setNGAY_PN(String NGAY_PN) {
        this.NGAY_PN = NGAY_PN;
    }


    public int getTHANHTIEN_PN() {
        return THANHTIEN_PN;
    }

    public void setTHANHTIEN_PN(int THANHTIEN_PN) {
        this.THANHTIEN_PN = THANHTIEN_PN;
    }

    public PHIEUNHAP() {
    }

    public PHIEUNHAP(String MAPHIEUNHAP, String MANHACUNGCAP) {
        this.MAPHIEUNHAP = MAPHIEUNHAP;
        this.MANHACUNGCAP = MANHACUNGCAP;
    }

    public PHIEUNHAP(String MAPHIEUNHAP, String MANHACUNGCAP, String NGAY_PN, int THANHTIEN_PN) {
        this.MAPHIEUNHAP = MAPHIEUNHAP;
        this.MANHACUNGCAP = MANHACUNGCAP;
        this.NGAY_PN = NGAY_PN;
        this.THANHTIEN_PN = THANHTIEN_PN;
    }
}
