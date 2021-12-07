package com.rin1903.bookstoremanager.SQLite;

import java.io.Serializable;

public class SACH_TRONG_PHIEUNHAP implements Serializable {
    private String TenSach;
    private int MaSach, SoLuongconlai,soluongtrongphieunhap,giaban,thanhtien;

    public SACH_TRONG_PHIEUNHAP(String tenSach, int maSach, int soLuongconlai, int giaban) {
        TenSach = tenSach;
        MaSach = maSach;
        SoLuongconlai = soLuongconlai;
        this.giaban = giaban;
    }

    public String getTenSach() {
        return TenSach;
    }

    public void setTenSach(String tenSach) {
        TenSach = tenSach;
    }

    public int getMaSach() {
        return MaSach;
    }

    public void setMaSach(int maSach) {
        MaSach = maSach;
    }

    public int getSoLuongconlai() {
        return SoLuongconlai;
    }

    public void setSoLuongconlai(int soLuongconlai) {
        SoLuongconlai = soLuongconlai;
    }

    public int getsoluongtrongphieunhap() {
        return soluongtrongphieunhap;
    }

    public void setsoluongtrongphieunhap(int soluongtrongphieunhap) {
        this.soluongtrongphieunhap = soluongtrongphieunhap;
    }

    public int getGiaban() {
        return giaban;
    }

    public void setGiaban(int giaban) {
        this.giaban = giaban;
    }

    public int getThanhtien() {
        if(giaban>0&soluongtrongphieunhap>0)
        {
            return soluongtrongphieunhap*giaban;
        }
        else {
            return 0;
        }
    }

    public void setThanhtien(int thanhtien) {
        this.thanhtien = thanhtien;
    }
}
