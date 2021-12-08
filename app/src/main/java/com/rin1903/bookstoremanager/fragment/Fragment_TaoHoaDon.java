package com.rin1903.bookstoremanager.fragment;

import static com.rin1903.bookstoremanager.MainActivity.Tag;
import static com.rin1903.bookstoremanager.MainActivity.database;
import static com.rin1903.bookstoremanager.MainActivity.dulieu;
import static com.rin1903.bookstoremanager.MainActivity.hoadonArrayList;
import static com.rin1903.bookstoremanager.MainActivity.khachhangArrayList;
import static com.rin1903.bookstoremanager.MainActivity.refesh_hoadon;
import static com.rin1903.bookstoremanager.MainActivity.refesh_khachhang;
import static com.rin1903.bookstoremanager.MainActivity.refesh_sach;
import static com.rin1903.bookstoremanager.MainActivity.sachArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
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
import com.rin1903.bookstoremanager.SQLite.SACH_TRONG_HOADON;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Fragment_TaoHoaDon extends Fragment {

    private Unbinder unbinder;
    private String mahoadon;
    private ArrayList<SACH_TRONG_HOADON> arrayList;
    private ChiTietHoaDonAdapter adapter;
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
    private HOADON hoadon;
    private ArrayList<CHI_TIET_HOA_DON>  arrayList_chitiethoadon_update;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hoadon, container, false);
        final Activity activity = getActivity();
        unbinder = ButterKnife.bind(this, view);
        Tag = Fragment_TaoHoaDon.class.getName();

        refesh_spinner_sach();
        refesh_spinner_khachhang();

        Bundle bundle = getArguments();
        //loadlistview
        if (bundle != null) {
            dulieu = bundle.getString("guidulieu").split("_");
            if (dulieu[0].toLowerCase().contains("tao")) {
                Cursor cursor = database.Getdata("select MAHOADON from HOADON order by MAHOADON desc limit 1");

                if (cursor.getCount() == 0) {
                    mahoadon = "mahoadon-01";
                } else {
                    String[] tach = new String[0];
                    while (cursor.moveToNext()) {
                        tach = cursor.getString(0).split("-");
                    }
                    mahoadon = String.format("%s-%s", tach[0], String.valueOf(Integer.parseInt(tach[1]) + 1));
                }
            } else if(dulieu[0].toString().toLowerCase().contains("chinhsua")){
                Cursor cursor= database.Getdata("select * from HOADON where MAHOADON='"+dulieu[2].toString()+"'");
                while (cursor.moveToNext()){
                  hoadon=  new HOADON(cursor.getString(0)
                            ,cursor.getString(1)
                            ,cursor.getInt(2)
                            ,cursor.getString(3));
                }
                Cursor cursor1= database.Getdata("select * from CHI_TIET_HOA_DON where MAHOADON='"+dulieu[2].toString()+"'");
                refesh_sach();
                int soluong =0;
                while (cursor1.moveToNext()){
                    arrayList_chitiethoadon_update.add(new CHI_TIET_HOA_DON(cursor.getInt(0),cursor.getString(1),cursor.getInt(2)));
                    for (int i=0;i<sachArrayList.size();i++){
                        if(cursor1.getInt(0)==sachArrayList.get(i).getMASACH()){
                            soluong=sachArrayList.get(i).getSOQUYEN();
                            soluong+=cursor1.getInt(2);
                            sachArrayList.get(i).setSOQUYEN(soluong);
                            database.UPDATE_SACH(sachArrayList.get(i));
                        }
                    }
                }



            }
        }

        linearLayout.setVisibility(View.GONE);

        arrayList = new ArrayList<>();
        recyclerView_thongtinhoadon.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_thongtinhoadon.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();


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
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content,fragment).addToBackStack(Tag).commit();
            }
        });

        tv_themsachvaohoadon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refesh_sach();
                refesh_adapter();
                String[] mang = spinner_masach.getSelectedItem().toString().split(" - ");
                int so = Integer.parseInt(mang[0].toString().trim());
                MainActivity.refesh_sach();
                refesh_adapter();
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
                if (getActivity().getSupportFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        btn_thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refesh_adapter();
                StringBuilder chuoi = new StringBuilder("Thông Tin Hoá Đơn \n Tên Sách \t Giá Bán \t Số Lượng \t Thành Tiền ");
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
        if (khachhangArrayList.size() > 0) {
            for (i = 0; i < khachhangArrayList.size(); i++) {
                arrayList_spinner_khachhang.add(String.valueOf(khachhangArrayList.get(i).getMAKHACHHANG() + " - " + khachhangArrayList.get(i).getTENKHACHHANG()));
            }
        } else {
            arrayList_spinner_khachhang.add("Vui Lòng Thêm Khách Hàng");
        }
        ArrayAdapter adapter_khachhang = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, arrayList_spinner_khachhang);
        spinner_makhachhang.setAdapter(adapter_khachhang);

    }

    public void refesh_spinner_sach() {
        refesh_sach();
        ArrayList<String> arrayList_spinner_sach = new ArrayList<>();
        if (sachArrayList.size() > 0) {
            for (i = 0; i < sachArrayList.size(); i++) {
                if (sachArrayList.get(i).getSOQUYEN() > 0) {
                    arrayList_spinner_sach.add(String.valueOf(sachArrayList.get(i).getMASACH()) + " - " + sachArrayList.get(i).getTENSACH());
                }
            }
        } else {
            arrayList_spinner_sach.add("Vui Lòng Thêm Sách");
        }
        ArrayAdapter adapter_sach = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, arrayList_spinner_sach);
        spinner_masach.setAdapter(adapter_sach);
    }

    public void refesh_adapter() {
        if (adapter != null) {
            if (adapter.getItemCount() != 0) {
                arrayList = adapter.getArrayList();
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
        if (spinner_makhachhang.getSelectedItem().toString().trim().toLowerCase().contains("vui lòng thêm khách hàng")) {
            makh = null;
        } else {
            mang = spinner_makhachhang.getSelectedItem().toString().split(" - ");
            makh = mang[0];
        }
//        if(hoadonArrayList.size()>0){
//            Cursor cursor= database.Getdata("select MAHOADON from HOADON order by MAHOADON desc limit 1");
//            while (cursor.moveToNext()){
//                 mang=cursor.getString(0).toString().split("-");
//            }
//            mahoadon=mang[0]+"-"+String.valueOf(Integer.parseInt(mang[1])+1);
        if (arrayList.size() > 0) {
            for (i = 0; i < arrayList.size(); i++) {
                Toast.makeText(getActivity(), "" + mahoadon, Toast.LENGTH_SHORT).show();
                database.INSERT_CHITIETHOADON(arrayList.get(i).getMaSach(), mahoadon, arrayList.get(i).getSoluongtronghoadon());
                database.QueryData("UPDATE SACH SET SOQUYEN=" + (arrayList.get(i).getSoLuongconlai() - arrayList.get(i).getSoluongtronghoadon()) + " WHERE MASACH =" + arrayList.get(i).getMaSach());
            }
            database.INSERT_HOADON(mahoadon, makh, adapter.getthanhtientong());
            Toast.makeText(getActivity(), "Thêm hoá đơn thành công", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Chưa có sách trong hoá đơn vui lòng thêm", Toast.LENGTH_SHORT).show();
        }
    }
//        else {
//            if(arrayList.size()>0){
//                for(i=0;i<arrayList.size();i++){
//                    database.INSERT_CHITIETHOADON(arrayList.get(i).getMaSach(),mahoadon,arrayList.get(i).getSoluongtronghoadon());
//                    database.QueryData("UPDATE SACH SET SOQUYEN="+(arrayList.get(i).getSoLuongconlai()-arrayList.get(i).getSoluongtronghoadon())+" WHERE MASACH ="+arrayList.get(i).getMaSach());
//                }
//                database.INSERT_HOADON(mahoadon,makh,adapter.getthanhtientong());
//                Toast.makeText(getActivity(), "Thêm hoá đơn thành công", Toast.LENGTH_SHORT).show();
//            }
//            else {
//                Toast.makeText(getActivity(), "Chưa có sách trong hoá đơn vui lòng thêm", Toast.LENGTH_SHORT).show();
//            }
//        }

//    }

}
