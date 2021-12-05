package com.rin1903.bookstoremanager.SQLite;



public class TACGIA {
    public String SQL_createtable(){
        return "create table if not exists TACGIA (" +
                "   MATACGIA             varchar(100)         not null," +
                "   TENTACGIA            varchar(200)         null," +
                "   GIOITINH_TG          varchar(3)           null," +
                "   NGAYSINH_TG          char(50)             null," +
                "   DIACHI_TG            varchar(100)         null," +
                " HINH_TACGIA            BLOB         null,"+
                "   constraint PK_TACGIA primary key (MATACGIA)" +
                ")";
    }
    public String getTENBANG(){
        return "TACGIA";
    }

    public TACGIA(String MATACGIA) {
        this.MATACGIA = MATACGIA;
    }

    public String getMATACGIA() {
        return MATACGIA;
    }

    public void setMATACGIA(String MATACGIA) {
        this.MATACGIA = MATACGIA;
    }

    public String getTENTACGIA() {
        return TENTACGIA;
    }

    public void setTENTACGIA(String TENTACGIA) {
        this.TENTACGIA = TENTACGIA;
    }

    public String getGIOITINH_TG() {
        return GIOITINH_TG;
    }

    public void setGIOITINH_TG(String GIOITINH_TG) {
        this.GIOITINH_TG = GIOITINH_TG;
    }

    public String getNGAYSINH_TG() {
        return NGAYSINH_TG;
    }

    public void setNGAYSINH_TG(String NGAYSINH_TG) {
        this.NGAYSINH_TG = NGAYSINH_TG;
    }

    public String getDIACHI_TG() {
        return DIACHI_TG;
    }

    public void setDIACHI_TG(String DIACHI_TG) {
        this.DIACHI_TG = DIACHI_TG;
    }

    public byte[] getHINH_TACGIA() {
        return HINH_TACGIA;
    }

    public void setHINH_TACGIA(byte[] HINH_TACGIA) {
        this.HINH_TACGIA = HINH_TACGIA;
    }

    public TACGIA(String MATACGIA, String TENTACGIA, String GIOITINH_TG, String NGAYSINH_TG, String DIACHI_TG, byte[] HINH_TACGIA) {
        this.MATACGIA = MATACGIA;
        this.TENTACGIA = TENTACGIA;
        this.GIOITINH_TG = GIOITINH_TG;
        this.NGAYSINH_TG = NGAYSINH_TG;
        this.DIACHI_TG = DIACHI_TG;
        this.HINH_TACGIA = HINH_TACGIA;
    }

    public TACGIA() {
    }

    private String MATACGIA,TENTACGIA,GIOITINH_TG;
    private String NGAYSINH_TG;
    private String DIACHI_TG;
    private byte[] HINH_TACGIA;


}
