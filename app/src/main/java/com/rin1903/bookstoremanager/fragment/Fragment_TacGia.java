package com.rin1903.bookstoremanager.fragment;

import static android.app.Activity.RESULT_OK;
import static com.rin1903.bookstoremanager.MainActivity.SELECT_PICTURE;
import static com.rin1903.bookstoremanager.MainActivity.database;
import static com.rin1903.bookstoremanager.MainActivity.dulieu;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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

import com.rin1903.bookstoremanager.MainActivity;
import com.rin1903.bookstoremanager.R;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Fragment_TacGia extends Fragment {
    Unbinder unbinder;
    private String Matacgia;
    private String check_change_image="";
    @BindView(R.id.img_hinh_tacgia_fragment)  ImageView imghinhtacgia;
    @BindView(R.id.edt_tentacgia_tacgia)  EditText edt_tentacgia;
    @BindView(R.id.edt_sdt_tacgia) EditText edt_sdttacgia;
    @BindView(R.id.edt_diachi_tacgia) EditText edt_diachi;
    @BindView(R.id.spinner_gioitinh_tacgia)  Spinner spinner_gioitinh;
    @BindView(R.id.tv_calendar_tacgia)  TextView tv_ngaysinh;
    @BindView(R.id.tv_Tieude_dialog_tacgia) TextView tieude_tacgia;
    @BindView(R.id.img_calendar_tacgia) ImageView img_calendar;
    @BindView(R.id.btn_them_theloai_tacgia) Button btn_them;
    @BindView(R.id.btn_huy_tacgia) Button btn_huy;
    private   Character first;
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
            dulieu = bundle.getString("guidulieu").split("-");
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
        }

        imghinhtacgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

        img_calendar.setOnClickListener(new View.OnClickListener() {
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
                        tv_ngaysinh.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },Nam,Thang,Ngay);
                datePickerDialog.show();
            }
        });
        edt_sdttacgia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty()){
                    first = s.toString().charAt(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    first = s.toString().charAt(0);
                }
            }
        });

        btn_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edt_tentacgia.getText().toString().isEmpty()&
                !edt_diachi.getText().toString().isEmpty()&
                !edt_sdttacgia.getText().toString().isEmpty()&
                tv_ngaysinh.getText().toString()!="dd/mm/yyyy"){
                    if(check_change_image.contains("false"))
                    {
                        Toast.makeText(getActivity(), "Bạn chưa đổi ảnh đại diện", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(edt_sdttacgia.getText().length()<10){
                            Toast.makeText(getActivity(), "Bạn chưa nhập đủ 10 số!!!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if(first.toString().contains("0")){
                                BitmapDrawable bitmapDrawable = (BitmapDrawable) imghinhtacgia.getDrawable();
                                Bitmap bitmap= bitmapDrawable.getBitmap();
                                ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                                byte[] hinhanh= byteArrayOutputStream.toByteArray();
                                database.INSERT_TACGIA(Matacgia,edt_tentacgia.getText().toString(),spinner_gioitinh.getSelectedItem().toString(),
                                        tv_ngaysinh.getText().toString(),edt_diachi.getText().toString(), hinhanh);
                                Toast.makeText(getActivity(), "Thêm Thành Công", Toast.LENGTH_SHORT).show();
                                if (dulieu[2].contains("sach")){
                                    if(getFragmentManager()!=null){
                                        getFragmentManager().popBackStack();
                                    }
                                }
                                else {
                                    Bundle bundle1= new Bundle();
                                    bundle1.putString("guidulieu","guidulieu-tác giả");
                                    Fragment_HienThi fragment_hienThi= new Fragment_HienThi();
                                    fragment_hienThi.setArguments(bundle1);
                                    getFragmentManager().beginTransaction().replace(R.id.fragment_content,fragment_hienThi).commit();
                                }
                            }
                            else {
                                Toast.makeText(getActivity(), "Số điện thoại bắt đầu bằng 0", Toast.LENGTH_SHORT).show();
                            }
                        }
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
                    check_change_image="true";
                    imghinhtacgia.setImageURI(selectedImageUri);
                }
            }
        }
    }
}
