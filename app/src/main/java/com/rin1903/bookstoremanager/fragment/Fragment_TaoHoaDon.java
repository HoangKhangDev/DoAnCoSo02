package com.rin1903.bookstoremanager.fragment;

import static com.rin1903.bookstoremanager.MainActivity.Tag;
import static com.rin1903.bookstoremanager.MainActivity.chi_tiet_hoa_donArrayList;
import static com.rin1903.bookstoremanager.MainActivity.database;
import static com.rin1903.bookstoremanager.MainActivity.dulieu;
import static com.rin1903.bookstoremanager.MainActivity.hoadonArrayList;
import static com.rin1903.bookstoremanager.MainActivity.khachhangArrayList;
import static com.rin1903.bookstoremanager.MainActivity.refesh_hoadon;
import static com.rin1903.bookstoremanager.MainActivity.refesh_khachhang;
import static com.rin1903.bookstoremanager.MainActivity.refesh_sach;
import static com.rin1903.bookstoremanager.MainActivity.sachArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.rin1903.bookstoremanager.Adapter.ChiTietHoaDonAdapter;
import com.rin1903.bookstoremanager.MainActivity;
import com.rin1903.bookstoremanager.R;
import com.rin1903.bookstoremanager.SQLite.CHI_TIET_HOA_DON;
import com.rin1903.bookstoremanager.SQLite.HOADON;
import com.rin1903.bookstoremanager.SQLite.SACH;
import com.rin1903.bookstoremanager.SQLite.SACH_TRONG_HOADON;

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
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Fragment_TaoHoaDon extends Fragment {

    private Unbinder unbinder;
    private String kiemtra_cothaydoikhong;
    private String mahoadon;
    private String makh;
    private HOADON hoadon;
    private ArrayList<SACH_TRONG_HOADON> arrayList;
    private ArrayList<SACH_TRONG_HOADON> arrayList_tam;
    private String ngay;

    private ChiTietHoaDonAdapter adapter;
    private ArrayList<SACH> saches;
    @BindView(R.id.btn_huy_fragment_taohoadon)
    Button btn_huy;
    @BindView(R.id.btn_thanhtoan_fragment_taohoadon)
    Button btn_thanhtoan;
    @BindView(R.id.image_themsach_taohoadon)
    ImageView img_themsachtheospinner;
    @BindView(R.id.tv_themsachvaohoadon_taohoadon)
    TextView tv_themsachvaohoadon;
    @BindView(R.id.image_themkhachhangmoi_taohoadon)
    ImageView img_themkhachhangmoi;
    @BindView(R.id.layout_sach_taohoadon)
    LinearLayout layout_sach;
    @BindView(R.id.recycleview_fragment_taohoadon)
    RecyclerView recyclerView_thongtinhoadon;
    @BindView(R.id.spinner_makhachhang_taohoadon)
    Spinner spinner_makhachhang;
    @BindView(R.id.spinner_masach_taohoadon)
    Spinner spinner_masach;
    @BindView(R.id.linear_hoadon_taohoadon)
    LinearLayout linearLayout;
    @BindView(R.id.scanner_view)
    CodeScannerView scannerview;
    @BindView(R.id.btn_quetma_taohoadon)
    Button btn_quetmabarcode;
    private CodeScanner mCodeScanner;
    private int i, j;
    private String[] dulieu;
    private int dem=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hoadon, container, false);
        final Activity activity = getActivity();
        unbinder = ButterKnife.bind(this, view);
        Tag = Fragment_TaoHoaDon.class.getName();

        arrayList_tam= new ArrayList<>();
        arrayList = new ArrayList<>();
        refesh_spinner_sach();
        refesh_spinner_khachhang();
        saches=new ArrayList<>();


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
                    mahoadon = String.format("%s-%s", tach[0], String.valueOf(Integer.parseInt(tach[1]) + 1));
                }
                else {
                    mahoadon="hd-1";
                }
            }
            else if(dulieu[0].toString().toLowerCase().contains("chinhsua")){
                refesh_spinner_khachhang();
                mahoadon= dulieu[2];
                Cursor cursor= database.Getdata("select * from HOADON where MAHOADON='"+dulieu[2].toString()+"'");
                while (cursor.moveToNext()){
                  hoadon=  new HOADON(cursor.getString(0)
                            ,cursor.getString(1)
                            ,cursor.getInt(2)
                            ,cursor.getString(3));
                  makh=hoadon.getMAKHACHHANG();
                    for(int i=0;i<spinner_makhachhang.getCount();i++){
                        if(spinner_makhachhang.getItemAtPosition(i).toString().contains(cursor.getString(1))){
                            spinner_makhachhang.setSelection(i);
                        }
                    }

                }

                    cursor= database.Getdata("select * from CHI_TIET_HOA_DON where MAHOADON='"+dulieu[2].toString()+"'");
                    while (cursor.moveToNext()){
                        Cursor cursor1= database.Getdata("select * from SACH where MASACH="+cursor.getInt(0));
                        while (cursor1.moveToNext()){
                            arrayList.add(new SACH_TRONG_HOADON(cursor1.getString(3),cursor1.getInt(0),
                                    (cursor1.getInt(4)+cursor.getInt(2)),cursor.getInt(2),cursor1.getInt(6)));
                            arrayList_tam.add(new SACH_TRONG_HOADON(cursor1.getString(3),cursor1.getInt(0),
                                    (cursor1.getInt(4)+cursor.getInt(2)),cursor.getInt(2),cursor1.getInt(6)));
                        }
                    }
                if(arrayList!=null){
                    adapter = new ChiTietHoaDonAdapter(getActivity(), arrayList);
                    recyclerView_thongtinhoadon.setAdapter(adapter);
                }



            }
        }


        recyclerView_thongtinhoadon.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_thongtinhoadon.setLayoutManager(linearLayoutManager);
        if(arrayList!=null){
            adapter = new ChiTietHoaDonAdapter(getActivity(), arrayList);
            recyclerView_thongtinhoadon.setAdapter(adapter);
        }
        refesh_adapter();





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
                        int so = Integer.parseInt(result.getText().toString());
                        Cursor cursor = database.Getdata("select * from Sach where MaSach=" + Integer.parseInt(result.getText()) + " and SoQuyen>0");
                        if (cursor.getCount() > 0) {
                            if (kiemtra_sachtronghoadon(so) == 0) {
                                while (cursor.moveToNext()) {
                                    arrayList.add(new SACH_TRONG_HOADON(cursor.getString(3), cursor.getInt(0), cursor.getInt(4), cursor.getInt(6)));
                                    adapter = new ChiTietHoaDonAdapter(getActivity(), arrayList);
                                    recyclerView_thongtinhoadon.setAdapter(adapter);
                                    dialog("Sách này đã thêm vào thành công!!! Bạn có muốn thêm sách khác?", "Thêm sách khác", "Rời khỏi");

                                }
                            } else {
                                dialog("Sách này đã có trong hoá đơn!!! Vui lòng thêm sách khác!!!", "Thêm sách khác", "Rời khỏi");
                            }
                        } else {
                            dialog("Vui lòng kiểm tra lại sách này trong hệ thống!!!\n Bạn có muốn thêm sách khác vào hoá đơn không???", "Thêm sách khác", "Rời khỏi");
                        }
                    }
                });
            }
        });


        img_themkhachhangmoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("guidulieu", "tao_Khách Hàng_sach");
                Fragment_KhachHang fragment = new Fragment_KhachHang();
                fragment.setArguments(bundle1);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, fragment).addToBackStack(Tag).commit();
            }
        });

        img_themsachtheospinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("guidulieu", "tao_Sách_sach");
                Fragment_Sach fragment = new Fragment_Sach();
                fragment.setArguments(bundle1);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content,fragment).addToBackStack(Tag).commit();
            }
        });

        tv_themsachvaohoadon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refesh_sach();
                refesh_spinner_sach();
                refesh_adapter();
                if(spinner_masach.getCount()>0){
                    if(!spinner_masach.getSelectedItem().toString().toLowerCase().contains("vui lòng thêm sách")){
                        Toast.makeText(getActivity(), ""+spinner_masach.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                        String[] mang = spinner_masach.getSelectedItem().toString().split(" - ");
                        int so = Integer.parseInt(mang[0].toString().trim());
                        Cursor cursor = database.Getdata("select * from Sach where MaSach=" + so + " and SoQuyen>0");
                        if (cursor.getCount() > 0) {
                            if (kiemtra_sachtronghoadon(so) == 0) {
                                while (cursor.moveToNext()) {
                                    arrayList.add(new SACH_TRONG_HOADON(cursor.getString(3), cursor.getInt(0), cursor.getInt(4), cursor.getInt(6)));
                                    adapter = new ChiTietHoaDonAdapter(getActivity(), arrayList);
                                    recyclerView_thongtinhoadon.setAdapter(adapter);
                                    dialog("Sách này đã thêm vào thành công!!! Bạn có muốn thêm sách khác?", "Thêm sách khác", "Rời khỏi");
                                    linearLayout.setVisibility(View.VISIBLE);

                                }
                            } else {
                                dialog("Sách này đã có trong hoá đơn!!! Vui lòng thêm sách khác!!!", "Thêm sách khác", "Rời khỏi");
                            }
                        } else {
                            dialog("Vui lòng kiểm tra lại sách này trong hệ thống!!!\n Bạn có muốn thêm sách khác vào hoá đơn không???", "Thêm sách khác", "Rời khỏi");
                        }
                    }
                    else {
                        Toast.makeText(getActivity(), "Sách đã hết vui lòng cập nhập thêm sách lại trên hệ thống", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        btn_quetmabarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                scancode();
                if (btn_quetmabarcode.getText().toString().toLowerCase().contains("quét mã")) {
                    tv_themsachvaohoadon.setVisibility(View.GONE);
                    layout_sach.setVisibility(View.GONE);
                    refesh_adapter();
                    scannerview.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                    mCodeScanner.startPreview();
                    btn_quetmabarcode.setText("Huỷ");
                } else {
                    tv_themsachvaohoadon.setVisibility(View.VISIBLE);
                    layout_sach.setVisibility(View.VISIBLE);
                    scannerview.setVisibility(View.GONE);
                    btn_quetmabarcode.setText("Quét Mã");
                }
            }
        });

        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().popBackStack();


            }
        });

        btn_thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refesh_adapter();
                StringBuilder chuoi = new StringBuilder("Thông Tin Hoá Đơn ");
                if(dulieu[0].equals("tao")){
                    if (arrayList.size() > 0) {
                        for (i = 0; i < arrayList.size(); i++) {
                            chuoi.append("\n Tên Sách: ").append(arrayList.get(i).getTenSach()).append("\t").append(String.valueOf(arrayList.get(i).getGiaban())).append("\t").append(String.valueOf(arrayList.get(i).getSoluongtronghoadon())).append("\t").append(String.valueOf(arrayList.get(i).getThanhtien()));
                        }
                        chuoi.append("\n Tổng thành tiền:").append(adapter.getthanhtientong());
                        Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.dialog_hoadon);
                        TextView tv_noidung, tv_xacnhan, tv_huy;
                        tv_noidung = dialog.findViewById(R.id.tv_thongtinhoadon_dialog_hoadon);
                        tv_xacnhan = dialog.findViewById(R.id.tv_xacnhan_dialog_hoadon);
                        tv_huy = dialog.findViewById(R.id.tv_huy_dialog_hoadon);
                        tv_noidung.setText(chuoi);
                        tv_xacnhan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                themhoadon();
                                dialog.cancel();
                                getActivity().getSupportFragmentManager().popBackStack();

                            }
                        });
                        tv_huy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                                getActivity().getSupportFragmentManager().popBackStack();

                            }
                        });
                        dialog.show();
                    }

                }
                else {
                    updatehoadon();
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

    public void dialog(String chuoi, String tenxacnhan, String huy) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_xacnhan);
        TextView tv = dialog.findViewById(R.id.tv_noidung_dialog_xacnhan);
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
                tv_themsachvaohoadon.setVisibility(View.VISIBLE);
                layout_sach.setVisibility(View.VISIBLE);
                btn_quetmabarcode.setText("Quét Mã");
                dialog.cancel();

            }
        });
        dialog.show();

    }

    public void refesh_spinner_khachhang() {
        refesh_khachhang();

        ArrayList<String> arrayList_spinner_khachhang = new ArrayList<>();
        if(khachhangArrayList.size()>0){
            if(spinner_makhachhang.getCount()==0){
                for(int i=0;i<khachhangArrayList.size();i++){
                    arrayList_spinner_khachhang.add(String.valueOf(khachhangArrayList.get(i).getMAKHACHHANG() + " - " + khachhangArrayList.get(i).getTENKHACHHANG()));
                }
            }
            else {
                for(int i=0;i<spinner_makhachhang.getCount();i++){
                    if(spinner_makhachhang.getAdapter().getItem(i).toString().contains("Vui Lòng Thêm Khách Hàng")){
                        spinner_makhachhang.setAdapter(null);
                    }
                }
            }

        }  else {
            arrayList_spinner_khachhang.add("Vui Lòng Thêm Khách Hàng");
        }
        ArrayAdapter adapter_khachhang = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, arrayList_spinner_khachhang);
        spinner_makhachhang.setAdapter(adapter_khachhang);

    }

    public void refesh_spinner_sach() {
        refesh_sach();
        ArrayList<String> arrayList_spinner_sach= new ArrayList<>();
        ArrayAdapter adapter_sach = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, arrayList_spinner_sach);
        if(sachArrayList.size()>0){
            if(spinner_masach.getCount()==0){
                spinner_masach.setAdapter(null);
                for(int i=0;i<sachArrayList.size();i++){
                    arrayList_spinner_sach.add(String.valueOf(sachArrayList.get(i).getMASACH())+" - "+sachArrayList.get(i).getTENSACH());
                }adapter_sach = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, arrayList_spinner_sach);
                spinner_masach.setAdapter(adapter_sach);
            }
        }
        else {
                    spinner_masach.getAdapter().getItem(i).toString().contains("Vui Lòng Thêm Sách");
            }
    }

    public void refesh_adapter() {
        if (adapter != null) {
            if (adapter.getItemCount() != 0) {
                arrayList = adapter.getArrayList();
                ArrayAdapter adapter_khachhang = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, arrayList);
                spinner_makhachhang.setAdapter(adapter_khachhang);
            }

        }

    }

    public int kiemtra_sachtronghoadon(int so) {
        refesh_adapter();
        int kq = 0;
        for (j = 0; j < arrayList.size(); j++) {
            if (so == arrayList.get(j).getMaSach()) {
                kq += 1;
            } else kq += 0;
        }
        return kq;

    }

    public void themhoadon() {
        refesh_adapter();
        refesh_hoadon();
        String[] mang = new String[0];
        String makh = "";
        if (spinner_makhachhang.getSelectedItem().toString().toLowerCase().contains("vui lòng thêm khách hàng")) {
            makh = "null";
        } else {
            mang = spinner_makhachhang.getSelectedItem().toString().split(" - ");
            makh = mang[0].toString();
        }
        if(hoadonArrayList.size()>0){
                if (arrayList.size() > 0) {

                    for (i = 0; i < arrayList.size(); i++) {
                        database.INSERT_CHITIETHOADON(arrayList.get(i).getMaSach(), mahoadon, arrayList.get(i).getSoluongtronghoadon());
                        database.QueryData("UPDATE SACH SET SOQUYEN=" + (arrayList.get(i).getSoLuongconlai() - arrayList.get(i).getSoluongtronghoadon()) + " WHERE MASACH =" + arrayList.get(i).getMaSach());
                    }
                    database.INSERT_HOADON(mahoadon,makh, adapter.getthanhtientong(),"null");
                    Toast.makeText(getActivity(), "Thêm hoá đơn thành công", Toast.LENGTH_SHORT).show();
                    createXlFile();
                } else {
                    Toast.makeText(getActivity(), "Chưa có sách trong hoá đơn vui lòng thêm", Toast.LENGTH_SHORT).show();
                }
             }
        else {
            if(arrayList.size()>0){
                for(i=0;i<arrayList.size();i++){
                    database.INSERT_CHITIETHOADON(arrayList.get(i).getMaSach(),mahoadon,arrayList.get(i).getSoluongtronghoadon());
                    database.QueryData("UPDATE SACH SET SOQUYEN="+(arrayList.get(i).getSoLuongconlai()-arrayList.get(i).getSoluongtronghoadon())+" WHERE MASACH ="+arrayList.get(i).getMaSach());
                }
                database.INSERT_HOADON(mahoadon,makh,adapter.getthanhtientong(),"null");
                Toast.makeText(getActivity(), "Thêm hoá đơn thành công", Toast.LENGTH_SHORT).show();
                createXlFile();
            }
            else {
                Toast.makeText(getActivity(), "Chưa có sách trong hoá đơn vui lòng thêm", Toast.LENGTH_SHORT).show();
            }
        }

    }


    public void updatehoadon() {
        refesh_hoadon();
        refesh_adapter();
        String[] mang = new String[0];
        if (makh.contains("null")|makh.isEmpty()) {
            makh = "null";
        }
        if (arrayList.size() > 0) {
            if(arrayList.size()==arrayList_tam.size()){
                for (i = 0; i < arrayList.size(); i++) {
                    database.UPDATE_CHITIETHOADON(new CHI_TIET_HOA_DON(arrayList.get(i).getMaSach(), mahoadon, arrayList.get(i).getSoluongtronghoadon()));
                    database.QueryData("UPDATE SACH SET SOQUYEN=" + (arrayList.get(i).getSoLuongconlai() - arrayList.get(i).getSoluongtronghoadon()) + " WHERE MASACH =" + arrayList.get(i).getMaSach());
                }
                database.UPDATE_HOADON(new HOADON(mahoadon,makh, adapter.getthanhtientong(),"null"));
                Toast.makeText(getActivity(), "Chỉnh sửa hoá đơn thành công", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }
            else {
                for (i = 0; i < arrayList_tam.size(); i++) {
                    int dem=0;
                    for (int j=0;j<arrayList.size();j++){
                        if(arrayList.get(j)==arrayList_tam.get(i)){
                            dem+=1;
                        }
                    }
                    if(dem>0){
                        database.UPDATE_CHITIETHOADON(new CHI_TIET_HOA_DON(arrayList.get(i).getMaSach(), mahoadon, arrayList.get(i).getSoluongtronghoadon()));
                        database.QueryData("UPDATE SACH SET SOQUYEN=" + (arrayList.get(i).getSoLuongconlai() - arrayList.get(i).getSoluongtronghoadon()) + " WHERE MASACH =" + arrayList.get(i).getMaSach());
                    }
                    else {
                        database.DELETE_CHITIETHOADON(mahoadon,arrayList_tam.get(i).getMaSach());
                        database.QueryData("UPDATE SACH SET SOQUYEN=" + arrayList_tam.get(i).getSoLuongconlai() + " WHERE MASACH =" + arrayList_tam.get(i).getMaSach());
                    }
                    database.UPDATE_HOADON(new HOADON(mahoadon,makh, adapter.getthanhtientong(),"null"));
                    Toast.makeText(getActivity(), "Chỉnh sửa hoá đơn thành công", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();

                }
            }

        } else {
            new AlertDialog.Builder(getActivity()).setTitle("Update Hoá Đơn").setMessage("Tất cả sách trong hoá đơn đã bị xoá\n" +
                    "bạn có muốn xoá Hoá Đơn này không???").setNeutralButton("Xoá", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    database.DELETE_CHITIETHOADON_ALL(mahoadon);
                    for (int i=0;i<arrayList_tam.size();i++){
                        database.QueryData("UPDATE SACH SET SOQUYEN=" + arrayList_tam.get(i).getSoLuongconlai() + " WHERE MASACH =" + arrayList_tam.get(i).getMaSach());
                    }
                    database.DELETE_HOADON(mahoadon);
                    Toast.makeText(getActivity(), "bạn đã xoá hoá đơn thành công", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }).setPositiveButton("Không",null).show();
        }
    }





    private void createXlFile() {


        // File filePath = new File(Environment.getExternalStorageDirectory() + "/Demo.xls");
        Workbook wb = new HSSFWorkbook();


        Cell cell = null;

        Sheet sheet = null;
        sheet = wb.createSheet("Demo Excel Sheet");
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
            cell.setCellValue(arrayList.get(i).getSoluongtronghoadon());

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
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd-MM-yyyy");
        ngay=simpleDateFormat.format(Calendar.getInstance().getTime()) ;
        String folderName = "Export Excel";
        File fi= new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),File.separator+"QuanLyCuaHangSach"+File.separator+folderName);
        fi.mkdirs();
        String fileName = folderName + ngay + ".xls";
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
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.getAbsolutePath()));
            startActivity(Intent.createChooser(share, "Share Report file"));
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


}
