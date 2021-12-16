package com.rin1903.bookstoremanager.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.rin1903.bookstoremanager.MainActivity;
import com.rin1903.bookstoremanager.R;
import com.rin1903.bookstoremanager.SQLite.KHACHHANG;
import com.rin1903.bookstoremanager.fragment.Fragment_KhachHang;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.List;

public class KhachHangAdapter extends RecyclerView.Adapter<KhachHangAdapter.ViewHolder> implements Filterable {
    private ArrayList<KHACHHANG> khachhangArrayList;
    private ArrayList<KHACHHANG> khachhangarray_old;
    private Context context;

    private ViewBinderHelper viewBinderHelper= new ViewBinderHelper();



    public KhachHangAdapter(ArrayList<KHACHHANG> khachhangArrayList, Context context) {
        this.khachhangArrayList = khachhangArrayList;
        this.context = context;
        this.khachhangarray_old= khachhangArrayList;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
       if(khachhangArrayList.get(position)!=null){
           holder.tv_tieude.setText("Tên Khách Hàng:"+khachhangArrayList.get(position).getTENKHACHHANG());
           holder.tv_mota.setText("Số điện thoại:"+khachhangArrayList.get(position).getSDT_KH());
           byte[] hinh= khachhangArrayList.get(position).getHINH_KH();
           Bitmap bitmap= BitmapFactory.decodeByteArray(hinh,0,hinh.length);
           holder.img_hinh.setImageBitmap(bitmap);
       }
       String makh= khachhangArrayList.get(position).getMAKHACHHANG();

        viewBinderHelper.bind(holder.swipeRevealLayout, khachhangArrayList.get(position).getMAKHACHHANG());

        holder.tv_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context).setTitle("Delete")
                        .setMessage("Bạn có muốn xoá khách hàng này không???").setNeutralButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        khachhangArrayList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        MainActivity.database.QueryData("delete from KHACHHANG where MAKHACHHANG='"+makh+"'");
                    }
                }).setPositiveButton("Không",null).show();
            }
        });
        KHACHHANG kh= khachhangArrayList.get(position);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(),kh.getTENKHACHHANG() , Toast.LENGTH_SHORT).show();
            }
        });
        holder.tv_item_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle= new Bundle();
                bundle.putString("guidulieu","chinhsua_khachhang_"+makh);
                Fragment_KhachHang fragment_khachHang= new Fragment_KhachHang();
                fragment_khachHang.setArguments(bundle);
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content,fragment_khachHang).addToBackStack(context.getClass().getName()).commit();
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle= new Bundle();
                bundle.putString("guidulieu","xem_khachhang_"+makh);
                Fragment_KhachHang fragment_khachHang= new Fragment_KhachHang();
                fragment_khachHang.setArguments(bundle);
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content,fragment_khachHang).addToBackStack(context.getClass().getName()).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        if(khachhangArrayList!=null) return khachhangArrayList.size(); else return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String stringsearch= constraint.toString();
                if(stringsearch.isEmpty()){
                   khachhangArrayList= khachhangarray_old;
                }
                else {
                    ArrayList<KHACHHANG> list= new ArrayList<>();
                    for(KHACHHANG khachhang:khachhangarray_old){
                        if(khachhang.getTENKHACHHANG().toLowerCase().contains(stringsearch.toLowerCase())){
                            list.add(khachhang);
                        }
                    }
                    khachhangArrayList= list;
                }
                FilterResults filterResults= new FilterResults();
                filterResults.values= khachhangArrayList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                khachhangArrayList = (ArrayList<KHACHHANG>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        BootstrapLabel tv_tieude;
        BootstrapLabel tv_mota;
        ImageView img_hinh;

        CardView cardView;
        SwipeRevealLayout swipeRevealLayout;
        LinearLayout tv_item_delete;
        LinearLayout tv_item_edit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tieude= itemView.findViewById(R.id.tv_tieude_item_cohinh_listview_hienthi);
            tv_mota= itemView.findViewById(R.id.tv_mota_item_cohinh_listview_hienthi);
            img_hinh=itemView.findViewById(R.id.image_item_cohinh_list_hienthi);
            tv_item_delete= itemView.findViewById(R.id.tv_delete_item_cohinh);
            tv_item_edit = itemView.findViewById(R.id.tv_edit_item_cohinh);
            swipeRevealLayout= itemView.findViewById(R.id.swipelayout_item_cohinh);
            cardView=itemView.findViewById(R.id.cardview_item_cohinh);

        }
    }
}
