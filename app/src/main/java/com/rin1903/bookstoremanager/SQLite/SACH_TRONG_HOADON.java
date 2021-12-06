package com.rin1903.bookstoremanager.SQLite;

import java.io.Serializable;

public class SACH_TRONG_HOADON implements Serializable {
    private String TenSach;
    private int MaSach, SoLuongconlai,soluongtronghoadon,giaban,thanhtien;

    public SACH_TRONG_HOADON(String tenSach, int maSach, int soLuongconlai, int giaban) {
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

    public int getSoluongtronghoadon() {
         if(this.soluongtronghoadon==0){
             return 1;
         }
         else {
             return soluongtronghoadon;
         }
    }

    public void setSoluongtronghoadon(int soluongtronghoadon) {
        this.soluongtronghoadon = soluongtronghoadon;
    }

    public int getThanhtien() {
        if(getGiaban()>0&getSoluongtronghoadon()>0){
            return getGiaban()*getSoluongtronghoadon();
        }
        else return 0;
    }

    public int getGiaban() {
        return giaban;
    }

    public void setGiaban(int giaban) {
        this.giaban = giaban;
    }
}
