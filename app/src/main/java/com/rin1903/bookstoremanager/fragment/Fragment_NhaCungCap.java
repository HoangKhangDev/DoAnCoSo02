package com.rin1903.bookstoremanager.fragment;

import static android.app.Activity.RESULT_OK;
import static com.rin1903.bookstoremanager.MainActivity.database;
import static com.rin1903.bookstoremanager.MainActivity.dulieu;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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

import com.rin1903.bookstoremanager.R;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Fragment_NhaCungCap extends Fragment {
    private static final int SELECT_PICTURE = 1;
    Unbinder unbinder;
    private  String Ma="";
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
        //loadlistview
        if(bundle!=null) {
            dulieu = bundle.getString("guidulieu").split("-");
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
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

        btn_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edt_tennhacungcap.getText().toString().isEmpty()&!edt_diachi.getText().toString().isEmpty()
                &!edt_sdt.getText().toString().isEmpty()&image.getDrawable()!= getResources().getDrawable(R.drawable.no_pictures)){
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) image.getDrawable();
                    Bitmap bitmap= bitmapDrawable.getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                    byte[] hinhanh= byteArrayOutputStream.toByteArray();
                    database.INSERT_NHACUNGCAP(Ma,edt_tennhacungcap.getText().toString(),edt_diachi.getText().toString(),edt_sdt.getText().toString(),hinhanh);
                    Toast.makeText(getActivity(), "Thêm Nhà Cung Cấp Thành Công", Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                }
            }
        });

        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getFragmentManager()!=null){
                    getFragmentManager().popBackStack();
                }
            }
        });




        return view;
    }


    //themhinhanh
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }
    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    image.setImageURI(selectedImageUri);
                }
            }
        }
    }
}
