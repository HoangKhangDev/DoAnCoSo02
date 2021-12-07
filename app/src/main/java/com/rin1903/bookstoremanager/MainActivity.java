package com.rin1903.bookstoremanager;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.rin1903.bookstoremanager.Extension.CaptureAct;
import com.rin1903.bookstoremanager.SQLite.CHI_TIET_HOA_DON;
import com.rin1903.bookstoremanager.SQLite.CHI_TIET_PHIEU_NHAP;
import com.rin1903.bookstoremanager.SQLite.Database;
import com.rin1903.bookstoremanager.SQLite.HOADON;
import com.rin1903.bookstoremanager.SQLite.KHACHHANG;
import com.rin1903.bookstoremanager.SQLite.NHACUNGCAP;
import com.rin1903.bookstoremanager.SQLite.PHIEUNHAP;
import com.rin1903.bookstoremanager.SQLite.SACH;
import com.rin1903.bookstoremanager.SQLite.TACGIA;
import com.rin1903.bookstoremanager.SQLite.THELOAI;
import com.rin1903.bookstoremanager.fragment.Fragment_HienThi;
import com.rin1903.bookstoremanager.fragment.Fragment_Main_Menu;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends Activity {

    @BindView(R.id.navbar_bottom)  BottomNavigationView navigation;
    public static String Tag= "fragment";
    public static String[] dulieu;
    public static Database database;
    public static ArrayList<CHI_TIET_HOA_DON> chi_tiet_hoa_donArrayList;
    public static ArrayList<CHI_TIET_PHIEU_NHAP> chi_tiet_phieu_nhapArrayList;
    public static ArrayList<HOADON> hoadonArrayList;
    public static ArrayList<KHACHHANG> khachhangArrayList;
    public static ArrayList<NHACUNGCAP> nhacungcapArrayList;
    public static ArrayList<SACH> sachArrayList;
    public static ArrayList<TACGIA> tacgiaArrayList;
    public static ArrayList<THELOAI> theloaiArrayList;
    public static ArrayList<PHIEUNHAP> phieunhapArrayList;

    public static ArrayList<String> trang;
    public static final int SELECT_PICTURE=19;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        requestPermissions();

        trang= new ArrayList<>();

        database = new Database(this,"quanlycuahangsach.sqlite",null,1);


        CHI_TIET_HOA_DON chi_tiet_hoa_don= new CHI_TIET_HOA_DON();
        CHI_TIET_PHIEU_NHAP chi_tiet_phieu_nhap= new CHI_TIET_PHIEU_NHAP();
        HOADON hoadon= new HOADON();
        KHACHHANG khachhang= new KHACHHANG();
        NHACUNGCAP nhacungcap= new NHACUNGCAP();
        PHIEUNHAP phieunhap= new PHIEUNHAP();
        SACH sach= new SACH();
        TACGIA tacgia = new TACGIA();
        THELOAI theloai = new THELOAI();


        database.QueryData(chi_tiet_hoa_don.SQL_Createtable());
        database.QueryData(chi_tiet_phieu_nhap.SQL_createtable());
        database.QueryData(hoadon.SQL_createtable());
        database.QueryData(khachhang.SQL_createtable());
        database.QueryData(nhacungcap.SQL_createtable());
        database.QueryData(phieunhap.SQL_createtable());
        database.QueryData(sach.SQL_createtable());
        database.QueryData(tacgia.SQL_createtable());
        database.QueryData(theloai.SQL_createtable());

        refesh_chitiethoadon();
        refesh_chitietphieunhap();
        refesh_hoadon();
        refesh_khachhang();
        refesh_nhacungcap();
        refesh_phieunhap();
        refesh_sach();
        refesh_tacgia();
        refesh_theloai();


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        Fragment_Main_Menu fragment_menu= new Fragment_Main_Menu();
        fragmentTransaction.add(R.id.fragment_content,fragment_menu);
        fragmentTransaction.commit();


        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch (item.getItemId()){
                   case R.id.home:
                   {
                       FragmentManager fragmentManager = getFragmentManager();
                       FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                       Fragment_Main_Menu fragment_menu= new Fragment_Main_Menu();
                       fragmentTransaction.add(R.id.fragment_content,fragment_menu);
                       fragmentTransaction.commit();
                       break;
                   }
                   case R.id.taohoadon:
                   {

                       break;
                   }
                   case R.id.report:
                   {
                   }
               }
                return true;
            }
        });

    }





    private void requestPermissions() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Quyền Truy Cập Thất Bại\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                System.exit(0);
            }

        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("Nếu bạn không cung cấp quyền truy cập, bạn sẽ không thể sử dụng dịch vụ của ứng dụng" +
                        "\n\nVui lòng cho phép quyền truy cập tại [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.INTERNET)
                .check();
    }

    public static void refesh_chitiethoadon(){
        chi_tiet_hoa_donArrayList = new ArrayList<>();
        Cursor cursor= database.Getdata("select * from CHI_TIET_HOA_DON order by MASACH,MAHOADON asc");
        if(cursor.getCount()!=0){
            while (cursor.moveToNext()){
                chi_tiet_hoa_donArrayList.add(new CHI_TIET_HOA_DON(cursor.getInt(0),cursor.getString(1),cursor.getInt(2)));
            }
        }
    }
    public static void refesh_chitietphieunhap(){
        chi_tiet_phieu_nhapArrayList = new ArrayList<>();
        Cursor cursor= database.Getdata("select * from CHI_TIET_PHIEU_NHAP order by MASACH,MAPHIEUNHAP asc");
        if(cursor.getCount()!=0){
            while (cursor.moveToNext()){
                chi_tiet_phieu_nhapArrayList.add(new CHI_TIET_PHIEU_NHAP(cursor.getInt(0),cursor.getString(1),cursor.getInt(2)));
            }
        }
    }
    public static void refesh_hoadon(){
        hoadonArrayList = new ArrayList<>();
        Cursor cursor= database.Getdata("select * from HOADON order by MAHOADON asc");
        if(cursor.getCount()!=0){
            while (cursor.moveToNext()){
                hoadonArrayList.add(new HOADON(cursor.getString(0)
                        ,cursor.getString(1)
                        ,cursor.getInt(2)
                        ,cursor.getString(3)));
            }
        }
    }
    public static void refesh_khachhang(){
        khachhangArrayList = new ArrayList<>();
        Cursor cursor= database.Getdata("select * from KHACHHANG order by MAKHACHHANG asc");
        if(cursor.getCount()!=0){
            while (cursor.moveToNext()){
                khachhangArrayList.add(new KHACHHANG(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getBlob(6)));
            }
        }
    }
    public static void refesh_nhacungcap(){
        nhacungcapArrayList = new ArrayList<>();
        Cursor cursor= database.Getdata("select * from NHACUNGCAP order by MANHACUNGCAP asc");
        if(cursor.getCount()!=0){
            while (cursor.moveToNext()){
                nhacungcapArrayList.add(new NHACUNGCAP(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getBlob(4)));
            }
        }
    }
    public static void refesh_phieunhap(){
        phieunhapArrayList = new ArrayList<>();
        Cursor cursor= database.Getdata("select * from PHIEUNHAP order by MAPHIEUNHAP asc");
        if(cursor.getCount()!=0){
            while (cursor.moveToNext()){
                phieunhapArrayList.add(new PHIEUNHAP(cursor.getString(0)
                        ,cursor.getString(1)
                        ,cursor.getString(2)
                        ,cursor.getInt(3)));
            }
        }
    }
    public static void refesh_sach(){
        sachArrayList = new ArrayList<>();
        Cursor cursor= database.Getdata("select * from SACH order by MASACH asc");
        if(cursor.getCount()!=0){
            while (cursor.moveToNext()){
                sachArrayList.add(new SACH(cursor.getInt(0)
                        ,cursor.getString(1)
                        ,cursor.getString(2)
                        ,cursor.getString(3)
                        ,cursor.getInt(4)
                        ,cursor.getString(5)
                        ,cursor.getInt(6)
                        ,cursor.getBlob(7)));
            }
        }
    }
    public static void refesh_tacgia(){
        tacgiaArrayList = new ArrayList<>();
        Cursor cursor= database.Getdata("select * from TACGIA order by MATACGIA asc");
        if(cursor.getCount()!=0){
            while (cursor.moveToNext()){
                tacgiaArrayList.add(new TACGIA(cursor.getString(0)
                        ,cursor.getString(1)
                        ,cursor.getString(2)
                        ,cursor.getString(3)
                        ,cursor.getString(4)
                        ,cursor.getBlob(5)));
            }
        }
    }
    public static void refesh_theloai(){
        theloaiArrayList = new ArrayList<>();
        Cursor cursor= database.Getdata("select * from THELOAI order by MALOAI asc");
        if(cursor.getCount()!=0){
            while (cursor.moveToNext()){
                theloaiArrayList.add(new THELOAI(cursor.getString(0)
                        ,cursor.getString(1)));
            }
        }
    }

    public static void fragment_menu_replace(FragmentManager fragmentManager){
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        Fragment_Main_Menu fragment_menu= new Fragment_Main_Menu();
        fragmentTransaction.replace(R.id.fragment_content,fragment_menu);
        fragmentTransaction.commit();
    }
    public static void fragment_hienthi_replace(FragmentManager fragmentManager,Bundle bundle){

        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        Fragment_HienThi fragment= new Fragment_HienThi();
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.fragment_content,fragment).addToBackStack("fragment").commit();
    }



    public void scancode(){
        IntentIntegrator integrator= new IntentIntegrator(MainActivity.this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning ...");
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result= IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result.getContents()!=null) {
            Toast.makeText(this, ""+result.getContents(), Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }


}