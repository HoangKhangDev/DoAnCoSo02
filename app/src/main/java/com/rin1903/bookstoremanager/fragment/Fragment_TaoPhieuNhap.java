package com.rin1903.bookstoremanager.fragment;

import static com.rin1903.bookstoremanager.MainActivity.Tag;
import static com.rin1903.bookstoremanager.MainActivity.database;
import static com.rin1903.bookstoremanager.MainActivity.hoadonArrayList;
import static com.rin1903.bookstoremanager.MainActivity.khachhangArrayList;
import static com.rin1903.bookstoremanager.MainActivity.nhacungcapArrayList;
import static com.rin1903.bookstoremanager.MainActivity.refesh_hoadon;
import static com.rin1903.bookstoremanager.MainActivity.refesh_khachhang;
import static com.rin1903.bookstoremanager.MainActivity.refesh_nhacungcap;
import static com.rin1903.bookstoremanager.MainActivity.refesh_phieunhap;
import static com.rin1903.bookstoremanager.MainActivity.refesh_sach;
import static com.rin1903.bookstoremanager.MainActivity.sachArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.rin1903.bookstoremanager.Adapter.ChiTietHoaDonAdapter;
import com.rin1903.bookstoremanager.Adapter.ChiTietPhieuNhapAdapter;
import com.rin1903.bookstoremanager.Adapter.ChiTietPhieuNhapAdapter;
import com.rin1903.bookstoremanager.MainActivity;
import com.rin1903.bookstoremanager.R;
import com.rin1903.bookstoremanager.SQLite.CHI_TIET_HOA_DON;
import com.rin1903.bookstoremanager.SQLite.CHI_TIET_PHIEU_NHAP;
import com.rin1903.bookstoremanager.SQLite.HOADON;
import com.rin1903.bookstoremanager.SQLite.KHACHHANG;
import com.rin1903.bookstoremanager.SQLite.NHACUNGCAP;
import com.rin1903.bookstoremanager.SQLite.PHIEUNHAP;
import com.rin1903.bookstoremanager.SQLite.SACH;
import com.rin1903.bookstoremanager.SQLite.SACH_TRONG_HOADON;
import com.rin1903.bookstoremanager.SQLite.SACH_TRONG_PHIEUNHAP;
import com.rin1903.bookstoremanager.SQLite.SACH_TRONG_PHIEUNHAP;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Fragment_TaoPhieuNhap extends Fragment {

    private Unbinder unbinder;
    private String ngay;
    private ArrayList<SACH_TRONG_PHIEUNHAP> arrayList;
    private ChiTietPhieuNhapAdapter adapter;
    private String[] dulieu;
    private PHIEUNHAP phieunhap;
    private String mancc;
    private  ArrayList<SACH_TRONG_PHIEUNHAP> arrayList_old;
    private String mapn;
    @BindView(R.id.btn_huy_fragment_taophieunhap) Button btn_huy;
    @BindView(R.id.btn_thanhtoan_fragment_taophieunhap) Button btn_thanhtoan;
    @BindView(R.id.image_themsach_taophieunhap) ImageView img_themsachtheospinner;
    @BindView(R.id.tv_themsachvaohoadon_taophieunhap) TextView tv_themsachvaophieunhap;
    @BindView(R.id.image_themnhacungcap_taophieunhap) ImageView img_themnhacungcapmoi;
    @BindView(R.id.layout_sach_taophieunhap) LinearLayout layout_sach;
    @BindView(R.id.recycleview_fragment_taophieunhap) RecyclerView recyclerView_thongtinhoadon;
    @BindView(R.id.spinner_manhacungcap_taophieunhap) Spinner spinner_manhacungcap;
    @BindView(R.id.spinner_masach_taophieunhap) Spinner spinner_masach;
    @BindView(R.id.linear_phieunhap_taophieunhap) LinearLayout linearLayout;
    @BindView(R.id.scanner_view) CodeScannerView scannerview;
    @BindView(R.id.btn_quetma_taophieunhap) Button btn_quetmabarcode;
    @BindView(R.id.label_tensach_fragment_phieunhap) BootstrapLabel label_tensach;
    @BindView(R.id.label_tennhacungcap_fragment_phieunhap) BootstrapLabel label_tennhacungcap;
    @BindView(R.id.img_calendar_phieunhap) ImageView img_calendar;
    @BindView(R.id.label_calendar_phieunhap) BootstrapLabel label_calendar;

    private CodeScanner mCodeScanner;
    private int i,j;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phieunhap,container,false);
        final Activity activity = getActivity();
        unbinder= ButterKnife.bind(this,view);
        Tag= Fragment_TaoPhieuNhap.class.getName();

        refesh_spinner_sach();
        refesh_spinner_nhacungcap();
        refesh_adapter();

        if(nhacungcapArrayList.size()>0){
            spinner_manhacungcap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    refesh_nhacungcap();
                    for(NHACUNGCAP ncc:nhacungcapArrayList)
                        if(ncc.getMANHACUNGCAP().equals(parent.getAdapter().getItem(position).toString()))
                        {
                            label_tennhacungcap.setText(ncc.getTENNHACUNGCAP());
                        }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    label_tennhacungcap.setText("Không có nhà cung cấp này");

                }
            });
        }
        if(sachArrayList.size()>0){
            spinner_masach.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    refesh_sach();
                    for (SACH s: sachArrayList){
                        if(s.getMASACH()==Integer.parseInt(parent.getAdapter().getItem(position).toString())){
                            label_tensach.setText(s.getTENSACH());
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    label_tensach.setText("Không có sách này");

                }
            });
        }
        img_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(label_calendar);
            }
        });


        arrayList_old = new ArrayList<>();
        arrayList = new ArrayList<>();
        recyclerView_thongtinhoadon.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView_thongtinhoadon.setLayoutManager(linearLayoutManager);
        linearLayout.setVisibility(View.GONE);
        if(arrayList!=null){
            adapter = new ChiTietPhieuNhapAdapter( getActivity(), arrayList);
            recyclerView_thongtinhoadon.setAdapter(adapter);
            linearLayout.setVisibility(View.VISIBLE);

        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            dulieu = bundle.getString("guidulieu").split("_");
            if (dulieu[0].toLowerCase().contains("tao")) {
                Cursor cursor = database.Getdata("select MAHOADON from HOADON order by MAHOADON desc limit 1");
                if (cursor.getCount() > 0) {
                    String[] tach = new String[0];
                    while (cursor.moveToNext()) {
                        tach = cursor.getString(0).split("-");
                    }
                    mapn = String.format("%s-%s", tach[0], String.valueOf(Integer.parseInt(tach[1]) + 1));
                }
                else {
                    mapn="pn-1";
                }
            }
            else if(dulieu[0].toString().toLowerCase().contains("chinhsua")){
                refesh_spinner_nhacungcap();
                mapn= dulieu[2];
                Cursor cursor= database.Getdata("select * from PHIEUNHAP where MAPHIEUNHAP='"+dulieu[2].toString()+"'");
                while (cursor.moveToNext()){
                    phieunhap=  new PHIEUNHAP(cursor.getString(0)
                            ,cursor.getString(1)
                            ,cursor.getString(2)
                            ,cursor.getInt(3));
                    mancc=phieunhap.getMANHACUNGCAP();
                    for(int i=0;i<spinner_manhacungcap.getCount();i++){
                        if(spinner_manhacungcap.getItemAtPosition(i).toString().contains(cursor.getString(1))){
                            spinner_manhacungcap.setSelection(i);
                        }
                    }

                }

                cursor= database.Getdata("select * from CHI_TIET_PHIEU_NHAP where MAPHIEUNHAP='"+dulieu[2].toString()+"'");
                while (cursor.moveToNext()){
                    Cursor cursor1= database.Getdata("select * from SACH where MASACH="+cursor.getInt(0));
                    while (cursor1.moveToNext()){
                        arrayList.add(new SACH_TRONG_PHIEUNHAP(cursor1.getString(3),cursor1.getInt(0),
                                (cursor1.getInt(4)-cursor.getInt(2)),cursor.getInt(2),cursor1.getInt(6)));
                        arrayList_old.add(new SACH_TRONG_PHIEUNHAP(cursor1.getString(3),cursor1.getInt(0),
                                (cursor1.getInt(4)-cursor.getInt(2)),cursor.getInt(2),cursor1.getInt(6)));
                    }
                }

                refesh_spinner_nhacungcap();
                for(int i=0;i<spinner_manhacungcap.getCount();i++){
                    if(spinner_manhacungcap.getItemAtPosition(i).toString().equals(mancc)){
                        spinner_manhacungcap.setSelection(i);
                        break;
                    }
                }
                adapter = new ChiTietPhieuNhapAdapter(getActivity(), arrayList);
                recyclerView_thongtinhoadon.setAdapter(adapter);
                img_themnhacungcapmoi.setVisibility(View.GONE);
                layout_sach.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                btn_quetmabarcode.setVisibility(View.GONE);



            }
        }







        mCodeScanner = new CodeScanner(activity, scannerview);
        scannerview.setVisibility(View.GONE);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.refesh_sach();
                        refesh_adapter();
                        int so= Integer.parseInt(result.getText().toString());
                        Cursor cursor= database.Getdata("select * from Sach where MaSach="+so);
                        if(cursor.getCount()>0){
                            if(kiemtra_sachtrongphieunhap(so)==0){
                                while (cursor.moveToNext()){
                                    arrayList.add(new SACH_TRONG_PHIEUNHAP(cursor.getString(3),cursor.getInt(0),cursor.getInt(4),cursor.getInt(6)));
                                    adapter= new ChiTietPhieuNhapAdapter(getActivity(),arrayList);
                                    recyclerView_thongtinhoadon.setAdapter(adapter);
                                    dialog("Sách này đã thêm vào thành công!!! Bạn có muốn thêm sách khác?","Thêm sách khác","Rời khỏi");
                                }
                            }
                            else {
                                dialog("Sách này đã có trong phiếu nhập!!! Vui lòng thêm sách khác!!!","Thêm sách khác","Rời khỏi");
                            }
                        }
                        else {
                            dialog("Sách này chưa có trên hệ thống!!! Vui lòng thêm sách mới","Tiếp tục quét","Rời khỏi");
                        }
                    }
                });
            }
        });



        img_themnhacungcapmoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1= new Bundle();
                bundle1.putString("guidulieu","tao_Nhà Cung Cấp_sach");
                Fragment_NhaCungCap fragment=new Fragment_NhaCungCap();
                fragment.setArguments(bundle1);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content,fragment).addToBackStack(Tag).commit();
            }
        });

        img_themsachtheospinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1= new Bundle();
                bundle1.putString("guidulieu","tao_Sách_sach");
                Fragment_Sach fragment=new Fragment_Sach();
                fragment.setArguments(bundle1);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content,fragment).addToBackStack(Tag).commit();
            }
        });

        tv_themsachvaophieunhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refesh_sach();
                refesh_adapter();
                int dem=0;
                String[] mang=spinner_masach.getSelectedItem().toString().split(" - ");
                int so= Integer.parseInt(mang[0].toString().trim());
                if(sachArrayList.size()>0){
                    for (i=0;i<sachArrayList.size();i++){
                        if(so == sachArrayList.get(i).getMASACH()){
                            if(arrayList.size()>0){
                                if(kiemtra_sachtrongphieunhap(sachArrayList.get(i).getMASACH())!=0){
                                    Toast.makeText(getActivity(), "Đã tồn tại sách này trong phiếu nhập", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    arrayList.add(new SACH_TRONG_PHIEUNHAP(sachArrayList.get(i).getTENSACH(),sachArrayList.get(i).getMASACH()
                                            ,sachArrayList.get(i).getSOQUYEN(),sachArrayList.get(i).getGIABAN()));
                                    adapter= new ChiTietPhieuNhapAdapter(getActivity(),arrayList);
                                    recyclerView_thongtinhoadon.setAdapter(adapter);
                                    Toast.makeText(getActivity(), " đã được thêm vào phiếu nhập", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                arrayList.add(new SACH_TRONG_PHIEUNHAP(sachArrayList.get(i).getTENSACH(),sachArrayList.get(i).getMASACH()
                                ,sachArrayList.get(i).getSOQUYEN(),sachArrayList.get(i).getGIABAN()));
                                adapter= new ChiTietPhieuNhapAdapter(getActivity(),arrayList);
                                recyclerView_thongtinhoadon.setAdapter(adapter);
                            }
                        }
                    }
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(getActivity(), "Không có sách nào! vui lòng thêm!!!", Toast.LENGTH_SHORT).show();
                }



            }
        });

        btn_quetmabarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                scancode();
                if(btn_quetmabarcode.getText().toString().toLowerCase().contains("quét mã")){
                    tv_themsachvaophieunhap.setVisibility(View.GONE);
                    layout_sach.setVisibility(View.GONE);
                    refesh_adapter();
                    scannerview.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                    mCodeScanner.startPreview();
                    btn_quetmabarcode.setText("Huỷ");
                }
                else {
                    tv_themsachvaophieunhap.setVisibility(View.VISIBLE);
                    layout_sach.setVisibility(View.VISIBLE);
                    scannerview.setVisibility(View.GONE);
                    btn_quetmabarcode.setText("Quét Mã");
                }
            }
        });

        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity().getSupportFragmentManager()!=null){
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        btn_thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refesh_adapter();
                StringBuilder chuoi= new StringBuilder("Thông Tin Phiếu Nhập ");
                if(dulieu[0].equals("tao")){
                    if(arrayList.size()>0){
                        for (i=0;i<arrayList.size();i++){
                            chuoi.append("\n Tên Sách: ").append(arrayList.get(i).getTenSach()).append("\t Giá Bán: ").append(String.valueOf(arrayList.get(i).getGiaban())).append("\t Số lượng: ").append(String.valueOf(arrayList.get(i).getsoluongtrongphieunhap())).append("\t Thành Tiền: ").append(String.valueOf(arrayList.get(i).getThanhtien()));
                        }
                        chuoi.append("\n Tổng thành tiền:").append(adapter.getthanhtientong());
                        Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.dialog_hoadon);
                        TextView tv_noidung,tv_xacnhan,tv_huy;
                        tv_noidung= dialog.findViewById(R.id.tv_thongtinhoadon_dialog_hoadon);
                        tv_xacnhan=dialog.findViewById(R.id.tv_xacnhan_dialog_hoadon);
                        tv_huy = dialog.findViewById(R.id.tv_huy_dialog_hoadon);
                        tv_noidung.setText(chuoi);
                        tv_xacnhan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(!spinner_manhacungcap.getSelectedItem().toString().toLowerCase().contains("vui lòng thêm nhà cung cấp")){
                                    themphieunhap(spinner_manhacungcap.getSelectedItem().toString());
                                    dialog.cancel();
                                    getActivity().getSupportFragmentManager().popBackStack();
                                }
                                else {
                                    Toast.makeText(getActivity(), "Vui lòng thêm nhà cung cấp", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }


                            }
                        });
                        tv_huy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();

                            }
                        });
                        dialog.show();
                    }
                }
                else {
                    updatephieunhap();
                }
            }
        });


        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
        // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mCodeScanner.releaseResources();

    }
    public void dialog(String chuoi,String tenxacnhan,String huy){
        Dialog dialog= new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_xacnhan);
        TextView tv= dialog.findViewById(R.id.tv_noidung_dialog_xacnhan);
        TextView btn_tieptuc = dialog.findViewById(R.id.btn_xacnhan_dialog_xacnhan);
        TextView btn_huy = dialog.findViewById(R.id.btn_huy_dialog_xacnhan);
        tv.setText(chuoi);
        btn_tieptuc.setText(tenxacnhan);
        btn_huy.setText(huy);
        onPause();
        btn_tieptuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                onResume();

            }
        });
        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scannerview.setVisibility(View.GONE);
                tv_themsachvaophieunhap.setVisibility(View.VISIBLE);
                layout_sach.setVisibility(View.VISIBLE);
                btn_quetmabarcode.setText("Quét Mã");
                dialog.cancel();

            }
        });
        dialog.show();

    }
    public void refesh_spinner_nhacungcap(){
        refesh_nhacungcap();
        ArrayList<String> arrayList_spinner_nhacungcap= new ArrayList<>();
        if(nhacungcapArrayList.size()>0){
            if(spinner_manhacungcap.getCount()==0){
                for(int i=0;i<nhacungcapArrayList.size();i++){
                    arrayList_spinner_nhacungcap.add(String.valueOf(nhacungcapArrayList.get(i).getMANHACUNGCAP()));
                }
            }
            else {
                for(int i=0;i<spinner_manhacungcap.getCount();i++){
                    if(spinner_manhacungcap.getAdapter().getItem(i).toString().contains("Vui Lòng Thêm Nhà Cung Cấp")){
                        spinner_manhacungcap.setAdapter(null);
                    }
                }
            }

        }  else {
            arrayList_spinner_nhacungcap.add("Vui Lòng Thêm Nhà Cung Cấp");
        }
        ArrayAdapter adapter= new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,arrayList_spinner_nhacungcap);
        spinner_manhacungcap.setAdapter(adapter);

    }
    public void refesh_spinner_sach() {
        refesh_sach();

        ArrayList<String> arrayList_spinner_sach= new ArrayList<>();
        if(sachArrayList.size()>0){
            if(spinner_masach.getCount()==0){
                for(int i=0;i<sachArrayList.size();i++){
                    arrayList_spinner_sach.add(String.valueOf(sachArrayList.get(i).getMASACH()));
                }
            }
            else {
                for(int i=0;i<spinner_masach.getCount();i++){
                    if(spinner_masach.getAdapter().getItem(i).toString().contains("Vui Lòng Thêm Sách")){
                        spinner_masach.setAdapter(null);
                    }
                }
            }

        }  else {
            arrayList_spinner_sach.add("Vui Lòng Thêm Sách");
        }
        ArrayAdapter adapter_sach= new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,arrayList_spinner_sach);
        spinner_masach.setAdapter(adapter_sach);
    }
    public void refesh_adapter(){
        if(adapter!=null){
            if(adapter.getItemCount()!=0){
                arrayList=adapter.getArrayList();
            }
        }
    }
    public int kiemtra_sachtrongphieunhap(int so){
        refesh_adapter();
        int kq=0;
        for(j=0;j<arrayList.size();j++){
            if(so==arrayList.get(j).getMaSach()){
               kq+=1;
            }
            else kq+=0;
        }
        return kq;

    }
    public void themphieunhap(String manhacungcap){
        refesh_adapter();
        refesh_hoadon();
        String maphieunhap="";
        String[] mang = new String[0];
        Cursor cursor= database.Getdata("select MAPHIEUNHAP from PHIEUNHAP order by MAPHIEUNHAP desc limit 1");
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                mang= cursor.getString(0).split("-");
            }
            maphieunhap="pn-"+String.valueOf(Integer.parseInt(mang[1])+1);
            if(arrayList.size()>0){
                for(i=0;i<arrayList.size();i++){
                    database.INSERT_CHITIETPHIEUNHAP(arrayList.get(i).getMaSach(),maphieunhap,arrayList.get(i).getsoluongtrongphieunhap());
                    database.QueryData("UPDATE SACH SET SOQUYEN="+(arrayList.get(i).getSoLuongconlai()+arrayList.get(i).getsoluongtrongphieunhap())+" WHERE MASACH ="+arrayList.get(i).getMaSach());
                }
                if(!label_calendar.getText().toString().toLowerCase().contains("ngày lập phiếu nhập")){
                    database.INSERT_PHIEUNHAP(maphieunhap,manhacungcap,adapter.getthanhtientong(),label_calendar.getText().toString());
                    Toast.makeText(getActivity(), "Thêm phiếu nhập thành công", Toast.LENGTH_SHORT).show();
                    phieunhap= new PHIEUNHAP();
                    phieunhap.setMAPHIEUNHAP(maphieunhap);
                    phieunhap.setMANHACUNGCAP(manhacungcap);
                    phieunhap.setNGAY_PN(label_calendar.getText().toString());
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                else {
                    phieunhap= new PHIEUNHAP();
                    phieunhap.setMAPHIEUNHAP(maphieunhap);
                    phieunhap.setMANHACUNGCAP(manhacungcap);
                    SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    phieunhap.setNGAY_PN(simpleDateFormat.format(Calendar.getInstance().getTime()));
                    database.INSERT_PHIEUNHAP(maphieunhap,manhacungcap,adapter.getthanhtientong(),phieunhap.getNGAY_PN());
                    Toast.makeText(getActivity(), "Thêm phiếu nhập thành công", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }

            }
            else {
                Toast.makeText(getActivity(), "Chưa có sách trong phiếu nhập vui lòng thêm", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            maphieunhap="pn-1";
            if(arrayList.size()>0){
                for(i=0;i<arrayList.size();i++){
                    database.INSERT_CHITIETPHIEUNHAP(arrayList.get(i).getMaSach(),maphieunhap,arrayList.get(i).getsoluongtrongphieunhap());
                    database.QueryData("UPDATE SACH SET SOQUYEN="+(arrayList.get(i).getSoLuongconlai()+arrayList.get(i).getsoluongtrongphieunhap())+" WHERE MASACH ="+arrayList.get(i).getMaSach());
                }

                if(!label_calendar.getText().toString().toLowerCase().contains("ngày lập phiếu nhập")){
                    database.INSERT_PHIEUNHAP(maphieunhap,manhacungcap,adapter.getthanhtientong(),label_calendar.getText().toString());
                    Toast.makeText(getActivity(), "Thêm phiếu nhập thành công", Toast.LENGTH_SHORT).show();
                    phieunhap= new PHIEUNHAP();
                    phieunhap.setMAPHIEUNHAP(maphieunhap);
                    phieunhap.setMANHACUNGCAP(manhacungcap);
                    phieunhap.setNGAY_PN(label_calendar.getText().toString());
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                else {
                    phieunhap= new PHIEUNHAP();
                    phieunhap.setMAPHIEUNHAP(maphieunhap);
                    phieunhap.setMANHACUNGCAP(manhacungcap);
                    SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    phieunhap.setNGAY_PN(simpleDateFormat.format(Calendar.getInstance().getTime()));
                    database.INSERT_PHIEUNHAP(maphieunhap,manhacungcap,adapter.getthanhtientong(),phieunhap.getNGAY_PN());
                    Toast.makeText(getActivity(), "Thêm phiếu nhập thành công", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
            else {
                Toast.makeText(getActivity(), "Chưa có sách trong phiếu nhập vui lòng thêm", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void updatephieunhap() {
        refesh_adapter();
        refesh_phieunhap();
        if (mancc.contains("null")|mancc.isEmpty()) {
            mancc = "null";
        }
        if (arrayList.size() > 0) {
            if(arrayList.size()==arrayList_old.size()){
                for (i = 0; i < arrayList.size(); i++) {
                    database.UPDATE_CHITIETPHIEUNHAP(new CHI_TIET_PHIEU_NHAP(arrayList.get(i).getMaSach(), mapn, arrayList.get(i).getsoluongtrongphieunhap()));
                    database.QueryData("UPDATE SACH SET SOQUYEN=" + (arrayList.get(i).getSoLuongconlai() + arrayList.get(i).getsoluongtrongphieunhap()) + " WHERE MASACH =" + arrayList.get(i).getMaSach());
                }
                if(!label_calendar.getText().equals("Ngày Lập phiếu nhập")){
                    phieunhap= new PHIEUNHAP(mapn,mancc,label_calendar.getText().toString(), adapter.getthanhtientong());
                    database.UPDATE_PHIEUNHAP(new PHIEUNHAP(mapn,mancc,label_calendar.getText().toString(), adapter.getthanhtientong()));
                    Toast.makeText(getActivity(), "Chỉnh sửa phiếu nhập thành công", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                else {
                    database.UPDATE_PHIEUNHAP(new PHIEUNHAP(mapn,mancc,phieunhap.getNGAY_PN(), adapter.getthanhtientong()));
                    Toast.makeText(getActivity(), "Chỉnh sửa phiếu nhập thành công", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }

            }
            else {
                for (i = 0; i < arrayList_old.size(); i++) {
                    int dem=0;
                    for (int j=0;j<arrayList.size();j++){
                        if(arrayList.get(j).getMaSach()==arrayList_old.get(i).getMaSach()){
                            dem+=1;
                            arrayList_old.get(i).setsoluongtrongphieunhap(arrayList.get(j).getsoluongtrongphieunhap());
                        }
                    }
                    if(dem>0){
                        database.UPDATE_CHITIETPHIEUNHAP(new CHI_TIET_PHIEU_NHAP(arrayList_old.get(i).getMaSach(), mapn, arrayList_old.get(i).getsoluongtrongphieunhap()));
                        database.QueryData("UPDATE SACH SET SOQUYEN=" + (arrayList_old.get(i).getSoLuongconlai() + arrayList_old.get(i).getsoluongtrongphieunhap()) + " WHERE MASACH =" + arrayList_old.get(i).getMaSach());
                    }
                    else {
                        database.DELETE_CHITIETPHIEUNHAP(mapn,arrayList_old.get(i).getMaSach());
                        database.QueryData("UPDATE SACH SET SOQUYEN=" + arrayList_old.get(i).getSoLuongconlai() + " WHERE MASACH =" + arrayList_old.get(i).getMaSach());
                    }
                    if(!label_calendar.getText().equals("Ngày Lập phiếu nhập")){
                        phieunhap= new PHIEUNHAP(mapn,mancc,label_calendar.getText().toString(), adapter.getthanhtientong());
                        database.UPDATE_PHIEUNHAP(new PHIEUNHAP(mapn,mancc,label_calendar.getText().toString(), adapter.getthanhtientong()));
                        Toast.makeText(getActivity(), "Chỉnh sửa phiếu nhập thành công", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                    else {
                        database.UPDATE_PHIEUNHAP(new PHIEUNHAP(mapn,mancc,phieunhap.getNGAY_PN(), adapter.getthanhtientong()));
                        Toast.makeText(getActivity(), "Chỉnh sửa phiếu nhập thành công", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }



                }
            }

        } else {
            new AlertDialog.Builder(getActivity()).setTitle("Update Phiếu nhập").setMessage("Tất cả sách trong phiếu nhập đã bị xoá\n" +
                    "bạn có muốn xoá phiếu nhập này không???").setNeutralButton("Xoá", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    database.DELETE_CHITIETPHIEUNHAP_ALL(mapn);
                    for (int i=0;i<arrayList_old.size();i++){
                        database.QueryData("UPDATE SACH SET SOQUYEN=" + arrayList_old.get(i).getSoLuongconlai() + " WHERE MASACH =" + arrayList_old.get(i).getMaSach());
                    }
                    database.DELETE_PHIEUNHAP(mapn);
                    Toast.makeText(getActivity(), "bạn đã xoá phiếu nhập thành công", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }).setPositiveButton("Không",null).show();
        }
    }


    private void createXlFile(PHIEUNHAP pn) {


        // File filePath = new File(Environment.getExternalStorageDirectory() + "/Demo.xls");
        Workbook wb = new HSSFWorkbook();


        Cell cell = null;

        Sheet sheet = null;
        sheet = wb.createSheet("Quản Lý Cửa Hàng Sách");
        //Now column and row
        Row row = sheet.createRow(0);

        cell = row.createCell(0);
        cell.setCellValue("Mã Sách");


        cell = row.createCell(1);
        cell.setCellValue("Tên Sách");


        cell = row.createCell(2);
        cell.setCellValue("Số Lượng");

        cell = row.createCell(3);
        cell.setCellValue("Giá bán");

        cell = row.createCell(4);
        cell.setCellValue("Thành Tiền");


        //column width
        sheet.setColumnWidth(0, (20 * 200));
        sheet.setColumnWidth(1, (30 * 200));
        sheet.setColumnWidth(2, (30 * 200));
        sheet.setColumnWidth(3, (30 * 200));
        sheet.setColumnWidth(4, (30 * 200));




        for (int i = 0; i < arrayList.size(); i++) {
            Row row1 = sheet.createRow(i + 1);

            cell = row1.createCell(0);
            cell.setCellValue(arrayList.get(i).getMaSach());

            cell = row1.createCell(1);
            cell.setCellValue((arrayList.get(i).getTenSach()));
            //  cell.setCellStyle(cellStyle);

            cell = row1.createCell(2);
            cell.setCellValue(arrayList.get(i).getsoluongtrongphieunhap());

            cell = row1.createCell(3);
            cell.setCellValue(arrayList.get(i).getGiaban());

            cell = row1.createCell(4);
            cell.setCellValue(arrayList.get(i).getThanhtien());


            sheet.setColumnWidth(0, (20 * 200));
            sheet.setColumnWidth(1, (30 * 200));
            sheet.setColumnWidth(2, (30 * 200));
            sheet.setColumnWidth(3, (30 * 200));
            sheet.setColumnWidth(4, (30 * 200));


        }

        String folderName = "Phiếu Nhập";
        File fi= new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),File.separator+"QuanLyCuaHangSach"+File.separator+folderName);
        fi.mkdirs();
        String fileName =  pn.getNGAY_PN()+"_"+pn.getMAPHIEUNHAP() + ".xls";
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+File.separator+"QuanLyCuaHangSach" + File.separator + folderName + File.separator + fileName;
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + folderName);

        if (!file.exists()) {
            file.mkdirs();
        }

        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(path);
            wb.write(outputStream);
            Toast.makeText(getActivity(), "Excel Created in " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(getActivity(), "Not OK", Toast.LENGTH_LONG).show();
            try {
                outputStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();

            }
        }


    }




    private void showDateTimeDialog(final BootstrapLabel date_time_in) {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");

                        date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(getActivity(),timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(getActivity(),dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

}
