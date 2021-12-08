package com.rin1903.bookstoremanager.SQLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //create/update/insert/delete
    public void  QueryData(String sql){
        SQLiteDatabase database= getWritableDatabase();
        database.execSQL(sql);
    }
    //select
    public Cursor Getdata(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return  database.rawQuery(sql,null);
    }

    public void INSERT_CHITIETHOADON(int MASACH, String MAHOADON, int SOLUONG_HD){
        SQLiteDatabase database= getWritableDatabase();
        String sql="INSERT INTO CHI_TIET_HOA_DON VALUES(?,?,?)";
        SQLiteStatement sqLiteStatement= database.compileStatement(sql);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindLong(1,MASACH);
        sqLiteStatement.bindString(2,MAHOADON);
        sqLiteStatement.bindLong(3,SOLUONG_HD);
        sqLiteStatement.executeInsert();
    }
    public void UPDATE_CHITIETHOADON(CHI_TIET_HOA_DON chi_tiet_hoa_don){
        SQLiteDatabase database= getWritableDatabase();
        String sql="UPDATE CHI_TIET_HOA_DON SET MASACH=?,SOLUONG_HD=? where MAHOADON=?";
        SQLiteStatement sqLiteStatement= database.compileStatement(sql);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindLong(1,chi_tiet_hoa_don.getMASACH());
        sqLiteStatement.bindString(3,chi_tiet_hoa_don.getMAHOADON());
        sqLiteStatement.bindLong(2,chi_tiet_hoa_don.getSOLUONG_HD());
        sqLiteStatement.executeUpdateDelete();
    }
    public void DELETE_CHITIETHOADON(String MAHOADON){
        SQLiteDatabase database= getWritableDatabase();
        database.execSQL("delete from CHI_TIET_HOA_DON where MAHOADON='"+MAHOADON+"'");
    }
    public void INSERT_CHITIETPHIEUNHAP(int MASACH, String MAPHIEUNHAP, int SOLUONG_PN){
        SQLiteDatabase database= getWritableDatabase();
        String sql="INSERT INTO CHI_TIET_PHIEU_NHAP VALUES(?,?,?)";
        SQLiteStatement sqLiteStatement= database.compileStatement(sql);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindLong(1,MASACH);
        sqLiteStatement.bindString(2,MAPHIEUNHAP);
        sqLiteStatement.bindLong(3,SOLUONG_PN);
        sqLiteStatement.executeInsert();
    }
    public void UPDATE_CHITIETPHIEUNHAP(CHI_TIET_PHIEU_NHAP chi_tiet_phieu_nhap){
        SQLiteDatabase database= getWritableDatabase();
        String sql="UPDATE CHI_TIET_PHIEU_NHAP SET MASACH=?,SOLUONG_PN=? where MAPHIEUNHAP=?";
        SQLiteStatement sqLiteStatement= database.compileStatement(sql);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindLong(1,chi_tiet_phieu_nhap.getMASACH());
        sqLiteStatement.bindString(3,chi_tiet_phieu_nhap.getMAPHIEUNHAP());
        sqLiteStatement.bindLong(2,chi_tiet_phieu_nhap.getSOLUONG_PN());
        sqLiteStatement.executeUpdateDelete();
    }
    public void DELETE_CHITIETPHIEUNHAP(String MAPHIEUNHAP)
    {
        SQLiteDatabase database= getWritableDatabase();
        database.execSQL("delete from CHI_TIET_PHIEU_NHAP where MAPHIEUNHAP='"+MAPHIEUNHAP+"'");
    }
    public void INSERT_HOADON(String MAHOADON, String MAKHACHHANG, int THANHTIEN_CTHD){
        SQLiteDatabase database= getWritableDatabase();
        String sql= "INSERT INTO HOADON VALUES(?,?,?,(select datetime('now','location')))";
        SQLiteStatement sqLiteStatement= database.compileStatement(sql);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindString(1,MAHOADON);
        sqLiteStatement.bindString(2,MAKHACHHANG);
        sqLiteStatement.bindLong(3,THANHTIEN_CTHD);
        sqLiteStatement.executeInsert();
    }
    public void UPDATE_HOADON(HOADON hoadon){
        SQLiteDatabase database= getWritableDatabase();
        String sql= "UPDATE HOADON set MAKHACHHANG=?,THANHTIEN_CTHD=? where MAHOADON=?";
        SQLiteStatement sqLiteStatement= database.compileStatement(sql);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindString(3,hoadon.getMAHOADON());
        sqLiteStatement.bindString(1,hoadon.getMAKHACHHANG());
        sqLiteStatement.bindLong(2,hoadon.getTHANHTIEN_CTHD());
        sqLiteStatement.executeUpdateDelete();
    }
    public void DELETE_HOADON(String MAHOADON)
    {
        SQLiteDatabase database=getWritableDatabase();
        database.execSQL("delete from HOADON where MAHOADON='"+MAHOADON+"'");
    }
    public void INSERT_KHACHHANG(String MAKHACHHANG, String TENKHACHHANG, String GIOITINH_KH, String NGAYSINH_KH, String SDT_KH, String DIACHI_KH, byte[] HINH_KH)
    {
        SQLiteDatabase database= getWritableDatabase();
        String sql= "INSERT INTO KHACHHANG VALUES(?,?,?,?,?,?,?)";
        SQLiteStatement sqLiteStatement= database.compileStatement(sql);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindString(1,MAKHACHHANG);
        sqLiteStatement.bindString(2,TENKHACHHANG);
        sqLiteStatement.bindString(3,GIOITINH_KH);
        sqLiteStatement.bindString(4,NGAYSINH_KH);
        sqLiteStatement.bindString(5,SDT_KH);
        sqLiteStatement.bindString(6,DIACHI_KH);
        sqLiteStatement.bindBlob(7  ,HINH_KH);
        sqLiteStatement.executeInsert();
    }
    public void UPDATE_KHACHHANG(KHACHHANG khachhang)
    {
        SQLiteDatabase database= getWritableDatabase();
        String sql= "update KHACHHANG set TENKHACHHANG=?,GIOITINH_KH=?,NGAYSINH_KH=?,SDT_KH=?,DIACHI_KH=?,HINH_KH=? where MAKHACHHANH=?";
        SQLiteStatement sqLiteStatement= database.compileStatement(sql);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindString(7,khachhang.getMAKHACHHANG());
        sqLiteStatement.bindString(1,khachhang.getTENKHACHHANG());
        sqLiteStatement.bindString(2,khachhang.getGIOITINH_KH());
        sqLiteStatement.bindString(3,khachhang.getNGAYSINH_KH());
        sqLiteStatement.bindString(4,khachhang.getSDT_KH());
        sqLiteStatement.bindString(5,khachhang.getDIACHI_KH());
        sqLiteStatement.bindBlob(6,khachhang.getHINH_KH());
        sqLiteStatement.executeUpdateDelete();
    }
    public void DELETE_KHACHHANG(String MAKHACHHANG)
    {
        SQLiteDatabase database= getWritableDatabase();
        database.execSQL("delete from KHACHHANG WHERE MAKHACHHANG='"+MAKHACHHANG+"'");
    }
    public void INSERT_NHACUNGCAP(String MANHACUNGCAP, String TENNHACUNGCAP, String DIACHI_NCC, String SDT_NCC, byte[] HINH_NHACUNGCAP)
    {
        SQLiteDatabase database= getWritableDatabase();
        String sql= "INSERT INTO NHACUNGCAP VALUES(?,?,?,?,?)";
        SQLiteStatement sqLiteStatement= database.compileStatement(sql);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindString(1,MANHACUNGCAP);
        sqLiteStatement.bindString(2,TENNHACUNGCAP);
        sqLiteStatement.bindString(3,DIACHI_NCC);
        sqLiteStatement.bindString(4,SDT_NCC);
        sqLiteStatement.bindBlob(5,HINH_NHACUNGCAP);
        sqLiteStatement.executeInsert();
    }
    public void UPDATE_NHACUNGCAP(NHACUNGCAP nhacungcap)
    {
        SQLiteDatabase database= getWritableDatabase();
        String sql= "UPDATE NHACUNGCAP SET TENNHACUNGCAP=?,DIACHI_NCC=?,SDT_NCC=?,HINH_NHACUNGCAP=? WHERE MANHACUNGCAP='"+nhacungcap.getMANHACUNGCAP()+"'";
        SQLiteStatement sqLiteStatement= database.compileStatement(sql);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindString(1,nhacungcap.getTENNHACUNGCAP());
        sqLiteStatement.bindString(2,nhacungcap.getDIACHI_NCC());
        sqLiteStatement.bindString(3,nhacungcap.getSDT_NCC());
        sqLiteStatement.bindBlob(4,nhacungcap.getHINH_NHACUNGCAP());
        sqLiteStatement.executeUpdateDelete();
    }
    public void DELETE_NHACUNGCAP(String MANHACUNGCAP){
        SQLiteDatabase database= getWritableDatabase();
        database.execSQL("delete from NHACUNGCAP where MANHACUNGCAP='"+MANHACUNGCAP+"'");
    }
    public void INSERT_PHIEUNHAP(String MAPHIEUNHAP, String MANHACUNGCAP, int THANHTIEN_PN){
        SQLiteDatabase database= getWritableDatabase();
        String sql= "INSERT INTO PHIEUNHAP VALUES(?,?,(select datetime('now','location')),?)";
        SQLiteStatement sqLiteStatement= database.compileStatement(sql);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindString(1,MAPHIEUNHAP);
        sqLiteStatement.bindString(2,MANHACUNGCAP);
        sqLiteStatement.bindLong(3,THANHTIEN_PN);
        sqLiteStatement.executeInsert();
    }
    public void UPDATE_PHIEUNHAP(PHIEUNHAP phieunhap){
        SQLiteDatabase database= getWritableDatabase();
        String sql= "UPDATE PHIEUNHAP SET MANHACUNGCAP=?,THANHTIEN_PN=? WHERE MAPHIEUNHAP=?";
        SQLiteStatement sqLiteStatement= database.compileStatement(sql);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindString(3,phieunhap.getMAPHIEUNHAP());
        sqLiteStatement.bindString(1,phieunhap.getMANHACUNGCAP());
        sqLiteStatement.bindLong(2,phieunhap.getTHANHTIEN_PN());
        sqLiteStatement.executeUpdateDelete();
    }
    public void DELETE_PHIEUNHAP(String MAPHIEUNHAP){
        SQLiteDatabase database= getWritableDatabase();
        database.execSQL("delete from PHIEUNHAP where MAPHIEUNHAP='"+MAPHIEUNHAP+"'");
    }
    public void INSERT_SACH(int MASACH, String MALOAI, String MATACGIA, String TENSACH, int SOQUYEN,String TRANGTHAI, int GIABAN, byte[] HINH_SACH)
    {
        SQLiteDatabase database= getWritableDatabase();
        String sql= "INSERT INTO SACH VALUES(?,?,?,?,?,?,?,?)";
        SQLiteStatement sqLiteStatement= database.compileStatement(sql);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindLong(1,MASACH);
        sqLiteStatement.bindString(2,MALOAI);
        sqLiteStatement.bindString(3,MATACGIA);
        sqLiteStatement.bindString(4,TENSACH);
        sqLiteStatement.bindLong(5,SOQUYEN);
        sqLiteStatement.bindString(6,TRANGTHAI);
        sqLiteStatement.bindLong(7,GIABAN);
        sqLiteStatement.bindBlob(8,HINH_SACH);
        sqLiteStatement.executeInsert();
    }
    public void UPDATE_SACH(SACH sach)
    {
        SQLiteDatabase database= getWritableDatabase();
        String sql= "UPDATE SACH SET MALOAI=?,MATACGIA=?,TENSACH=?,SOQUYEN=?,TRANGTHAI=?,GIABAN=?,HINH_SACH=? WHERE MASACH =?";
        SQLiteStatement sqLiteStatement= database.compileStatement(sql);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindLong(8,sach.getMASACH());
        sqLiteStatement.bindString(1,sach.getMALOAI());
        sqLiteStatement.bindString(2,sach.getMATACGIA());
        sqLiteStatement.bindString(3,sach.getTENSACH());
        sqLiteStatement.bindLong(4,sach.getSOQUYEN());
        sqLiteStatement.bindString(5,sach.getTRANGTHAI());
        sqLiteStatement.bindLong(6,sach.getGIABAN());
        sqLiteStatement.bindBlob(7,sach.getHINH_SACH());
        sqLiteStatement.executeUpdateDelete();
    }
    public void DELETE_SACH(int MASACH){
        SQLiteDatabase database= getWritableDatabase();
        database.execSQL("delete from SACH where MASACH='"+MASACH+"'");
    }
    public void INSERT_TACGIA(String MATACGIA, String TENTACGIA, String GIOITINH_TG, String NGAYSINH_TG, String DIACHI_TG, byte[] HINH_TACGIA)
    {
        SQLiteDatabase database= getWritableDatabase();
        String sql= "INSERT INTO TACGIA VALUES(?,?,?,?,?,?)";
        SQLiteStatement sqLiteStatement= database.compileStatement(sql);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindString(1,MATACGIA);
        sqLiteStatement.bindString(2,TENTACGIA);
        sqLiteStatement.bindString(3,GIOITINH_TG);
        sqLiteStatement.bindString(4,NGAYSINH_TG);
        sqLiteStatement.bindString(5,DIACHI_TG);
        sqLiteStatement.bindBlob(6,HINH_TACGIA);
        sqLiteStatement.executeInsert();
    }
    public void UPDATE_TACGIA(TACGIA tacgia)
    {
        SQLiteDatabase database= getWritableDatabase();
        String sql= "UPDATE TACGIA SET TENTACGIA=?,GIOITINH_TG=?,NGAYSINH_TG=?,DIACHI_TG=?,HINH_TACGIA=? WHERE MATACGIA=?";
        SQLiteStatement sqLiteStatement= database.compileStatement(sql);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindString(6,tacgia.getMATACGIA());
        sqLiteStatement.bindString(1,tacgia.getTENTACGIA());
        sqLiteStatement.bindString(2,tacgia.getGIOITINH_TG());
        sqLiteStatement.bindString(3,tacgia.getNGAYSINH_TG());
        sqLiteStatement.bindString(4,tacgia.getDIACHI_TG());
        sqLiteStatement.bindBlob(5,tacgia.getHINH_TACGIA());
        sqLiteStatement.executeUpdateDelete();
    }
    public void DELETE_TACGIA(String MATACGIA){
        SQLiteDatabase database= getWritableDatabase();
        database.execSQL("DELETE FROM TACGIA WHERE MATACGIA='"+MATACGIA+"'");
    }
    public void INSERT_THELOAI(String MATHELOAI, String TENTHELOAI){
        SQLiteDatabase database= getWritableDatabase();
        String sql= "Insert into THELOAI values(?,?)";
        SQLiteStatement sqLiteStatement= database.compileStatement(sql);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindString(1,MATHELOAI);
        sqLiteStatement.bindString(2,TENTHELOAI);
        sqLiteStatement.executeInsert();
    }
    public void UPDATE_THELOAI(THELOAI theloai){
        SQLiteDatabase database= getWritableDatabase();
        String sql= "update THELOAI set TENLOAI=? where MALOAI=?";
        SQLiteStatement sqLiteStatement= database.compileStatement(sql);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindString(1,theloai.getTENLOAI());
        sqLiteStatement.bindString(2,theloai.getMALOAI());
        sqLiteStatement.executeUpdateDelete();
    }
    public void DELETE_THELOAI(String MATHELOAI){
        SQLiteDatabase database= getWritableDatabase();
        database.execSQL("delete from THELOAI where MALOAI='"+MATHELOAI+"'");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
