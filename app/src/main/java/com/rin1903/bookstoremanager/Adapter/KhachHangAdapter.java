package com.rin1903.bookstoremanager.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.rin1903.bookstoremanager.R;
import com.rin1903.bookstoremanager.SQLite.KHACHHANG;

import java.util.ArrayList;

public class KhachHangAdapter extends RecyclerView.Adapter<KhachHangAdapter.ViewHolder> {
    private ArrayList<KHACHHANG> khachhangArrayList;
    private Context context;

    private ViewBinderHelper viewBinderHelper= new ViewBinderHelper();



    public KhachHangAdapter(ArrayList<KHACHHANG> khachhangArrayList, Context context) {
        this.khachhangArrayList = khachhangArrayList;
        this.context = context;
        viewBinderHelper.setOpenOnlyOne(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_list_hienthi_cohinh,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       if(khachhangArrayList.get(position)!=null){
           holder.tv_tieude.setText(khachhangArrayList.get(position).getTENKHACHHANG());
           holder.tv_mota.setText(khachhangArrayList.get(position).getSDT_KH());
           byte[] hinh= khachhangArrayList.get(position).getHINH_KH();
           Bitmap bitmap= BitmapFactory.decodeByteArray(hinh,0,hinh.length);
           holder.img_hinh.setImageBitmap(bitmap);
       }

        viewBinderHelper.bind(holder.swipeRevealLayout, khachhangArrayList.get(position).getMAKHACHHANG());

        holder.tv_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                khachhangArrayList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
        KHACHHANG kh= khachhangArrayList.get(position);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(),kh.getTENKHACHHANG() , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(khachhangArrayList!=null) return khachhangArrayList.size(); else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_tieude;
        TextView tv_mota;
        ImageView img_hinh;

        CardView cardView;
        SwipeRevealLayout swipeRevealLayout;
        TextView tv_item_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tieude= itemView.findViewById(R.id.tv_tieude_item_cohinh_listview_hienthi);
            tv_mota= itemView.findViewById(R.id.tv_mota_item_cohinh_listview_hienthi);
            img_hinh=itemView.findViewById(R.id.image_item_cohinh_list_hienthi);
            tv_item_delete= itemView.findViewById(R.id.tv_delete_item_cohinh);
            swipeRevealLayout= itemView.findViewById(R.id.swipelayout_item_cohinh);
            cardView=itemView.findViewById(R.id.cardview_item_cohinh);

        }
    }
}
