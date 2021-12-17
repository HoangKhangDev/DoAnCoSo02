package com.rin1903.bookstoremanager.fragment;

import static com.rin1903.bookstoremanager.MainActivity.Tag;
import static com.rin1903.bookstoremanager.MainActivity.database;
import static com.rin1903.bookstoremanager.MainActivity.dulieu;
import static com.rin1903.bookstoremanager.MainActivity.refesh_tacgia;
import static com.rin1903.bookstoremanager.MainActivity.refesh_theloai;
import static com.rin1903.bookstoremanager.MainActivity.sachArrayList;
import static com.rin1903.bookstoremanager.MainActivity.tacgiaArrayList;
import static com.rin1903.bookstoremanager.MainActivity.theloaiArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.rin1903.bookstoremanager.R;
import com.rin1903.bookstoremanager.SQLite.SACH;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

public class Fragment_Sach extends Fragment {
    Unbinder unbinder;
    private int REQUEST_CODE=19;
    private OutputStream outputStream;
    private String check_image_change="";
    private int Masach;
    private SACH sach;
    @BindView(R.id.spinner_trangthai_sach) Spinner spinner_trangthai;
    @BindView(R.id.btn_huy_sach)
    Button btn_huy;
    @BindView(R.id.btn_update_sach) Button btn_update;
    @BindView(R.id.edt_giaban_sach) BootstrapEditText edt_giaban;
    @BindView(R.id.edt_ten_sach) BootstrapEditText edt_tensach;
    @BindView(R.id.edt_soquyen_sach) BootstrapEditText edt_soquyen;
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

