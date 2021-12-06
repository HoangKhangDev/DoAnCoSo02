package com.rin1903.bookstoremanager.fragment;

import static com.rin1903.bookstoremanager.MainActivity.Tag;
import static com.rin1903.bookstoremanager.MainActivity.database;
import static com.rin1903.bookstoremanager.MainActivity.khachhangArrayList;
import static com.rin1903.bookstoremanager.MainActivity.refesh_khachhang;
import static com.rin1903.bookstoremanager.MainActivity.refesh_sach;
import static com.rin1903.bookstoremanager.MainActivity.sachArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.rin1903.bookstoremanager.Adapter.ChiTietHoaDonAdapter;
import com.rin1903.bookstoremanager.MainActivity;
import com.rin1903.bookstoremanager.R;
import com.rin1903.bookstoremanager.SQLite.SACH_TRONG_HOADON;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Fragment_TaoHoaDon extends Fragment {

    private Unbinder unbinder;
    private ArrayList<SACH_TRONG_HOADON> arrayList;
    private ChiTietHoaDonAdapter adapter;
    @BindView(R.id.btn_huy_fragment_taohoadon)
    Button btn_huy;
    @BindView(R.id.btn_thanhtoan_fragment_taohoadon) Button btn_thanhtoan;
    @BindView(R.id.image_themsach_taohoadon)
    ImageView img_themsachtheospinner;
    @BindView(R.id.tv_themsachvaohoadon_taohoadon) TextView tv_themsachvaohoadon;
    @BindView(R.id.image_themkhachhangmoi_taohoadon) ImageView img_themkhachhangmoi;
    @BindView(R.id.layout_sach_taohoadon) LinearLayout layout_sach;
    @BindView(R.id.tv_tensach_taohoadon) TextView tv_tensach;
    @BindView(R.id.tv_tenkhachhang_taohoadon) TextView tv_tenkhachhang;
    @BindView(R.id.recycleview_fragment_taohoadon)
    RecyclerView recyclerView_thongtinhoadon;
    @BindView(R.id.spinner_makhachhang_taohoadon)
    Spinner spinner_makhachhang;
    @BindView(R.id.spinner_masach_taohoadon) Spinner spinner_masach;
    @BindView(R.id.linear_hoadon_taohoadon)
    LinearLayout linearLayout;
    @BindView(R.id.scanner_view)
    CodeScannerView scannerview;
    @BindView(R.id.btn_quetma_taohoadon) Button btn_quetmabarcode;
    private CodeScanner mCodeScanner;
    private int i,j;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taohoadon,container,false);
        final Activity activity = getActivity();
        unbinder= ButterKnife.bind(this,view);
        Tag= Fragment_TaoHoaDon.class.getName();

        refesh_spinner_sach();
        refesh_spinner_khachhang();


        arrayList = new ArrayList<>();
        recyclerView_thongtinhoadon.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView_thongtinhoadon.setLayoutManager(linearLayoutManager);

        arrayList=new ArrayList<>();



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
                        if(sachArrayList.size()>0){
                            for(i=0;i<sachArrayList.size();i++){
                                if (so==sachArrayList.get(i).getMASACH()){
                                    if(arrayList.size()>0)
                                    {
                                       if( kiemtra_sachtronghoadon(sachArrayList.get(i).getMASACH())!=0)
                                       {
                                           arrayList.add(new SACH_TRONG_HOADON(sachArrayList.get(i).getTENSACH(),sachArrayList.get(i).getMASACH(),
                                                   sachArrayList.get(i).getSOQUYEN(),sachArrayList.get(i).getGIABAN()));
                                           adapter= new ChiTietHoaDonAdapter(getActivity(),arrayList);
                                           recyclerView_thongtinhoadon.setAdapter(adapter);
                                           dialog(sachArrayList.get(i).getTENSACH()+" đã được thêm thành công! \n Bạn có muốn thêm sách khác không?","Tiếp tục","Rời Khỏi");
                                           break;
                                       }
                                       else {
                                           dialog(sachArrayList.get(i).getTENSACH()+" đã tồn tại! \n Bạn có muốn thêm sách khác không?","Tiếp tục","Rời Khỏi");
                                       }

                                    }
                                    else {
                                        arrayList.add(new SACH_TRONG_HOADON(sachArrayList.get(i).getTENSACH(),sachArrayList.get(i).getMASACH(),
                                                sachArrayList.get(i).getSOQUYEN(),sachArrayList.get(i).getGIABAN()));
                                        adapter= new ChiTietHoaDonAdapter(getActivity(),arrayList);
                                        recyclerView_thongtinhoadon.setAdapter(adapter);
                                        dialog(sachArrayList.get(i).getTENSACH()+" đã được thêm thành công! \n Bạn có muốn thêm sách khác không?","Tiếp tục","Rời Khỏi");
                                        break;
                                    }
                                }
                                else {
                                    dialog(sachArrayList.get(i).getTENSACH()+" Chưa có sách nào vui lòng thêm! \n Bạn có muốn thêm sách khác không?","Thêm","Rời Khỏi");
                                    break;
                                }
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "Chưa có sách nào vui lòng thêm", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });



        img_themkhachhangmoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1= new Bundle();
                bundle1.putString("guidulieu","tao-Khách Hàng-sach");
                Fragment_KhachHang fragment=new Fragment_KhachHang();
                fragment.setArguments(bundle1);
                getFragmentManager().beginTransaction().replace(R.id.fragment_content,fragment).addToBackStack(Tag).commit();
            }
        });

        img_themsachtheospinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1= new Bundle();
                bundle1.putString("guidulieu","tao-Sách-sach");
                Fragment_Sach fragment=new Fragment_Sach();
                fragment.setArguments(bundle1);
                getFragmentManager().beginTransaction().replace(R.id.fragment_content,fragment).addToBackStack(Tag).commit();
            }
        });

        tv_themsachvaohoadon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refesh_sach();
                refesh_adapter();
                int dem=0;
                String[] mang=spinner_masach.getSelectedItem().toString().split(" - ");
                int so= Integer.parseInt(mang[0].toString().trim());
                if(sachArrayList.size()>0){
                    for (i=0;i<sachArrayList.size();i++){
                        if(so == sachArrayList.get(i).getMASACH()&sachArrayList.get(i).getSOQUYEN()>0){
                            if(arrayList.size()>0){
                                if(kiemtra_sachtronghoadon(sachArrayList.get(i).getMASACH())!=0){
                                    Toast.makeText(getActivity(), "Đã tồn tại sách này trong hoá đơn", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    arrayList.add(new SACH_TRONG_HOADON(sachArrayList.get(i).getTENSACH(),sachArrayList.get(i).getMASACH()
                                            ,sachArrayList.get(i).getSOQUYEN(),sachArrayList.get(i).getGIABAN()));
                                    adapter= new ChiTietHoaDonAdapter(getActivity(),arrayList);
                                    recyclerView_thongtinhoadon.setAdapter(adapter);
                                }
                            }
                            else {
                                arrayList.add(new SACH_TRONG_HOADON(sachArrayList.get(i).getTENSACH(),sachArrayList.get(i).getMASACH()
                                ,sachArrayList.get(i).getSOQUYEN(),sachArrayList.get(i).getGIABAN()));
                                adapter= new ChiTietHoaDonAdapter(getActivity(),arrayList);
                                recyclerView_thongtinhoadon.setAdapter(adapter);
                            }
                        }
                    }
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
                    tv_themsachvaohoadon.setVisibility(View.GONE);
                    layout_sach.setVisibility(View.GONE);
                    refesh_adapter();
                    scannerview.setVisibility(View.VISIBLE);
                    mCodeScanner.startPreview();
                    btn_quetmabarcode.setText("Huỷ");
                }
                else {
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
                if(getFragmentManager()!=null){
                    getFragmentManager().popBackStack();
                }
            }
        });

        btn_thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refesh_adapter();
                StringBuilder chuoi= new StringBuilder("Thông Tin Hoá Đơn \n Tên Sách \t Giá Bán \t Số Lượng \t Thành Tiền ");
                if(arrayList.size()>0){
                    for (i=0;i<arrayList.size();i++){
                        chuoi.append("\n Tên Sách: ").append(arrayList.get(i).getTenSach()).append("\t").append(String.valueOf(arrayList.get(i).getGiaban())).append("\t").append(String.valueOf(arrayList.get(i).getSoluongtronghoadon())).append("\t").append(String.valueOf(arrayList.get(i).getThanhtien()));
                    }
                    chuoi.append("\n Tổng thành tiền:").append(adapter.getthanhtientong());
                    Dialog dialog = new Dialog(getActivity());
                    dialog.setTitle(chuoi);
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
                tv_themsachvaohoadon.setVisibility(View.VISIBLE);
                layout_sach.setVisibility(View.VISIBLE);
                btn_quetmabarcode.setText("Quét Mã");
                dialog.cancel();

            }
        });
        dialog.show();

    }
    public void refesh_spinner_khachhang(){
        refesh_khachhang();
        ArrayList<String> arrayList_spinner_khachhang= new ArrayList<>();
        if(khachhangArrayList.size()>0){
            for(i=0;i<khachhangArrayList.size();i++){
                arrayList_spinner_khachhang.add(String.valueOf(khachhangArrayList.get(i).getMAKHACHHANG()+" - "+khachhangArrayList.get(i).getTENKHACHHANG()));
            }
        }  else {
            arrayList_spinner_khachhang.add("Vui Lòng Thêm Khách Hàng");
        }
        ArrayAdapter adapter_khachhang= new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,arrayList_spinner_khachhang);
        spinner_makhachhang.setAdapter(adapter_khachhang);

    }
    public void refesh_spinner_sach() {
        refesh_sach();
        ArrayList<String> arrayList_spinner_sach= new ArrayList<>();
        if(sachArrayList.size()>0){
            for(i=0;i<sachArrayList.size();i++){
                if(sachArrayList.get(i).getSOQUYEN()>0){
                    arrayList_spinner_sach.add(String.valueOf(sachArrayList.get(i).getMASACH())+" - "+sachArrayList.get(i).getTENSACH());
                }
            }
        }
        else {
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
    public int kiemtra_sachtronghoadon(int so){
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

}
