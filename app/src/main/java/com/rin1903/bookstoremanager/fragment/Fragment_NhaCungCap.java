package com.rin1903.bookstoremanager.fragment;

import static android.app.Activity.RESULT_OK;
import static com.rin1903.bookstoremanager.MainActivity.database;
import static com.rin1903.bookstoremanager.MainActivity.dulieu;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rin1903.bookstoremanager.R;
import com.rin1903.bookstoremanager.SQLite.NHACUNGCAP;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

public class Fragment_NhaCungCap extends Fragment {
    private static final int SELECT_PICTURE = 1;
    Unbinder unbinder;
    private  String Ma="";
    private String check_image_change;
    private NHACUNGCAP nhacungcap;
    private String dulieu[];
    @BindView(R.id.edt_tennhacungcap_nhacungcap) EditText edt_tennhacungcap;
    @BindView(R.id.edt_diachi_nhacungcap) EditText edt_diachi;
    @BindView(R.id.edt_sdt_nhacungcap) EditText edt_sdt;
    @BindView(R.id.img_hinh_nhacungcap_fragment) ImageView image;
    @BindView(R.id.btn_them_theloai_nhacungcap) Button btn_them;
    @BindView(R.id.btn_huy_theloai_nhacungcap) Button btn_huy;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_nhacungcap,container,false);
        unbinder= ButterKnife.bind(this,view);

        Bundle bundle= getArguments();
        check_image_change="false";

        //loadlistvie
        if(bundle!=null) {
            dulieu=bundle.getString("guidulieu").split("_");
            Toast.makeText(getActivity(), ""+bundle.getString("guidulieu"), Toast.LENGTH_SHORT).show();
            if(dulieu[0].toLowerCase().contains("tao")){
                Cursor cursor=database.Getdata("select MANHACUNGCAP from NHACUNGCAP order by MANHACUNGCAP desc limit 1");
                if(cursor.getCount()==0){
                    Ma= "nhacungcap-1";
                }
                else{
                    String[] tach = new String[0];
                    while (cursor.moveToNext()){
                        tach= cursor.getString(0).split("-");
                    }
                    Ma=String.format("%s-%s", tach[0], String.valueOf(Integer.parseInt(tach[1])+1));
                }

            }
            else if(dulieu[0].toLowerCase().contains("chinhsua")){
                btn_them.setText("Update");
                Cursor cursor= database.Getdata("select * from MANHACUNGCAP='"+dulieu[2].toString()+"'");
                while (cursor.moveToNext()){
                   nhacungcap= new NHACUNGCAP(cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getBlob(4));
                }
                edt_tennhacungcap.setText(nhacungcap.getTENNHACUNGCAP());
                edt_diachi.setText(nhacungcap.getDIACHI_NCC());
                edt_sdt.setText(nhacungcap.getSDT_NCC());
                byte[] hinh= nhacungcap.getHINH_NHACUNGCAP();
                Bitmap bitmap= BitmapFactory.decodeByteArray(hinh,0,hinh.length);
                image.setImageBitmap(bitmap);
            }
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TedBottomPicker.with(getActivity()).setTitle("Vui Lòng Chọn Ảnh").setPreviewMaxCount(19)
                        .show(new TedBottomSheetDialogFragment.OnImageSelectedListener() {
                            @Override
                            public void onImageSelected(Uri uri) {
                                Picasso.get().load(uri).into(image);
                                check_image_change="true";
                            }
                        });
            }
        });

        btn_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), ""+dulieu[0], Toast.LENGTH_SHORT).show();
                if(!edt_tennhacungcap.getText().toString().isEmpty()&!edt_diachi.getText().toString().isEmpty()
                &!edt_sdt.getText().toString().isEmpty()&dulieu[0].toLowerCase().contains("tao")
                        &check_image_change.contains("true")){
                       BitmapDrawable bitmapDrawable = (BitmapDrawable) image.getDrawable();
                       Bitmap bitmap= bitmapDrawable.getBitmap();
                       ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
                       bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                       byte[] hinhanh= byteArrayOutputStream.toByteArray();
                       database.INSERT_NHACUNGCAP(Ma,edt_tennhacungcap.getText().toString(),edt_diachi.getText().toString(),edt_sdt.getText().toString(),hinhanh);
                       Toast.makeText(getActivity(), "Thêm Nhà Cung Cấp Thành Công", Toast.LENGTH_SHORT).show();
                       getActivity().getSupportFragmentManager().popBackStack();


                }
                else if(dulieu[0].toLowerCase().toString().contains("chinhsua")) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) image.getDrawable();
                    Bitmap bitmap= bitmapDrawable.getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                    byte[] hinhanh= byteArrayOutputStream.toByteArray();
                    nhacungcap.setHINH_NHACUNGCAP(hinhanh);
                    nhacungcap.setTENNHACUNGCAP(edt_tennhacungcap.getText().toString());
                    nhacungcap.setDIACHI_NCC(edt_diachi.getText().toString());
                    nhacungcap.setSDT_NCC(edt_sdt.getText().toString());
                    database.UPDATE_NHACUNGCAP(nhacungcap);
                    Toast.makeText(getActivity(), "Chỉnh sửa Nhà Cung Cấp Thành Công", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                else {
                    Toast.makeText(getActivity(), "Bạn chưa điền đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity().getSupportFragmentManager()!=null){
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });




        return view;
    }


}