        Bundle bundle = getArguments();
        if(bundle!=null) {
            dulieu = bundle.getString("guidulieu").split("_");
            if(dulieu[0].toLowerCase().contains("tao")){
                Cursor cursor=database.Getdata("select MASACH from SACH order by MASACH desc limit 1");
                if(cursor.getCount()==0){
                    Masach= 1000;
                }
                else{
                    while (cursor.moveToNext()){
                        Masach=cursor.getInt(0)+1;
                    }
                }

                taomabarcode(String.valueOf(Masach));
            }
            else if(dulieu[0].toLowerCase().contains("chinhsua")){
                btn_update.setText("Update");
                Masach= Integer.parseInt(dulieu[2]);
                Cursor cursor= database.Getdata("select * from SACH where MASACH="+Integer.parseInt(dulieu[2].toString()));
                while (cursor.moveToNext()){
                    sach= new SACH(cursor.getInt(0)
                            ,cursor.getString(1)
                            ,cursor.getString(2)
                            ,cursor.getString(3)
                            ,cursor.getInt(4)
                            ,cursor.getString(5)
                            ,cursor.getInt(6)
                            ,cursor.getBlob(7));
                    taomabarcode(String.valueOf(sach.getMASACH()));
                    byte[] hinh= sach.getHINH_SACH();
                    Bitmap bitmap= BitmapFactory.decodeByteArray(hinh,0,hinh.length);
                    img_sach.setImageBitmap(bitmap);
                    edt_tensach.setText(sach.getTENSACH());
                    edt_giaban.setText(String.valueOf(sach.getGIABAN()));
                    edt_soquyen.setText(String.valueOf(sach.getSOQUYEN()));
                    reload_loaisach();
                    String tenloaisach="";


                    Cursor cursor1= database.Getdata("select TENLOAI from THELOAI where MALOAI='"+sach.getMALOAI()+"'");
                    while (cursor1.moveToNext()){
                        tenloaisach=cursor1.getString(0);
                    }
                    if(spinner_tenloaisach.getCount()>0){
                        for (int ii=0;ii<spinner_tenloaisach.getCount();ii++){
                            spinner_tenloaisach.setSelection(ii);
                            if(spinner_tenloaisach.getSelectedItem().toString().toLowerCase().contains(tenloaisach)){
                                break;
                            }
                        }
                    }
                    if(spinner_trangthai.getCount()>0){
                        for (int i=0;i<spinner_trangthai.getCount();i++){
                            spinner_trangthai.setSelection(i);
                            if(spinner_trangthai.getSelectedItem().toString().toLowerCase().contains(sach.getTRANGTHAI())){
                                break;
                            }
                        }
                    }
                    Cursor cursor2= database.Getdata("select TENTACGIA from TACGIA where MATACGIA='"+sach.getMATACGIA()+"'");
                    while (cursor1.moveToNext()){
                        tenloaisach=cursor1.getString(0);
                    }
                    if(spinner_tentacgia.getCount()>0){
                        for (int ii=0;ii<spinner_tentacgia.getCount();ii++){
                            spinner_tentacgia.setSelection(ii);
                            if(spinner_tentacgia.getSelectedItem().toString().toLowerCase().contains(tenloaisach)){
                                break;
                            }
                        }
                    }
                }
            }
            else {
                Cursor cursor= database.Getdata("select * from SACH where MASACH="+Integer.parseInt(dulieu[2].toString()));
                while (cursor.moveToNext()){
                    sach= new SACH(cursor.getInt(0)
                            ,cursor.getString(1)
                            ,cursor.getString(2)
                            ,cursor.getString(3)
                            ,cursor.getInt(4)
                            ,cursor.getString(5)
                            ,cursor.getInt(6)
                            ,cursor.getBlob(7));
                    taomabarcode(String.valueOf(sach.getMASACH()));
                    byte[] hinh= sach.getHINH_SACH();
                    Bitmap bitmap= BitmapFactory.decodeByteArray(hinh,0,hinh.length);
                    img_sach.setImageBitmap(bitmap);
                    edt_tensach.setText(sach.getTENSACH());
                    edt_giaban.setText(String.valueOf(sach.getGIABAN()));
                    edt_soquyen.setText(String.valueOf(sach.getSOQUYEN()));
                    reload_loaisach();
                    String tenloaisach="";


                    Cursor cursor1= database.Getdata("select TENLOAI from THELOAI where MALOAI='"+sach.getMALOAI()+"'");
                    while (cursor1.moveToNext()){
                        tenloaisach=cursor1.getString(0);
                    }
                    if(spinner_tenloaisach.getCount()>0){
                        for (int ii=0;ii<spinner_tenloaisach.getCount();ii++){
                            spinner_tenloaisach.setSelection(ii);
                            if(spinner_tenloaisach.getSelectedItem().toString().toLowerCase().contains(tenloaisach)){
                                break;
                            }
                        }
                    }
                    if(spinner_trangthai.getCount()>0){
                        for (int i=0;i<spinner_trangthai.getCount();i++){
                            spinner_trangthai.setSelection(i);
                            if(spinner_trangthai.getSelectedItem().toString().toLowerCase().contains(sach.getTRANGTHAI())){
                                break;
                            }
                        }
                    }
                    Cursor cursor2= database.Getdata("select TENTACGIA from TACGIA where MATACGIA='"+sach.getMATACGIA()+"'");
                    while (cursor1.moveToNext()){
                        tenloaisach=cursor1.getString(0);
                    }
                    if(spinner_tentacgia.getCount()>0){
                        for (int ii=0;ii<spinner_tentacgia.getCount();ii++){
                            spinner_tentacgia.setSelection(ii);
                            if(spinner_tentacgia.getSelectedItem().toString().toLowerCase().contains(tenloaisach)){
                                break;
                            }
                        }
                    }
                }
                edt_soquyen.setEnabled(false);
                edt_giaban.setEnabled(false);
                edt_tensach.setEnabled(false);
                spinner_tenloaisach.setEnabled(false);
                spinner_tentacgia.setEnabled(false);
                spinner_trangthai.setEnabled(false);
                btn_update.setVisibility(View.GONE);
                img_sach.setEnabled(false);
                img_add_loaisach.setVisibility(View.GONE);
                img_add_tacgia.setVisibility(View.GONE);
                img_barcode_save.setVisibility(View.GONE);
            }

        }

