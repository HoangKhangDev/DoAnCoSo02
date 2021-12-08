package com.rin1903.bookstoremanager.fragment;

import static com.rin1903.bookstoremanager.MainActivity.database;
import static com.rin1903.bookstoremanager.MainActivity.dulieu;
import static com.rin1903.bookstoremanager.MainActivity.hoadonArrayList;
import static com.rin1903.bookstoremanager.MainActivity.phieunhapArrayList;
import static com.rin1903.bookstoremanager.MainActivity.refesh_hoadon;
import static com.rin1903.bookstoremanager.MainActivity.refesh_phieunhap;
import static com.rin1903.bookstoremanager.MainActivity.sachArrayList;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.rin1903.bookstoremanager.Adapter.HoaDonAdapter;
import com.rin1903.bookstoremanager.Adapter.PhieuNhapAdapter;
import com.rin1903.bookstoremanager.R;
import com.rin1903.bookstoremanager.SQLite.HOADON;
import com.rin1903.bookstoremanager.SQLite.PHIEUNHAP;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Fragment_thongke extends Fragment {
    private Unbinder unbinder;
    private   String[] dulieu;
    private ArrayList<PHIEUNHAP> arrayList_PN;
    private ArrayList<HOADON> arrayList_HD;

    private PhieuNhapAdapter phieuNhapAdapter;
    private HoaDonAdapter hoaDonAdapter;
    @BindView(R.id.recycleview_fragment_thongke) RecyclerView recyclerView;
    @BindView(R.id.img_calendar_star_thongke) ImageView img_star;
    @BindView(R.id.img_calendar_end_thongke) ImageView img_end;
    @BindView(R.id.tv_calendar_star_thongke) TextView tv_star;
    @BindView(R.id.tv_calendar_end_thongke) TextView tv_end;
    @BindView(R.id.tv_thongke_hienthi_fragment_thongke) TextView thongke;
    private Long star;
    private Long end;
    private int check=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_thongke,container,false);
        unbinder= ButterKnife.bind(this,view);

        refesh_phieunhap();
        refesh_hoadon();
        arrayList_HD= new ArrayList<>();
        arrayList_PN= new ArrayList<>();
        Bundle bundle= getArguments();
        //loadlistview
        if(bundle!=null) {
            dulieu = bundle.getString("guidulieu").split("_");
            if(dulieu[1].toString().toLowerCase().contains("phiếu nhập")){
                if (phieunhapArrayList.size()==0){
                    Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_list_null);

                    Button btn= dialog.findViewById(R.id.btn_ok_dialog_list_null);

                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                }
                else {
                    refesh_lv_phieunhap();
                }
            }
            else {
                if (hoadonArrayList.size()==0){
                    Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_list_null);

                    Button btn= dialog.findViewById(R.id.btn_ok_dialog_list_null);

                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                }
                else {
                    refesh_lv_hoadon();
                }
            }
        }
        img_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar= Calendar.getInstance();
                int Ngay= calendar.get(Calendar.DATE);
                int Thang= calendar.get(Calendar.MONTH);
                int Nam=calendar.get(Calendar.YEAR);
                final String chuoi = "";
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year,month,dayOfMonth);
                        star=calendar.getTimeInMillis();
                        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
                        tv_star.setText(simpleDateFormat.format(calendar.getTime()));
                        check+=1;
                    }
                },Nam,Thang,Ngay);
                datePickerDialog.show();
            }

        });
        img_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar= Calendar.getInstance();
                int Ngay= calendar.get(Calendar.DATE);
                int Thang= calendar.get(Calendar.MONTH);
                int Nam=calendar.get(Calendar.YEAR);
                final String chuoi = "";
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year,month,dayOfMonth);
                        end=calendar.getTimeInMillis();
                        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
                        tv_end.setText(simpleDateFormat.format(calendar.getTime()));
                        check+=1;
                    }
                },Nam,Thang,Ngay);
                datePickerDialog.show();


            }
        });
        thongke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check>=2){
                    String[] mangstar,mangend;
                    mangstar=tv_star.getText().toString().split("/");
                    mangend=tv_end.getText().toString().split("/");
                    if(end>star) {
                        if (dulieu[1].toString().toLowerCase().contains("phiếu nhập")) {
                            if (Integer.parseInt(mangstar[2].toString()) < Integer.parseInt(mangend[2].toString())) {
                                Cursor cursor = database.Getdata("SELECT * from PHIEUNHAP WHERE Date(NGAY_PN)>='"
                                        + mangstar[2].toString() + "-" + mangstar[1].toString() + "-" + mangstar[0].toString() +
                                        "' AND Date(NGAY_PN)<='"
                                        + mangend[2].toString() + "-" + mangend[1].toString() + "-" + mangend[0].toString() + "'");
                                while (cursor.moveToNext()) {
                                    arrayList_PN.add(new PHIEUNHAP(cursor.getString(0)
                                            , cursor.getString(1)
                                            , cursor.getString(2)
                                            , cursor.getInt(3)));
                                }

                            }
                        }else if(dulieu[1].toString().toLowerCase().contains("hoá đơn")) {
                            Cursor cursor = database.Getdata("SELECT * from HOADON WHERE Date(ngay_hd)>='"
                                    + mangstar[2].toString() + "-" + mangstar[1].toString() + "-" + mangstar[0].toString() +
                                    "' AND Date(ngay_hd)<='"
                                    + mangend[2].toString() + "-" + mangend[1].toString() + "-" + mangend[0].toString() + "'");
                            while (cursor.moveToNext()) {
                                arrayList_HD.add(new HOADON(cursor.getString(0)
                                        , cursor.getString(1)
                                        , cursor.getInt(2)
                                        , cursor.getString(3)));
                            }
                            hoaDonAdapter = new HoaDonAdapter(arrayList_HD, getActivity());
                            recyclerView.setAdapter(hoaDonAdapter);
                            hoaDonAdapter.notifyDataSetChanged();

                        }
                    }
                    else {
                        Toast.makeText(getActivity(), "Ngày bắt đầu lớn hơn ngày kết thúc", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Vui lòng đổi ngày trước", Toast.LENGTH_SHORT).show();
                }

            }
        });



        return view;
    }
    public void refesh_lv_phieunhap(){
        refesh_phieunhap();
        phieuNhapAdapter= new PhieuNhapAdapter(phieunhapArrayList,getActivity());
        recyclerView.setAdapter(phieuNhapAdapter);
        phieuNhapAdapter.notifyDataSetChanged();
    }
    public void refesh_lv_hoadon(){
        refesh_hoadon();
        hoaDonAdapter= new HoaDonAdapter(hoadonArrayList,getActivity());
        recyclerView.setAdapter(hoaDonAdapter);
        hoaDonAdapter.notifyDataSetChanged();
    }
}
