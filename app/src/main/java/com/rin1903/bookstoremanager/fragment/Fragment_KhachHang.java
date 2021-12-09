package com.rin1903.bookstoremanager.fragment;

import static android.app.Activity.RESULT_OK;
import static com.rin1903.bookstoremanager.MainActivity.SELECT_PICTURE;
import static com.rin1903.bookstoremanager.MainActivity.Tag;
import static com.rin1903.bookstoremanager.MainActivity.database;
import static com.rin1903.bookstoremanager.MainActivity.dulieu;

import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.rin1903.bookstoremanager.MainActivity;
import com.rin1903.bookstoremanager.R;
import com.rin1903.bookstoremanager.SQLite.KHACHHANG;
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


public class Fragment_KhachHang extends Fragment {
    Unbinder unbinder;

    private static final int SELECT_PICTURE = 1;

    private String selectedImagePath;
    private String makhachhang="";
    private String check_change_image="false";
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
    private KHACHHANG khachhang;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_khachhang, container, false);
        unbinder = ButterKnife.bind(this, view);

        List<String> gioitinh = new ArrayList<>();
        gioitinh.add("Nam");
        gioitinh.add("Nữ");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, gioitinh);
        spinner.setAdapter(adapter);

        Bundle bundle = getArguments();
        //loadlistview
        if (bundle != null) {
            dulieu = bundle.getString("guidulieu").split("_");
            if (dulieu[0].toLowerCase().contains("tao")) {
                Cursor cursor = database.Getdata("select MAKHACHHANG from KHACHHANG order by MAKHACHHANG desc limit 1");

                if (cursor.getCount() == 0) {
                    makhachhang = "khachhang-01";
                } else {
                    String[] tach = new String[0];
                    while (cursor.moveToNext()) {
                        tach = cursor.getString(0).split("-");
                    }
                    makhachhang = String.format("%s-%s", tach[0], String.valueOf(Integer.parseInt(tach[1]) + 1));
                }
            } else if (dulieu[0].toLowerCase().contains("chinhsua")) {
                Cursor cursor = database.Getdata("select * from KHACHHANG where MAKHACHHANG='" + dulieu[2].toString() + "'");
                while (cursor.moveToNext()) {
                    khachhang = new KHACHHANG(cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getBlob(6));

                    byte[] hinh = khachhang.getHINH_KH();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(hinh, 0, hinh.length);
                    img_hinhkhachhang.setImageBitmap(bitmap);
                    edt_tenkhachhang.setText(khachhang.getTENKHACHHANG());
                    edt_diachi.setText(khachhang.getDIACHI_KH());
                    edt_sdtkhachhang.setText(khachhang.getSDT_KH());
                    tv_calendar_khachhang.setText(khachhang.getNGAYSINH_KH());
                    if (khachhang.getGIOITINH_KH().toLowerCase().contains("nam")) {
                        spinner.setSelection(0);
                    } else if (khachhang.getGIOITINH_KH().toLowerCase().contains("nữ")) {
                        spinner.setSelection(1);
                    }
                    btn_them_khachhang.setText("Update");

                }
            } else if (dulieu[0].toLowerCase().contains("xem")) {
                Cursor cursor = database.Getdata("select * from KHACHHANG where MAKHACHHANG='" + dulieu[2].toString() + "'");
                while (cursor.moveToNext()) {
                    khachhang = new KHACHHANG(cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getBlob(6));

                    byte[] hinh = khachhang.getHINH_KH();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(hinh, 0, hinh.length);
                    img_hinhkhachhang.setImageBitmap(bitmap);
                    edt_tenkhachhang.setText(khachhang.getTENKHACHHANG());
                    edt_diachi.setText(khachhang.getDIACHI_KH());
                    edt_sdtkhachhang.setText(khachhang.getSDT_KH());
                    tv_calendar_khachhang.setText(khachhang.getNGAYSINH_KH());
                    if (khachhang.getGIOITINH_KH().toLowerCase().contains("nam")) {
                        spinner.setSelection(0);
                    } else if (khachhang.getGIOITINH_KH().toLowerCase().contains("nữ")) {
                        spinner.setSelection(1);
                    }
                    btn_them_khachhang.setVisibility(View.GONE);
                    edt_sdtkhachhang.setEnabled(false);
                    edt_diachi.setEnabled(false);
                    edt_tenkhachhang.setEnabled(false);
                    spinner.setEnabled(false);
                    img_calendar_khachhang.setEnabled(false);
                    img_hinhkhachhang.setEnabled(false);
                }
            }

        }


            img_calendar_khachhang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar calendar = Calendar.getInstance();
                    int Ngay = calendar.get(Calendar.DATE);
                    int Thang = calendar.get(Calendar.MONTH);
                    int Nam = calendar.get(Calendar.YEAR);
                    final String chuoi = "";
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            calendar.set(year, month, dayOfMonth);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            tv_calendar_khachhang.setText(simpleDateFormat.format(calendar.getTime()));
                        }
                    }, Nam, Thang, Ngay);
                    datePickerDialog.show();
                }
            });
            img_hinhkhachhang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TedBottomPicker.with(getActivity()).setTitle("Vui Lòng Chọn Ảnh").setPreviewMaxCount(19)
                            .show(new TedBottomSheetDialogFragment.OnImageSelectedListener() {
                                @Override
                                public void onImageSelected(Uri uri) {
                                    Picasso.get().load(uri).into(img_hinhkhachhang);
                                    check_change_image = "true";
                                }
                            });

                }

            });

            btn_them_khachhang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!edt_tenkhachhang.getText().toString().isEmpty()
                            & !edt_diachi.getText().toString().isEmpty()
                            & !edt_sdtkhachhang.getText().toString().isEmpty()
                            & !tv_calendar_khachhang.getText().toString().contains("dd/mm/yyyy")) {
                        if (check_change_image.contains("true") & dulieu[0].toLowerCase().contains("tao")) {
                            BitmapDrawable bitmapDrawable = (BitmapDrawable) img_hinhkhachhang.getDrawable();
                            Bitmap bitmap = bitmapDrawable.getBitmap();
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                            byte[] hinhanh = byteArrayOutputStream.toByteArray();
                            database.INSERT_KHACHHANG(makhachhang, edt_tenkhachhang.getText().toString(), spinner.getSelectedItem().toString(),
                                    tv_calendar_khachhang.getText().toString(), edt_sdtkhachhang.getText().toString(), edt_diachi.getText().toString(), hinhanh);
                            Toast.makeText(getActivity(), "Thêm Thành Công", Toast.LENGTH_SHORT).show();
                            if (getActivity().getSupportFragmentManager() != null) {
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        } else if (dulieu[0].toLowerCase().contains("chinhsua")) {
                            BitmapDrawable bitmapDrawable = (BitmapDrawable) img_hinhkhachhang.getDrawable();
                            Bitmap bitmap = bitmapDrawable.getBitmap();
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                            byte[] hinhanh = byteArrayOutputStream.toByteArray();
                            khachhang.setTENKHACHHANG(edt_tenkhachhang.getText().toString());
                            khachhang.setGIOITINH_KH(spinner.getSelectedItem().toString());
                            khachhang.setNGAYSINH_KH(tv_calendar_khachhang.getText().toString());
                            khachhang.setSDT_KH(edt_sdtkhachhang.getText().toString());
                            khachhang.setDIACHI_KH(edt_diachi.getText().toString());
                            khachhang.setHINH_KH(hinhanh);
                            database.UPDATE_KHACHHANG(khachhang);
                            Toast.makeText(getActivity(), "Chỉnh Sửa Thành Công", Toast.LENGTH_SHORT).show();
                            if (getActivity().getSupportFragmentManager() != null) {
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Please Change Photo", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
            btn_huy_khachhang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity().getSupportFragmentManager() != null) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                }
            });
            return view;
        }

}
