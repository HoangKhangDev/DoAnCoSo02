package com.rin1903.bookstoremanager.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rin1903.bookstoremanager.R;
import com.rin1903.bookstoremanager.SQLite.SACH_TRONG_PHIEUNHAP;

import java.util.ArrayList;

public class ChiTietPhieuNhapAdapter extends  RecyclerView.Adapter<ChiTietPhieuNhapAdapter.ViewHolder>{
    private Context context;
    private ArrayList<SACH_TRONG_PHIEUNHAP> arrayList = new ArrayList<>();

    public ChiTietPhieuNhapAdapter(Context context, ArrayList<SACH_TRONG_PHIEUNHAP> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view= inflater.inflate(R.layout.item_sach_thongtin_hoadon,parent,false);
        return new ViewHolder(view);
    }
    private int soluong=1;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_ten.setText(arrayList.get(position).getTenSach());
        holder.tv_soluong.setText("1");
        holder.tv_gia.setText(String.valueOf(arrayList.get(position).getGiaban()));
        SACH_TRONG_PHIEUNHAP sach_trong_phieunhap= arrayList.get(position);

            holder.imgdow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    soluong = sach_trong_phieunhap.getSoluongtronghoadon();
                    if (soluong > 1) {
                        soluong -= 1;
                        holder.tv_soluong.setText(String.valueOf(soluong));
                        sach_trong_phieunhap.setSoluongtronghoadon(soluong);
                        arrayList.set(position, sach_trong_phieunhap);
                    }
                }
            });
            holder.img_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    soluong = sach_trong_phieunhap.getSoluongtronghoadon();
                    if (soluong>1) {
                        soluong += 1;
                        holder.tv_soluong.setText(String.valueOf(soluong));
                        sach_trong_phieunhap.setSoluongtronghoadon(soluong);
                        arrayList.set(position, sach_trong_phieunhap);
                    }
                }
            });




    }
    public int getthanhtientong(){
        int tong=0;
        if(arrayList.size()>0){
            for(int i=0;i<arrayList.size();i++){
                tong+=arrayList.get(i).getThanhtien();
            }
            return tong;
        }
        else {
            return 0;
        }

    }

    public ArrayList<SACH_TRONG_PHIEUNHAP> getArrayList(){
        return arrayList;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ten,tv_gia,tv_soluong;
        ImageView img_up,imgdow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_ten= itemView.findViewById(R.id.tv_tensach_item_taohoadon);
            tv_gia= itemView.findViewById(R.id.tv_giaban_item_taohoadon);
            tv_soluong= itemView.findViewById(R.id.tv_soluong_item_taohoadon);
            img_up= itemView.findViewById(R.id.img_up_item_taohoadon);
            imgdow= itemView.findViewById(R.id.img_dow_item_taohoadon);

        }
    }
}