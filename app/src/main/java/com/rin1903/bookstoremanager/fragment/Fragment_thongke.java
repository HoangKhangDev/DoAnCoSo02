package com.rin1903.bookstoremanager.fragment;

import static com.rin1903.bookstoremanager.MainActivity.database;
import static com.rin1903.bookstoremanager.MainActivity.dulieu;
import static com.rin1903.bookstoremanager.MainActivity.hoadonArrayList;
import static com.rin1903.bookstoremanager.MainActivity.phieunhapArrayList;
import static com.rin1903.bookstoremanager.MainActivity.refesh_hoadon;
import static com.rin1903.bookstoremanager.MainActivity.refesh_phieunhap;
import static com.rin1903.bookstoremanager.MainActivity.sachArrayList;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.rin1903.bookstoremanager.Adapter.HoaDonAdapter;
import com.rin1903.bookstoremanager.Adapter.PhieuNhapAdapter;
import com.rin1903.bookstoremanager.MainActivity;
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
    @BindView(R.id.btn_hienthi_bootstrap_thongke) Button thongke;
    @BindView(R.id.btn_bootstrap_luufile_thongke) Button luufile;
    @BindView(R.id.img_back_thongke) ImageView img_back;

    private Long star;
    private Long end;
    private int check=0;


    private static final String LOG_TAG = "ExternalStorageDemo";


    private static final int REQUEST_ID_READ_PERMISSION = 100;
    private static final int REQUEST_ID_WRITE_PERMISSION = 200;

    private final String fileName = "note.txt";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_thongke,container,false);
        unbinder= ButterKnife.bind(this,view);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        luufile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        LayoutAnimationController layoutAnimationController= AnimationUtils.loadLayoutAnimation(getActivity(),R.anim.layout_animation_r_to_l);
        recyclerView.setLayoutAnimation(layoutAnimationController);
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
                        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd");
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
                        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd");
                        tv_end.setText(simpleDateFormat.format(calendar.getTime()));
                        check+=1;
                    }
                },Nam,Thang,Ngay);
                datePickerDialog.show();


            }
        });

        Toast.makeText(getActivity(), ""+dulieu[1], Toast.LENGTH_SHORT).show();
        thongke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check>=2){
                    if(end>star) {
                        if (dulieu[1].toString().toLowerCase().contains("phiếu nhập")) {
                                Cursor cursor = database.Getdata("SELECT * from PHIEUNHAP WHERE Date(NGAY_PN)>='"+tv_star.getText()+"'"
                                        +" and Date(NGAY_PN)<='"+tv_end.getText()+"'");
                            if(cursor.getCount()>0){
                                while (cursor.moveToNext()) {
                                    arrayList_PN.add(new PHIEUNHAP(cursor.getString(0)
                                            , cursor.getString(1)
                                            , cursor.getString(2)
                                            , cursor.getInt(3)));
                                }
                                phieuNhapAdapter = new PhieuNhapAdapter(arrayList_PN, getActivity());
                                recyclerView.setAdapter(phieuNhapAdapter);
                                phieuNhapAdapter.notifyDataSetChanged();
                            }
                            else {
                                AlertDialog.Builder alerdialog= new AlertDialog.Builder(getActivity());
                                alerdialog.setTitle("ERROR");
                                alerdialog.setMessage("Dữ liệu bạn yêu cầu hiện không có!!! Dữ liệu sẽ trả về ban đầu");
                                alerdialog.setPositiveButton("OKE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        refesh_lv_phieunhap();
                                    }
                                });
                                alerdialog.show();
                            }
                            }else if(dulieu[1].toLowerCase().contains("hoá đơn")) {
                            arrayList_HD= new ArrayList<>();
                            Cursor cursor = database.Getdata("SELECT * from HOADON WHERE Date(ngay_hd)>='"+tv_star.getText()+"' and Date(ngay_hd)<='"+tv_end.getText()+"'");
                            Toast.makeText(getActivity(), ""+cursor.getCount(), Toast.LENGTH_SHORT).show();
                            if(cursor.getCount()>0){
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
                            else {
                                AlertDialog.Builder alerdialog= new AlertDialog.Builder(getActivity());
                                alerdialog.setTitle("ERROR");
                                alerdialog.setMessage("Dữ liệu bạn yêu cầu hiện không có!!! Dữ liệu sẽ trả về ban đầu");
                                alerdialog.setPositiveButton("OKE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        refesh_lv_hoadon();
                                    }
                                });
                                alerdialog.show();
                            }
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
        if(phieunhapArrayList.size()>0){
            phieuNhapAdapter= new PhieuNhapAdapter(phieunhapArrayList,getActivity());
            recyclerView.setAdapter(phieuNhapAdapter);
            phieuNhapAdapter.notifyDataSetChanged();
        }
        else {
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
    }
    public void refesh_lv_hoadon(){
        refesh_hoadon();
        if(hoadonArrayList.size()>0){
            hoaDonAdapter= new HoaDonAdapter(hoadonArrayList,getActivity());
            recyclerView.setAdapter(hoaDonAdapter);
            hoaDonAdapter.notifyDataSetChanged();
        }
        else{
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
    }
}
