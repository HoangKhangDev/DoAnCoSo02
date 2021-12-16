package com.rin1903.bookstoremanager.fragment;

import static com.rin1903.bookstoremanager.MainActivity.Tag;
import static com.rin1903.bookstoremanager.MainActivity.database;
import static com.rin1903.bookstoremanager.MainActivity.dulieu;
import static com.rin1903.bookstoremanager.MainActivity.hoadonArrayList;
import static com.rin1903.bookstoremanager.MainActivity.khachhangArrayList;
import static com.rin1903.bookstoremanager.MainActivity.nhacungcapArrayList;
import static com.rin1903.bookstoremanager.MainActivity.phieunhapArrayList;
import static com.rin1903.bookstoremanager.MainActivity.refesh_hoadon;
import static com.rin1903.bookstoremanager.MainActivity.refesh_khachhang;
import static com.rin1903.bookstoremanager.MainActivity.refesh_nhacungcap;
import static com.rin1903.bookstoremanager.MainActivity.refesh_phieunhap;
import static com.rin1903.bookstoremanager.MainActivity.refesh_sach;
import static com.rin1903.bookstoremanager.MainActivity.refesh_sach_all;
import static com.rin1903.bookstoremanager.MainActivity.refesh_tacgia;
import static com.rin1903.bookstoremanager.MainActivity.refesh_theloai;
import static com.rin1903.bookstoremanager.MainActivity.sachArrayList;
import static com.rin1903.bookstoremanager.MainActivity.tacgiaArrayList;
import static com.rin1903.bookstoremanager.MainActivity.theloaiArrayList;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rin1903.bookstoremanager.Adapter.HoaDonAdapter;
import com.rin1903.bookstoremanager.Adapter.KhachHangAdapter;
import com.rin1903.bookstoremanager.Adapter.NhaCungCapAdapter;
import com.rin1903.bookstoremanager.Adapter.PhieuNhapAdapter;
import com.rin1903.bookstoremanager.Adapter.SachAdapter;
import com.rin1903.bookstoremanager.Adapter.TacGiaAdapter;
import com.rin1903.bookstoremanager.R;
import com.rin1903.bookstoremanager.SQLite.NHACUNGCAP;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Fragment_HienThi extends Fragment {
    Unbinder unbinder;

    private KhachHangAdapter khachHangAdapter;
    private SachAdapter sachAdapter;
    private HoaDonAdapter hoaDonAdapter;
    private NhaCungCapAdapter nhaCungCapAdapter;
    private PhieuNhapAdapter phieuNhapAdapter;
    private TacGiaAdapter tacGiaAdapter;
    @BindView(R.id.search_bar)
    SearchView searchView;
    @BindView(R.id.recycleview_fragment_hienthi)
    RecyclerView recyclerView_hienthi;
    @BindView(R.id.button_float_add_fragment_hienthi) FloatingActionButton btn_add;
    @BindView(R.id.back_fragment_hienthi) ImageView img_back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hienthi,container,false);
        Tag= Fragment_HienThi.class.getName();
        unbinder=ButterKnife.bind(this,view);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity().getSupportFragmentManager()!=null){
                    getActivity().getSupportFragmentManager().popBackStack();
                }

            }
        });



        recyclerView_hienthi.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView_hienthi.setLayoutManager(linearLayoutManager);


        Bundle bundle= getArguments();
        //loadlistview
        if(bundle!=null){
            dulieu=bundle.getString("guidulieu").split("_");


            if(dulieu[1].toLowerCase().contains("phiếu nhập")){
                refesh_phieunhap();

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
                    searchView.setVisibility(View.GONE);
                }
                else {
                    searchView.setVisibility(View.VISIBLE);
                    refesh_lv_phieunhap();
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            phieuNhapAdapter.getFilter().filter(query);
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            phieuNhapAdapter.getFilter().filter(newText);
                            return false;
                        }
                    });
                }


            }
            else if (dulieu[1].toLowerCase().contains("khách hàng")){
                refesh_khachhang();
                if (khachhangArrayList.size()==0){
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
                    searchView.setVisibility(View.GONE);

                }
                else {
                    searchView.setVisibility(View.VISIBLE);
                    refesh_lv_khachhang();
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            khachHangAdapter.getFilter().filter(query);
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            khachHangAdapter.getFilter().filter(newText);
                            return false;
                        }
                    });
                }

            }
            else if (dulieu[1].toLowerCase().contains("sách")){
                refesh_sach();
                if (sachArrayList.size()==0){
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
                    searchView.setVisibility(View.GONE);

                }
                else {
                    searchView.setVisibility(View.VISIBLE);
                    refesh_lv_sach();
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            sachAdapter.getFilter().filter(query);
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            sachAdapter.getFilter().filter(newText);
                            return false;
                        }
                    });
                }
            }
            else if (dulieu[1].toLowerCase().contains("nhà cung cấp")){
                refesh_nhacungcap();
                if (nhacungcapArrayList.size()==0){
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
                    searchView.setVisibility(View.GONE);
                }
                else {
                    searchView.setVisibility(View.VISIBLE);
                    refesh_lv_nhacungcap();
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            nhaCungCapAdapter.getFilter().filter(query);
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            nhaCungCapAdapter.getFilter().filter(newText);
                            return false;
                        }
                    });
                }
            }
            else if (dulieu[1].toLowerCase().contains("hoá đơn")){
                refesh_hoadon();
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
                    searchView.setVisibility(View.GONE);
                }
                else {
                    searchView.setVisibility(View.VISIBLE);
                    refesh_lv_hoadon();
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            hoaDonAdapter.getFilter().filter(query);
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            hoaDonAdapter.getFilter().filter(newText);
                            return false;
                        }
                    });
                }
            }
            else if (dulieu[1].toLowerCase().contains("tác giả")){
                refesh_tacgia();
                if (tacgiaArrayList.size()==0){
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
                    searchView.setVisibility(View.GONE);
                }
                else {
                    searchView.setVisibility(View.VISIBLE);
                    refesh_lv_tacgia();
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            tacGiaAdapter.getFilter().filter(query);
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            tacGiaAdapter.getFilter().filter(newText);
                            return false;
                        }
                    });
                }
            }
        }



        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dulieu[1].toLowerCase().contains("khách hàng")){
                    Bundle bundle1= new Bundle();
                    bundle1.putString("guidulieu","tao_Khách Hàng");
                    Fragment_KhachHang fragment_khachhang=new Fragment_KhachHang();
                    fragment_khachhang.setArguments(bundle1);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content,fragment_khachhang).addToBackStack(Tag).commit();
                }
                else if(dulieu[1].toString().toLowerCase().contains("phiếu nhập"))
                { Bundle bundle1= new Bundle();
                    bundle1.putString("guidulieu","tao_Phiếu Nhập");
                    Fragment_TaoPhieuNhap fragment= new Fragment_TaoPhieuNhap();
                    fragment.setArguments(bundle1);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content,fragment).addToBackStack(Tag).commit();
                }
                else if(dulieu[1].toString().toLowerCase().contains("hoá đơn")){
                    Bundle bundle1= new Bundle();
                    bundle1.putString("guidulieu","tao_Hoá Đơn");
                    Fragment_TaoHoaDon fragment= new Fragment_TaoHoaDon();
                    fragment.setArguments(bundle1);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content,fragment).addToBackStack(Tag).commit();
                }
                else if(dulieu[1].toString().toLowerCase().contains("sách")){
                    Bundle bundle1= new Bundle();
                    bundle1.putString("guidulieu","tao_Sách");
                    Fragment_Sach fragment_sach= new Fragment_Sach();
                    fragment_sach.setArguments(bundle1);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content,fragment_sach).addToBackStack(Tag).commit();
                }
                else if(dulieu[1].toString().toLowerCase().contains("tác giả")){
                    Bundle bundle1= new Bundle();
                    bundle1.putString("guidulieu","tao_Tác Giả_ok");
                    Fragment_TacGia fragment=new Fragment_TacGia();
                    fragment.setArguments(bundle1);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content,fragment).addToBackStack(Tag).commit();

                }
                else if(dulieu[1].toString().toLowerCase().contains("nhà cung cấp")){
                    Bundle bundle1= new Bundle();
                    bundle1.putString("guidulieu","tao_Nhà Cung Cấp_null");
                    Fragment_NhaCungCap fragment=new Fragment_NhaCungCap();
                    fragment.setArguments(bundle1);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content,fragment).addToBackStack(Tag).commit();
                }
            }
        });




        return view;
    }
    public void refesh_lv_phieunhap(){
        refesh_phieunhap();
        phieuNhapAdapter= new PhieuNhapAdapter(phieunhapArrayList,getActivity());
        recyclerView_hienthi.setAdapter(phieuNhapAdapter);
        phieuNhapAdapter.notifyDataSetChanged();
    }
    public void refesh_lv_khachhang(){
        refesh_khachhang();
        khachHangAdapter= new KhachHangAdapter(khachhangArrayList,getActivity());
        recyclerView_hienthi.setAdapter(khachHangAdapter);
        khachHangAdapter.notifyDataSetChanged();
    }
    public void refesh_lv_tacgia(){
        refesh_tacgia();
        tacGiaAdapter= new TacGiaAdapter(tacgiaArrayList,getActivity());
        recyclerView_hienthi.setAdapter(tacGiaAdapter);
        tacGiaAdapter.notifyDataSetChanged();
    }
    public void refesh_lv_sach(){
        refesh_sach();
        sachAdapter= new SachAdapter(sachArrayList,getActivity());
        recyclerView_hienthi.setAdapter(sachAdapter);
        sachAdapter.notifyDataSetChanged();
    }
    public void refesh_lv_nhacungcap(){
        refesh_nhacungcap();
        ArrayList<NHACUNGCAP> arrayList= new ArrayList<>();
        arrayList=nhacungcapArrayList;
        nhaCungCapAdapter= new NhaCungCapAdapter(arrayList,getActivity());
        recyclerView_hienthi.setAdapter(nhaCungCapAdapter);
        nhaCungCapAdapter.notifyDataSetChanged();
    }
    public void refesh_lv_hoadon(){
        refesh_hoadon();
        hoaDonAdapter= new HoaDonAdapter(hoadonArrayList,getActivity());
        recyclerView_hienthi.setAdapter(hoaDonAdapter);
        hoaDonAdapter.notifyDataSetChanged();
    }

}