        img_add_tacgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1= new Bundle();
                bundle1.putString("guidulieu","tao_Tác Giả_sach");
                Fragment_TacGia fragment=new Fragment_TacGia();
                fragment.setArguments(bundle1);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content,fragment).addToBackStack(Tag).commit();
            }
        });

        img_add_loaisach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog= new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_theloai);

                BootstrapEditText edt_tenloai = dialog.findViewById(R.id.edt_tenloaisach_theloai);
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
                try {
                    File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"QuanLyCuaHangSach");
                    folder.mkdirs();
                    BitmapDrawable drawable = (BitmapDrawable) img_barcode.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();

                    if(!edt_tensach.getText().toString().trim().isEmpty()){
                        File file = new File(folder,
                                edt_tensach.getText().toString()+"_"+ Masach+".jpg");
                        outputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                        Toast.makeText(getActivity(),"Ảnh đã được lưu tại "+file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
                        outputStream.flush();
                        outputStream.close();
                    }
                    else {
                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                                 Masach+".jpg");
                        outputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                        Toast.makeText(getActivity(),"Ảnh đã được lưu tại "+file.getAbsolutePath(),Toast.LENGTH_SHORT).show();

                        outputStream.flush();
                        outputStream.close();
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/jpeg");
                        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.getAbsolutePath()));
                        startActivity(Intent.createChooser(share, "Share Image"));
                    }


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


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
                TedBottomPicker.with(getActivity()).setTitle("Vui Lòng Chọn Ảnh").setPreviewMaxCount(19)
                        .show(new TedBottomSheetDialogFragment.OnImageSelectedListener() {
                            @Override
                            public void onImageSelected(Uri uri) {
                                Picasso.get().load(uri).into(img_sach);
                                check_image_change="true";
                            }
                        });
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edt_tensach.getText().toString().isEmpty()&!edt_giaban.getText().toString().isEmpty()
                &!edt_soquyen.getText().toString().isEmpty()&!spinner_tentacgia.getSelectedItem().toString().contains("Không có tác giả,vui lòng thêm")
                &!spinner_tenloaisach.getSelectedItem().toString().contains("Không có thể loại,vui lòng thêm")){
                    if(check_image_change.contains("true")&dulieu[0].toLowerCase().contains("tao")){

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

                                TextView btn_xacnhan= dialog.findViewById(R.id.btn_xacnhan_dialog_xacnhan);
                                TextView btn_huy= dialog.findViewById(R.id.btn_huy_dialog_xacnhan);
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
                    else if(dulieu[0].toLowerCase().toString().contains("chinhsua")){
                        update_sach();
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
                getActivity().getSupportFragmentManager().popBackStack();
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
        try {
            File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Quanlythuvien");
            folder.mkdirs();
            BitmapDrawable drawable1 = (BitmapDrawable) img_barcode.getDrawable();
            Bitmap bitmap1 = drawable1.getBitmap();

            if(!edt_tensach.getText().toString().trim().isEmpty()){
                File file = new File(folder,
                        edt_tensach.getText().toString()+"_"+ Masach+".jpg");
                outputStream = new FileOutputStream(file);
                bitmap1.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                Toast.makeText(getActivity(),"Thêm sách thành công!!!Ảnh đã được lưu tại "+file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
                outputStream.flush();
                outputStream.close();
            }
            else {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        Masach+".jpg");
                outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                Toast.makeText(getActivity(),"Ảnh đã được lưu tại "+file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
                outputStream.flush();
                outputStream.close();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        getActivity().getSupportFragmentManager().popBackStack();

    }
    private void update_sach(){
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
        sach.setMALOAI(maloai);
        sach.setMATACGIA(matacgia);
        sach.setTENSACH(edt_tensach.getText().toString());
        sach.setSOQUYEN(Integer.parseInt(edt_soquyen.getText().toString()));
        sach.setTRANGTHAI(spinner_trangthai.getSelectedItem().toString());
        sach.setGIABAN(Integer.parseInt(edt_giaban.getText().toString()));
        sach.setHINH_SACH(hinhanh);
        database.UPDATE_SACH(sach);
        Toast.makeText(getActivity(), "Chỉnh Sửa Thành Công", Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager().popBackStack();
    }

}
