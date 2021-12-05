package com.rin1903.bookstoremanager.fragment;

import static android.app.Activity.RESULT_OK;
import static com.rin1903.bookstoremanager.MainActivity.database;
import static com.rin1903.bookstoremanager.MainActivity.dulieu;

import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.rin1903.bookstoremanager.R;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Fragment_KhachHang extends Fragment {
    Unbinder unbinder;

    private static final int SELECT_PICTURE = 1;

    private String selectedImagePath;
    private String makhachhang="";

    @BindView(R.id.tv_Tieude_dialog_Khachhang) TextView tv_tentieude;
    @BindView(R.id.img_hinh_khachhang_dialog) ImageView img_hinhkhachhang;
    @BindView(R.id.edt_diachi_khachhang) EditText edt_diachi;
    @BindView(R.id.edt_sdt_khachhang) EditText edt_sdtkhachhang;
    @BindView(R.id.tv_calendar_khachhang) TextView tv_calendar_khachhang;
    @BindView(R.id.img_calendar_khachhang) ImageView img_calendar_khachhang;
    @BindView(R.id.edt_tenkhachhang_khachhang) EditText edt_tenkhachhang;
    @BindView(R.id.btn_them_khachhang) Button btn_them_khachhang;
    @BindView(R.id.btn_huy_khachhang) Button btn_huy_khachhang;
    @BindView(R.id.spinner_gioitinh_khachhang)
    Spinner spinner;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_khachhang,container,false);
        unbinder= ButterKnife.bind(this,view);

        List<String> gioitinh = new ArrayList<>();
        gioitinh.add("Nam");
        gioitinh.add("Nữ");
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,gioitinh);
        spinner.setAdapter(adapter);

        Bundle bundle= getArguments();
        //loadlistview
        if(bundle!=null) {
            dulieu = bundle.getString("guidulieu").split("-");
            if(dulieu[0].toLowerCase().contains("tao")){
                Cursor cursor=database.Getdata("select MAKHACHHANG from KHACHHANG order by MAKHACHHANG desc limit 1");

                if(cursor.getCount()==0){
                    makhachhang= "khachhang-01";
                }
                else{
                    String[] tach = new String[0];
                    while (cursor.moveToNext()){
                        tach= cursor.getString(0).split("-");
                    }
                    makhachhang=String.format("%s-%s", tach[0], String.valueOf(Integer.parseInt(tach[1])+1));
                }
            }
        }



        img_calendar_khachhang.setOnClickListener(new View.OnClickListener() {
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
                        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
                        tv_calendar_khachhang.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },Nam,Thang,Ngay);
                datePickerDialog.show();
            }
        });

        img_hinhkhachhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               imageChooser();
            }
        });

        btn_them_khachhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edt_tenkhachhang.getText().toString().isEmpty()
                &!edt_diachi.getText().toString().isEmpty()
                &!edt_sdtkhachhang.getText().toString().isEmpty()
                &tv_calendar_khachhang.getText().toString()!="dd/mm/yyyy"
                &img_hinhkhachhang.getDrawable()!= getResources().getDrawable(R.drawable.no_pictures)){
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) img_hinhkhachhang.getDrawable();
                    Bitmap bitmap= bitmapDrawable.getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                    byte[] hinhanh= byteArrayOutputStream.toByteArray();
                    database.INSERT_KHACHHANG(makhachhang,edt_tenkhachhang.getText().toString(),spinner.getSelectedItem().toString(),
                            tv_calendar_khachhang.getText().toString(),edt_sdtkhachhang.getText().toString(),edt_diachi.getText().toString(), hinhanh);
                    Toast.makeText(getActivity(), "Thêm Thành Công", Toast.LENGTH_SHORT).show();
                    if(getFragmentManager()!=null){
                        getFragmentManager().popBackStack();
                    }
                }
            }
        });
        btn_huy_khachhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    img_hinhkhachhang.setImageURI(selectedImageUri);
                }
            }
        }
    }



}
