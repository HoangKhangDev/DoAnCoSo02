package com.rin1903.bookstoremanager.fragment;

import static android.app.Activity.RESULT_OK;
import static com.rin1903.bookstoremanager.MainActivity.SELECT_PICTURE;
import static com.rin1903.bookstoremanager.MainActivity.Tag;
import static com.rin1903.bookstoremanager.MainActivity.database;
import static com.rin1903.bookstoremanager.MainActivity.dulieu;
import static com.rin1903.bookstoremanager.MainActivity.refesh_tacgia;
import static com.rin1903.bookstoremanager.MainActivity.refesh_theloai;
import static com.rin1903.bookstoremanager.MainActivity.sachArrayList;
import static com.rin1903.bookstoremanager.MainActivity.tacgiaArrayList;
import static com.rin1903.bookstoremanager.MainActivity.theloaiArrayList;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.rin1903.bookstoremanager.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Fragment_Sach extends Fragment {
    Unbinder unbinder;
    private int REQUEST_CODE=19;
    private OutputStream outputStream;
    private String check_image_change="";
    private int Masach;
    @BindView(R.id.spinner_trangthai_sach) Spinner spinner_trangthai;
    @BindView(R.id.btn_huy_sach)
    Button btn_huy;
    @BindView(R.id.btn_update_sach) Button btn_update;
    @BindView(R.id.edt_giaban_sach)
    EditText edt_giaban;
    @BindView(R.id.edt_ten_sach) EditText edt_tensach;
    @BindView(R.id.edt_soquyen_sach) EditText edt_soquyen;
    @BindView(R.id.img_add_tacsach_sach)
    ImageView img_add_tacgia;
    @BindView(R.id.img_add_loaisach_sach) ImageView img_add_loaisach;
    @BindView(R.id.img_hinh_sach) ImageView img_sach;
    @BindView(R.id.spinner_tacgia_sach)
    Spinner spinner_tentacgia;
    @BindView(R.id.spinner_tenloaisach_sach) Spinner spinner_tenloaisach;
    @BindView(R.id.img_barcode_sach) ImageView img_barcode;
    @BindView(R.id.img_save_barcode_sach) ImageView img_barcode_save;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_sach,container,false);
        Tag= Fragment_Sach.class.getName();

        unbinder= ButterKnife.bind(this,view);

        check_image_change="false";

        load_trangthaisach();
        reload_loaisach();
        reload_tacgia();

        img_add_tacgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1= new Bundle();
                bundle1.putString("guidulieu","tao-Tác Giả-sach");
                Fragment_TacGia fragment=new Fragment_TacGia();
                fragment.setArguments(bundle1);
                getFragmentManager().beginTransaction().replace(R.id.fragment_content,fragment).addToBackStack(Tag).commit();
            }
        });

        img_add_loaisach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog= new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_theloai);

                EditText edt_tenloai = dialog.findViewById(R.id.edt_tenloaisach_theloai);
                Button btn_them = dialog.findViewById(R.id.btn_them_theloai);
                Button btn_huy = dialog.findViewById(R.id.btn_huy_theloai);
                btn_them.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!edt_tenloai.getText().toString().isEmpty()){
                            if(theloaiArrayList.size()==0){
                                database.INSERT_THELOAI("LS-1",edt_tenloai.getText().toString());
                                Toast.makeText(getActivity(), "Thêm Loại "+edt_tenloai.getText().toString()+" Thành Công", Toast.LENGTH_SHORT).show();
                                edt_tenloai.setText(null);
                                reload_loaisach();
                                dialog.cancel();
                            }
                            else {
                                int so = theloaiArrayList.size()-1;
                                String[] tach= theloaiArrayList.get(so).getMALOAI().split("-");
                                so= Integer.parseInt(tach[1])+1;
                                database.INSERT_THELOAI("LS-"+String.valueOf(so),edt_tenloai.getText().toString());
                                Toast.makeText(getActivity(), "Thêm Loại "+edt_tenloai.getText().toString()+" Thành Công", Toast.LENGTH_SHORT).show();
                                reload_loaisach();
                                dialog.cancel();

                            }
                        }
                    }
                });
                btn_huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();

            }
        });

        img_barcode_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the image from drawable resource as drawable object
                Drawable drawable = img_barcode.getDrawable();

                // Get the bitmap from drawable object
                Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

                File mFile = new File("/barcode/"+String.valueOf(Masach)+".jpg");

                //save image in gallery
                String savedImageURL = MediaStore.Images.Media.insertImage(
                        getActivity().getContentResolver(),
                        bitmap,
                        "Barcode ",
                        "Image of Barcode"
                );
                Uri savedImageURI = Uri.parse(savedImageURL);

            }
        });

        Bundle bundle = getArguments();
        if(bundle!=null) {
            dulieu = bundle.getString("guidulieu").split("-");
            if(dulieu[0].toLowerCase().contains("tao")){
                Cursor cursor=database.Getdata("select MASACH from SACH order by MASACH desc limit 1");
                Toast.makeText(getActivity(), ""+cursor.getCount(), Toast.LENGTH_SHORT).show();
                if(cursor.getCount()==0){
                    Masach= 1000;
                }
                else{
                    while (cursor.moveToNext()){
                        Masach=cursor.getInt(0)+1;
                        Toast.makeText(getActivity(), ""+Masach, Toast.LENGTH_SHORT).show();
                    }
                }

                taomabarcode(String.valueOf(Masach));
                Toast.makeText(getActivity(), ""+Masach, Toast.LENGTH_SHORT).show();
            }
        }
        edt_soquyen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    if(Integer.parseInt(s.toString())>0){
                        spinner_trangthai.setSelection(2);
                    }
                    else {
                        spinner_trangthai.setSelection(0);
                    }
                }
            }
        });


        img_sach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edt_tensach.getText().toString().isEmpty()&!edt_giaban.getText().toString().isEmpty()
                &!edt_soquyen.getText().toString().isEmpty()&!spinner_tentacgia.getSelectedItem().toString().contains("Không có tác giả,vui lòng thêm")
                &!spinner_tenloaisach.getSelectedItem().toString().contains("Không có thể loại,vui lòng thêm")){
                    if(check_image_change.contains("true")){

                        if(sachArrayList.size()>0){
                            String ketqua="1";
                            for(int i=0;i<sachArrayList.size();i++){
                                if(sachArrayList.get(i).getTENSACH().contains(edt_tensach.getText().toString())){
                                    ketqua="0";
                                }
                            }
                            if(ketqua.contains("0")){
                                Dialog dialog= new Dialog(getActivity());
                                dialog.setContentView(R.layout.dialog_xacnhan);

                                Button btn_xacnhan= dialog.findViewById(R.id.btn_xacnhan_dialog_xacnhan);
                                Button btn_huy= dialog.findViewById(R.id.btn_huy_dialog_xacnhan);
                                TextView tv_noidung = dialog.findViewById(R.id.tv_noidung_dialog_xacnhan);

                                tv_noidung.setText("Tên sách này đã tồn tại bạn vẫn muốn tạo chứ???");
                                btn_xacnhan.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        tao_sach();
                                    }
                                });
                                btn_huy.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.cancel();
                                    }
                                });
                                dialog.show();
                            }
                            else {
                                tao_sach();
                            }
                        }
                        else {
                            tao_sach();
                        }

                    }
                    else {
                        Toast.makeText(getActivity(), "Vui lòng đổi ảnh", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Bạn chưa điền đủ thông tin", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        return view;
    }


    private void taomabarcode(String masach){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(masach, BarcodeFormat.CODE_128, 300, 100);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            img_barcode.setImageBitmap(barcodeEncoder.createBitmap(bitMatrix));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
    void reload_loaisach(){
        refesh_theloai();
        ArrayList<String> list= new ArrayList<>();
        if(theloaiArrayList.size()==0){
            list.add("Không có thể loại,vui lòng thêm");
        }
        else {
            for (int i=0;i<theloaiArrayList.size();i++){
                list.add(theloaiArrayList.get(i).getTENLOAI());
            }
        }
        ArrayAdapter adapter= new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item,list);
        spinner_tenloaisach.setAdapter(adapter);
    }
    void reload_tacgia(){
        refesh_tacgia();
        ArrayList<String> list= new ArrayList<>();
        if(tacgiaArrayList.size()==0){
            list.add("Không có tác giả,vui lòng thêm");
        }
        else {
            for (int i=0;i<tacgiaArrayList.size();i++){
                list.add(tacgiaArrayList.get(i).getTENTACGIA());
            }
        }
        ArrayAdapter adapter= new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item,list);
        spinner_tentacgia.setAdapter(adapter);
    }
    void load_trangthaisach(){
        ArrayList<String> list= new ArrayList<>();
        list.add("Ngừng kinh doanh");
        list.add("Hết hàng");
        list.add("Còn hàng");
        ArrayAdapter adapter= new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item,list);
        spinner_trangthai.setAdapter(adapter);
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
                    check_image_change="true";
                    img_sach.setImageURI(selectedImageUri);
                }
            }
        }
    }
    private void tao_sach(){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) img_sach.getDrawable();
        Bitmap bitmap= bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] hinhanh= byteArrayOutputStream.toByteArray();
        refesh_theloai();
        reload_tacgia();
        String maloai = null,matacgia=null;
        Cursor cursor_theloai=database.Getdata("select MALOAI from THELOAI where TENLOAI='"+spinner_tenloaisach.getSelectedItem().toString()+"'");
        Cursor cursor_tacgia= database.Getdata("Select MATACGIA from TACGIA where TENTACGIA='"+spinner_tentacgia.getSelectedItem().toString()+"'");
        while (cursor_tacgia.moveToNext()){
            matacgia=cursor_tacgia.getString(0);
        }
        while (cursor_theloai.moveToNext())
        {
            maloai=cursor_theloai.getString(0);
        }
        database.INSERT_SACH(Masach,maloai,matacgia
                ,edt_tensach.getText().toString()
                ,Integer.parseInt(edt_soquyen.getText().toString())
                ,spinner_trangthai.getSelectedItem().toString()
                ,Integer.parseInt(edt_giaban.getText().toString()),hinhanh);
        Toast.makeText(getActivity(), "Thêm Sách Thành Công", Toast.LENGTH_SHORT).show();
        Fragment_HienThi fragment_hienThi= new Fragment_HienThi();
        Bundle bundle1= new Bundle();
        bundle1.putString("guidulieu","guidulieu-Sách");
        fragment_hienThi.setArguments(bundle1);
        getFragmentManager().beginTransaction().replace(R.id.fragment_content,fragment_hienThi).commit();
    }

}
