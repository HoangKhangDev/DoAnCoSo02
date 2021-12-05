package com.rin1903.bookstoremanager;

import static com.rin1903.bookstoremanager.MainActivity.khachhangArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.rin1903.bookstoremanager.Adapter.ChiTietHoaDonAdapter;
import com.rin1903.bookstoremanager.Extension.CaptureAct;
import com.rin1903.bookstoremanager.SQLite.KHACHHANG;
import com.rin1903.bookstoremanager.SQLite.SACH;
import com.rin1903.bookstoremanager.SQLite.SACH_TRONG_HOADON;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TaoHoaDon extends Activity {
    private ArrayList<SACH_TRONG_HOADON> arrayList;
    private ArrayList<SACH> sachArrayList;
    private ArrayList<KHACHHANG> khachhangArrayList;
    private ChiTietHoaDonAdapter adapter;
    @BindView(R.id.btn_huy_fragment_taohoadon)
    Button btn_huy;
    @BindView(R.id.btn_thanhtoan_fragment_taohoadon) Button btn_thanhtoan;
    @BindView(R.id.image_themsach_taohoadon)
    ImageView img_themsachtheospinner;
    @BindView(R.id.img_quetsach_fragment_taohoadon) ImageView img_quet_sach_hoadon;
    @BindView(R.id.image_khachhang_taohoadon) ImageView img_quetmakhachhang;
    @BindView(R.id.tv_thanhtien_fragment_taohoadon)
    TextView tv_thanhtien;
    @BindView(R.id.tv_tensach_taohoadon) TextView tv_tensach;
    @BindView(R.id.tv_tenkhachhang_taohoadon) TextView tv_tenkhachhang;
    @BindView(R.id.recycleview_fragment_taohoadon)
    RecyclerView recyclerView_thongtinhoadon;
    @BindView(R.id.spinner_makhachhang_taohoadon)
    Spinner spinner_makhachhang;
    @BindView(R.id.spinner_masach_taohoadon) Spinner spinner_masach;
    @BindView(R.id.linear_hoadon_taohoadon)
    LinearLayout linearLayout;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tao_hoa_don);
        ButterKnife.bind(this);

        kiemtrahoadon();
        arrayList = new ArrayList<>();
        recyclerView_thongtinhoadon.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView_thongtinhoadon.setLayoutManager(linearLayoutManager);


        MainActivity.refesh_sach();
        MainActivity.refesh_khachhang();
        sachArrayList = MainActivity.sachArrayList;
        khachhangArrayList= MainActivity.khachhangArrayList;

        img_quet_sach_hoadon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scancode();
            }
        });
        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               TaoHoaDon.super.onBackPressed();
            }
        });
        arrayList=new ArrayList<>();
        if(adapter!=null){
            if(adapter.getItemCount()!=0){
                arrayList=adapter.getArrayList();
            }
        }


    }

    private void kiemtrahoadon() {
    }

    public void scancode(){
        IntentIntegrator integrator= new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning ...");
        integrator.initiateScan();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result= IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
        MainActivity.refesh_sach();
        sachArrayList = MainActivity.sachArrayList;
        if(result.getContents()!=null&sachArrayList.size()>0) {
            int so= Integer.parseInt(result.getContents());
            if(arrayList.size()>0){
                for(int i=0;i<sachArrayList.size();i++){
                    for (int j =0;j<arrayList.size();j++){
                        if(so==sachArrayList.get(i).getMASACH()& so!=arrayList.get(j).getMaSach()){
                            arrayList.add(new SACH_TRONG_HOADON(sachArrayList.get(i).getTENSACH(),
                                    sachArrayList.get(i).getMASACH(),sachArrayList.get(i).getSOQUYEN(),
                                    sachArrayList.get(i).getGIABAN()));
                            adapter= new ChiTietHoaDonAdapter(getApplication(),arrayList);
                            recyclerView_thongtinhoadon.setAdapter(adapter);

                        }
                        else {

                            Toast.makeText(getApplication(), "Sách này đã có trong hoá đơn", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            }
            else {
                for(int i=0;i<sachArrayList.size();i++){
                    if(so==sachArrayList.get(i).getMASACH()){
                        arrayList.add(new SACH_TRONG_HOADON(sachArrayList.get(i).getTENSACH(),
                                sachArrayList.get(i).getMASACH(),sachArrayList.get(i).getSOQUYEN(),
                                sachArrayList.get(i).getGIABAN()));
                        adapter= new ChiTietHoaDonAdapter(getApplication(),arrayList);
                        recyclerView_thongtinhoadon.setAdapter(adapter);
//
                    }
                }
            }


        }
    }
    public void dialog_add_hoadon(String chuoi){
                        Dialog dialog= new Dialog(getApplication());
                        dialog.setContentView(R.layout.dialog_xacnhan);
                        TextView tv= dialog.findViewById(R.id.tv_noidung_dialog_xacnhan);
                        Button btn_xacnhan=dialog.findViewById(R.id.btn_xacnhan_dialog_xacnhan);
                        Button btn_huy= dialog.findViewById(R.id.btn_huy_dialog_xacnhan);
                        tv.setText(chuoi);
                        btn_xacnhan.setText("Tiếp Tục");
                        btn_huy.setText("Rời khỏi");
                        btn_xacnhan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                scancode();
                                dialog.cancel();
                            }
                        });
                        btn_huy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                            }
                        });
                        dialog.show();
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