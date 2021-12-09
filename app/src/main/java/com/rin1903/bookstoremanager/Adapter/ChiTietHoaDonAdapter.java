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

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.rin1903.bookstoremanager.MainActivity;
import com.rin1903.bookstoremanager.R;
import com.rin1903.bookstoremanager.SQLite.SACH;
import com.rin1903.bookstoremanager.SQLite.SACH_TRONG_HOADON;

import java.util.ArrayList;

public class ChiTietHoaDonAdapter extends  RecyclerView.Adapter<ChiTietHoaDonAdapter.ViewHolder>{
    private Context context;
    private ArrayList<SACH_TRONG_HOADON> arrayList = new ArrayList<>();
    ViewBinderHelper viewBinderHelper= new ViewBinderHelper();
    public ChiTietHoaDonAdapter(Context context, ArrayList<SACH_TRONG_HOADON> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        viewBinderHelper.setOpenOnlyOne(true);
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
        SACH_TRONG_HOADON sach_trong_hoadon= arrayList.get(position);

        viewBinderHelper.bind(holder.swipeRevealLayout,String.valueOf(arrayList.get(position).getMaSach()));
            holder.imgdow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    soluong = sach_trong_hoadon.getSoluongtronghoadon();
                    if (soluong > 1) {
                        soluong -= 1;
                        holder.tv_soluong.setText(String.valueOf(soluong));
                        sach_trong_hoadon.setSoluongtronghoadon(soluong);
                        arrayList.set(position, sach_trong_hoadon);
                    }
                }
            });
            holder.img_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    soluong = sach_trong_hoadon.getSoluongtronghoadon();
                    if (soluong < sach_trong_hoadon.getSoLuongconlai()) {
                        soluong += 1;
                        holder.tv_soluong.setText(String.valueOf(soluong));
                        sach_trong_hoadon.setSoluongtronghoadon(soluong);
                        arrayList.set(position, sach_trong_hoadon);
                    }
                }
            });
            holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arrayList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
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

    public ArrayList<SACH_TRONG_HOADON> getArrayList(){
        return arrayList;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ten,tv_gia,tv_soluong;
        ImageView img_up,imgdow,imgdelete;
        SwipeRevealLayout swipeRevealLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_ten= itemView.findViewById(R.id.tv_tensach_item_taohoadon);
            tv_gia= itemView.findViewById(R.id.tv_giaban_item_taohoadon);
            tv_soluong= itemView.findViewById(R.id.tv_soluong_item_taohoadon);
            img_up= itemView.findViewById(R.id.img_up_item_taohoadon);
            imgdow= itemView.findViewById(R.id.img_dow_item_taohoadon);
            imgdelete= itemView.findViewById(R.id.img_delete_item_thongtinhhoadon);
            swipeRevealLayout= itemView.findViewById(R.id.swipelayout_item_thongtin_hoadon);
        }
    }
}
