package com.rin1903.bookstoremanager.fragment;

import static com.rin1903.bookstoremanager.MainActivity.Tag;
import static com.rin1903.bookstoremanager.MainActivity.database;
import static com.rin1903.bookstoremanager.MainActivity.hoadonArrayList;
import static com.rin1903.bookstoremanager.MainActivity.nhacungcapArrayList;
import static com.rin1903.bookstoremanager.MainActivity.refesh_hoadon;
import static com.rin1903.bookstoremanager.MainActivity.refesh_nhacungcap;
import static com.rin1903.bookstoremanager.MainActivity.refesh_sach;
import static com.rin1903.bookstoremanager.MainActivity.sachArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
import com.rin1903.bookstoremanager.Adapter.ChiTietPhieuNhapAdapter;
import com.rin1903.bookstoremanager.Adapter.ChiTietPhieuNhapAdapter;
import com.rin1903.bookstoremanager.MainActivity;
import com.rin1903.bookstoremanager.R;
import com.rin1903.bookstoremanager.SQLite.SACH_TRONG_HOADON;
import com.rin1903.bookstoremanager.SQLite.SACH_TRONG_PHIEUNHAP;
import com.rin1903.bookstoremanager.SQLite.SACH_TRONG_PHIEUNHAP;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Fragment_TaoPhieuNhap extends Fragment {

    private Unbinder unbinder;
    private ArrayList<SACH_TRONG_PHIEUNHAP> arrayList;
    private ChiTietPhieuNhapAdapter adapter;
    @BindView(R.id.btn_huy_fragment_taophieunhap)
    Button btn_huy;
    @BindView(R.id.btn_thanhtoan_fragment_taophieunhap) Button btn_thanhtoan;
    @BindView(R.id.image_themsach_taophieunhap)
    ImageView img_themsachtheospinner;
    @BindView(R.id.tv_themsachvaohoadon_taophieunhap) TextView tv_themsachvaophieunhap;
    @BindView(R.id.image_themnhacungcap_taophieunhap) ImageView img_themnhacungcapmoi;
    @BindView(R.id.layout_sach_taophieunhap) LinearLayout layout_sach;
    @BindView(R.id.recycleview_fragment_taophieunhap)
    RecyclerView recyclerView_thongtinhoadon;
    @BindView(R.id.spinner_manhacungcap_taophieunhap)
    Spinner spinner_manhacungcap;
    @BindView(R.id.spinner_masach_taophieunhap) Spinner spinner_masach;
    @BindView(R.id.linear_phieunhap_taophieunhap) LinearLayout linearLayout;
    @BindView(R.id.scanner_view)
    CodeScannerView scannerview;
    @BindView(R.id.btn_quetma_taophieunhap) Button btn_quetmabarcode;
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


        arrayList = new ArrayList<>();
        recyclerView_thongtinhoadon.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView_thongtinhoadon.setLayoutManager(linearLayoutManager);

        arrayList=new ArrayList<>();
        linearLayout.setVisibility(View.GONE);
        if(adapter!=null){
            adapter = new ChiTietPhieuNhapAdapter( getActivity(), arrayList);
            recyclerView_thongtinhoadon.setAdapter(adapter);
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
                    arrayList_spinner_nhacungcap.add(String.valueOf(nhacungcapArrayList.get(i).getMANHACUNGCAP()+"_"+nhacungcapArrayList.get(i).getTENNHACUNGCAP()));
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
                    arrayList_spinner_sach.add(String.valueOf(sachArrayList.get(i).getMASACH())+" - "+sachArrayList.get(i).getTENSACH());
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
                database.INSERT_PHIEUNHAP(maphieunhap,manhacungcap,adapter.getthanhtientong());
                Toast.makeText(getActivity(), "Thêm phiếu nhập đơn thành công", Toast.LENGTH_SHORT).show();
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
                database.INSERT_PHIEUNHAP(maphieunhap,manhacungcap,adapter.getthanhtientong());
                Toast.makeText(getActivity(), "Thêm phiếu nhập đơn thành công", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getActivity(), "Chưa có sách trong phiếu nhập vui lòng thêm", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
