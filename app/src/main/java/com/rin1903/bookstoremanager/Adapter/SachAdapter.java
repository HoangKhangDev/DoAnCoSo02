package com.rin1903.bookstoremanager.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.rin1903.bookstoremanager.R;
import com.rin1903.bookstoremanager.SQLite.SACH;

import java.util.ArrayList;

public class SachAdapter extends RecyclerView.Adapter<SachAdapter.ViewHolder>{
    private ArrayList<SACH> sachArrayList;
    private Context context;

    ViewBinderHelper viewBinderHelper= new ViewBinderHelper();


    public SachAdapter(ArrayList<SACH> sachArrayList, Context context) {
        this.sachArrayList = sachArrayList;
        this.context = context;
        viewBinderHelper.setOpenOnlyOne(true);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_list_hienthi_cohinh,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_tieude.setText(sachArrayList.get(position).getTENSACH());
        holder.tv_mota.setText(String.valueOf(sachArrayList.get(position).getSOQUYEN()));
        byte[] hinh = sachArrayList.get(position).getHINH_SACH();
        Bitmap bitmap = BitmapFactory.decodeByteArray(hinh,0,hinh.length);
        holder.imageView.setImageBitmap(bitmap);
        if(sachArrayList!=null){
            viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(sachArrayList.get(position).getMASACH()));

        }

    }

    @Override
    public int getItemCount() {
        if(sachArrayList!=null)
        {
            return sachArrayList.size();
        }
        else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_tieude,tv_mota;
        ImageView imageView;
        SwipeRevealLayout swipeRevealLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tieude=itemView.findViewById(R.id.tv_tieude_item_cohinh_listview_hienthi);
            tv_mota= itemView.findViewById(R.id.tv_mota_item_cohinh_listview_hienthi);
            imageView=itemView.findViewById(R.id.image_item_cohinh_list_hienthi);
            swipeRevealLayout=itemView.findViewById(R.id.swipelayout_item_cohinh);
        }
    }
}
