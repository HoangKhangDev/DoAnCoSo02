package com.rin1903.bookstoremanager.fragment;

import static com.rin1903.bookstoremanager.MainActivity.*;
import static com.rin1903.bookstoremanager.MainActivity.trang;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rin1903.bookstoremanager.MainActivity;
import com.rin1903.bookstoremanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Fragment_Main_Menu extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.cardview_sach_main) CardView cardview_sach;
    @BindView(R.id.cardview_khachhang_main) CardView cardview_khachhang;
    @BindView(R.id.cardview_tacgia_main) CardView cardview_tacgia;
    @BindView(R.id.cardview_nhacungcap_main) CardView cardview_nhacungcap;
    @BindView(R.id.cardview_hoadon_main) CardView cardview_hoadon;
    @BindView(R.id.cardview_phieunhap_main) CardView cardview_phieunhap;
    private  BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private  FragmentTransaction fragmentTransaction;
    private Fragment_HienThi fragment_hienthi;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu,container,false);
        Tag= Fragment_Main_Menu.class.getName();
        unbinder= ButterKnife.bind(this,view);
        bottomNavigationView = getActivity().findViewById(R.id.navbar_bottom);



        bottomNavigationView.setVisibility(View.VISIBLE);
        fragment_hienthi= new Fragment_HienThi();
        getFragmentManager().beginTransaction().add(R.id.fragment_content, fragment_hienthi).addToBackStack(Tag).commit();



        cardview_hoadon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setVisibility(View.GONE);
                Bundle bundle= new Bundle();
                bundle.putString("guidulieu","guidulieu-Hoá Đơn");
                FragmentManager fragmentManager= getFragmentManager();
                fragment_hienthi_replace(fragmentManager,bundle);
            }
        });
        cardview_khachhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setVisibility(View.GONE);
                Bundle bundle= new Bundle();
                bundle.putString("guidulieu","guidulieu-Khách Hàng");
                FragmentManager fragmentManager= getFragmentManager();
                fragment_hienthi_replace(fragmentManager,bundle);
            }
        });
        cardview_nhacungcap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setVisibility(View.GONE);
                Bundle bundle= new Bundle();
                bundle.putString("guidulieu","guidulieu-Nhà Cung Cấp");
                FragmentManager fragmentManager= getFragmentManager();
                fragment_hienthi_replace(fragmentManager,bundle);

            }
        });
        cardview_sach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setVisibility(View.GONE);
                Bundle bundle= new Bundle();
                bundle.putString("guidulieu","guidulieu-Sách");
                FragmentManager fragmentManager= getFragmentManager();
                fragment_hienthi_replace(fragmentManager,bundle);

            }
        });
        cardview_tacgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setVisibility(View.GONE);
                Bundle bundle= new Bundle();
                bundle.putString("guidulieu","guidulieu-Tác Giả");
                FragmentManager fragmentManager= getFragmentManager();
                fragment_hienthi_replace(fragmentManager,bundle);

            }
        });
        cardview_phieunhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setVisibility(View.GONE);
                Bundle bundle= new Bundle();
                bundle.putString("guidulieu","guidulieu-Phiếu Nhập");
                FragmentManager fragmentManager= getFragmentManager();
                fragment_hienthi_replace(fragmentManager,bundle);

            }
        });


        return view;
    }
}
