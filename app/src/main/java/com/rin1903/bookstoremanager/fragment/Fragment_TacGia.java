package com.rin1903.bookstoremanager.fragment;

import static android.app.Activity.RESULT_OK;
import static com.rin1903.bookstoremanager.MainActivity.SELECT_PICTURE;
import static com.rin1903.bookstoremanager.MainActivity.database;
import static com.rin1903.bookstoremanager.MainActivity.dulieu;
import static com.rin1903.bookstoremanager.MainActivity.refesh_theloai;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rin1903.bookstoremanager.MainActivity;
import com.rin1903.bookstoremanager.R;
import com.rin1903.bookstoremanager.SQLite.TACGIA;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

public class Fragment_TacGia extends Fragment {
    Unbinder unbinder;
    private String Matacgia;
    private String check_change_image="";
    @BindView(R.id.img_hinh_tacgia_fragment)  ImageView imghinhtacgia;
    @BindView(R.id.edt_tentacgia_tacgia)  EditText edt_tentacgia;
    @BindView(R.id.edt_diachi_tacgia) EditText edt_diachi;
    @BindView(R.id.spinner_gioitinh_tacgia)  Spinner spinner_gioitinh;
    @BindView(R.id.tv_calendar_tacgia)  TextView tv_ngaysinh;
    @BindView(R.id.img_calendar_tacgia) ImageView img_calendar;
    @BindView(R.id.btn_them_theloai_tacgia) Button btn_them;
    @BindView(R.id.btn_huy_tacgia) Button btn_huy;
    private TACGIA tacgia;
    private String[] dulieu;
    private  Character first;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_tacgia,container,false);
        unbinder= ButterKnife.bind(this,view);
        check_change_image="false";


        List<String> gioitinh = new ArrayList<>();
        gioitinh.add("Nam");
        gioitinh.add("Nữ");
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,gioitinh);
        spinner_gioitinh.setAdapter(adapter);

        Bundle bundle= getArguments();
        if(bundle!=null){
            Toast.makeText(getActivity(), ""+bundle.getString("guidulieu"), Toast.LENGTH_SHORT).show();
            dulieu = bundle.getString("guidulieu").split("_");
            if(dulieu[0].toLowerCase().contains("tao")){
                Cursor cursor=database.Getdata("select MATACGIA from TACGIA order by MATACGIA desc limit 1");
                if(cursor.getCount()==0){
                    Matacgia= "tacgia-01";
                }
                else{
                    String[] tach = new String[0];
                    while (cursor.moveToNext()){
                        tach= cursor.getString(0).split("-");
                    }
                    Matacgia=String.format("%s-%s", tach[0], String.valueOf(Integer.parseInt(tach[1])+1));
                }
            }
            else if(dulieu[0].toLowerCase().contains("chinhsua")){
                    Cursor cursor= database.Getdata("select * from TACGIA where MATACGIA='"+dulieu[2].toString()+"'");
                    while (cursor.moveToNext()){
                      tacgia= new TACGIA(cursor.getString(0)
                              ,cursor.getString(1)
                              ,cursor.getString(2)
                              ,cursor.getString(3)
                              ,cursor.getString(4)
                              ,cursor.getBlob(5));
                        byte[] hinh= tacgia.getHINH_TACGIA();
                        Bitmap bitmap= BitmapFactory.decodeByteArray(hinh,0,hinh.length);
                        imghinhtacgia.setImageBitmap(bitmap);
                        edt_tentacgia.setText(tacgia.getTENTACGIA());
                        edt_diachi.setText(tacgia.getDIACHI_TG());
                        tv_ngaysinh.setText(tacgia.getNGAYSINH_TG());
                        if(tacgia.getGIOITINH_TG().toLowerCase().contains("nam")){
                            spinner_gioitinh.setSelection(0);
                        }
                        else if(tacgia.getGIOITINH_TG().toLowerCase().contains("nữ")) {
                            spinner_gioitinh.setSelection(1);
                        }

                    }
                    btn_them.setText("Update");


            }
        }

        imghinhtacgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TedBottomPicker.with(getActivity()).setTitle("Vui Lòng Chọn Ảnh").setPreviewMaxCount(19)
                        .show(new TedBottomSheetDialogFragment.OnImageSelectedListener() {
                            @Override
                            public void onImageSelected(Uri uri) {
                                Picasso.get().load(uri).into(imghinhtacgia);
                                check_change_image="true";
                            }
                        });
            }
        });

        img_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar= Calendar.getInstance();
                final String chuoi = "";
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year,month,dayOfMonth);
                        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
                        tv_ngaysinh.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },19,03,2000);
                datePickerDialog.show();
            }
        });


        btn_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edt_tentacgia.getText().toString().isEmpty()&
                !edt_diachi.getText().toString().isEmpty()&
                tv_ngaysinh.getText().toString()!="Ngày sinh"){
                    if(check_change_image.contains("true")&dulieu[0].toString().contains("tao"))
                    {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) imghinhtacgia.getDrawable();
                        Bitmap bitmap= bitmapDrawable.getBitmap();
                        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                        byte[] hinhanh= byteArrayOutputStream.toByteArray();
                        database.INSERT_TACGIA(Matacgia,edt_tentacgia.getText().toString(),spinner_gioitinh.getSelectedItem().toString(),
                                tv_ngaysinh.getText().toString(),edt_diachi.getText().toString(), hinhanh);
                        Toast.makeText(getActivity(), "Thêm Thành Công", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                    else if(dulieu[0].toString().toLowerCase().contains("chinhsua")){
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) imghinhtacgia.getDrawable();
                        Bitmap bitmap= bitmapDrawable.getBitmap();
                        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                        byte[] hinhanh= byteArrayOutputStream.toByteArray();
                        tacgia.setTENTACGIA(edt_tentacgia.getText().toString());
                        tacgia.setGIOITINH_TG(spinner_gioitinh.getSelectedItem().toString());
                        tacgia.setHINH_TACGIA(hinhanh);
                        tacgia.setNGAYSINH_TG(tv_ngaysinh.getText().toString());
                        tacgia.setDIACHI_TG(edt_diachi.getText().toString());
                        database.UPDATE_TACGIA(tacgia);
                        Toast.makeText(getActivity(), "Chỉnh Sửa Thành Công", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                    else {
                        Toast.makeText(getActivity(), "Bạn chưa đổi ảnh đại diện", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(getActivity(), "Bạn chưa điền đủ thông tin", Toast.LENGTH_LONG).show();
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

        return view;
    }
}
