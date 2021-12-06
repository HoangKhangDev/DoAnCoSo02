package com.rin1903.bookstoremanager.fragment;

import static com.rin1903.bookstoremanager.MainActivity.sachArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
    @BindView(R.id.scanner_view)
    CodeScannerView scannerview;
    private CodeScanner mCodeScanner;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taohoadon,container,false);
        final Activity activity = getActivity();
        unbinder= ButterKnife.bind(this,view);

        arrayList = new ArrayList<>();
        recyclerView_thongtinhoadon.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView_thongtinhoadon.setLayoutManager(linearLayoutManager);

        mCodeScanner = new CodeScanner(activity, scannerview);
        scannerview.setVisibility(View.GONE);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.refesh_sach();
                        if (result.getText() != null & sachArrayList.size() > 0) {
                            int so = Integer.parseInt(result.getText());
                            if (arrayList.size() > 0) {
                                for (int i = 0; i < sachArrayList.size(); i++) {
                                    for (int j = 0; j < arrayList.size(); j++) {
                                        if(so != arrayList.get(j).getMaSach()){
                                            if (so == sachArrayList.get(i).getMASACH()) {
                                                arrayList.add(new SACH_TRONG_HOADON(sachArrayList.get(i).getTENSACH(),
                                                        sachArrayList.get(i).getMASACH(), sachArrayList.get(i).getSOQUYEN(),
                                                        sachArrayList.get(i).getGIABAN()));
                                                adapter = new ChiTietHoaDonAdapter(getActivity(), arrayList);
                                                recyclerView_thongtinhoadon.setAdapter(adapter);

                                                dialog("Bạn đã thêm "+sachArrayList.get(i).getTENSACH().toString()+" Thành Công!!!\n Bạn có muốn tiếp tục thêm sách???",
                                                        "Tiếp Tục","Huỷ");
                                            }
                                        }
                                        else if(so==sachArrayList.get(i).getMASACH()) {

                                            dialog(sachArrayList.get(i).getTENSACH().toString()+" Đã tồn tại trong hoá đơn!!!\n Bạn có muốn tiếp tục thêm sách???",
                                                    "Tiếp Tục","Huỷ");
                                        }


                                    }

                                }
                            } else {
                                for (int i = 0; i < sachArrayList.size(); i++) {
                                    if (so == sachArrayList.get(i).getMASACH()) {
                                        arrayList.add(new SACH_TRONG_HOADON(sachArrayList.get(i).getTENSACH(),
                                                sachArrayList.get(i).getMASACH(), sachArrayList.get(i).getSOQUYEN(),
                                                sachArrayList.get(i).getGIABAN()));
                                        adapter = new ChiTietHoaDonAdapter(getActivity(), arrayList);
                                        recyclerView_thongtinhoadon.setAdapter(adapter);
                                        dialog("Bạn đã thêm "+sachArrayList.get(i).getTENSACH().toString()+" Thành Công!!!\n Bạn có muốn tiếp tục thêm sách???",
                                                "Tiếp Tục","Huỷ");

                                    }
                                }
                            }
                        }
                    }
                });
            }
        });
        img_quet_sach_hoadon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                scancode();
                scannerview.setVisibility(View.VISIBLE);
                mCodeScanner.startPreview();
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
        arrayList=new ArrayList<>();
        if(adapter!=null){
            if(adapter.getItemCount()!=0){
                arrayList=adapter.getArrayList();
            }
        }

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
                dialog.cancel();

            }
        });
        dialog.show();

    }
}
