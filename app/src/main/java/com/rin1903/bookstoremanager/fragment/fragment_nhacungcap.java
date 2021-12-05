package com.rin1903.bookstoremanager.fragment;

import static android.app.Activity.RESULT_OK;
import static com.rin1903.bookstoremanager.MainActivity.database;
import static com.rin1903.bookstoremanager.MainActivity.dulieu;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.rin1903.bookstoremanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class fragment_nhacungcap extends Fragment {
    private static final int SELECT_PICTURE = 1;
    Unbinder unbinder;
    @BindView(R.id.edt_tennhacungcap_nhacungcap)
    EditText edt_tennhacungcap;
    @BindView(R.id.edt_diachi_nhacungcap) EditText edt_diachi;
    @BindView(R.id.edt_sdt_nhacungcap) EditText edt_sdt;
    @BindView(R.id.img_hinh_nhacungcap_fragment)
    ImageView image;
    @BindView(R.id.btn_them_theloai_nhacungcap)
    Button btn_them;
    @BindView(R.id.btn_huy_theloai_nhacungcap)
    Button btn_huy;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_nhacungcap,container,false);
        unbinder= ButterKnife.bind(this,view);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
        Bundle bundle= getArguments();
        //loadlistview
        if(bundle!=null) {
            dulieu = bundle.getString("guidulieu").split("-");
            if(dulieu[0].toLowerCase().contains("tao")){
                String Ma="";
                Cursor cursor=database.Getdata("select MANHACUNGCAP from NHACUNGCAP order by MANHACUNGCAP desc limit 1");

                if(cursor.getCount()==0){
                    Ma= "khachhang-01";
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
