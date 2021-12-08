package com.rin1903.bookstoremanager.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.rin1903.bookstoremanager.MainActivity;
import com.rin1903.bookstoremanager.R;
import com.rin1903.bookstoremanager.SQLite.HOADON;
import com.rin1903.bookstoremanager.SQLite.SACH;
import com.rin1903.bookstoremanager.fragment.Fragment_TaoHoaDon;

import java.util.ArrayList;

public class HoaDonAdapter extends RecyclerView.Adapter<HoaDonAdapter.ViewHolder> implements Filterable {
    private ArrayList<HOADON> hoadonArrayList;
    private ArrayList<HOADON> hoadonArrayList_old;
    private Context context;

    private ViewBinderHelper viewBinderHelper= new ViewBinderHelper();

    public HoaDonAdapter(ArrayList<HOADON> hoadonArrayList, Context context) {
        this.hoadonArrayList = hoadonArrayList;
        this.context = context;
        this.hoadonArrayList_old= hoadonArrayList;
        viewBinderHelper.setOpenOnlyOne(true);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view= inflater.inflate(R.layout.item_hienthi_khongcohinh,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_ten.setText("Mã Hoá Đơn:"+hoadonArrayList.get(position).getMAHOADON());
        holder.tv_mota1.setText("Thành Tiền:"+String.valueOf(hoadonArrayList.get(position).getTHANHTIEN_CTHD()));

        String mahoadon= hoadonArrayList.get(position).getMAHOADON();

            viewBinderHelper.bind(holder.swipeRevealLayout,hoadonArrayList.get(position).getMAHOADON());
        holder.item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hoadonArrayList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                MainActivity.database.DELETE_CHITIETHOADON(mahoadon);
                MainActivity.database.DELETE_HOADON(mahoadon);
            }
        });
        holder.item_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle= new Bundle();
                bundle.putString("guidulieu","chinhsua_hoadon_"+mahoadon);
                Fragment_TaoHoaDon fragment_taoHoaDon= new Fragment_TaoHoaDon();
                fragment_taoHoaDon.setArguments(bundle);
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content,fragment_taoHoaDon).addToBackStack(context.getClass().getName()).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return hoadonArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ten,tv_mota1;
        SwipeRevealLayout swipeRevealLayout;
        LinearLayout linearLayout;
        ImageView item_delete,item_edit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_ten= itemView.findViewById(R.id.tv_item_khongcohinh_hienthi);
            tv_mota1=itemView.findViewById(R.id.tv_item_mota1_khongcohinh_hienthi);
            swipeRevealLayout = itemView.findViewById(R.id.swipelayout_item_khongcohinh);
            linearLayout = itemView.findViewById(R.id.linearlayout_item_khongcohinh);
            item_delete= itemView.findViewById(R.id.tv_delete_item_khongcohinh);
            item_edit= itemView.findViewById(R.id.tv_edit_item_khongcohinh);
        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String stringsearch= constraint.toString();
                if(stringsearch.isEmpty()){
                    hoadonArrayList= hoadonArrayList_old;
                }
                else {
                    ArrayList<HOADON> list= new ArrayList<>();
                    for(HOADON hoadon:hoadonArrayList_old){
                        if(hoadon.getMAHOADON().toLowerCase().contains(stringsearch.toLowerCase())){
                            list.add(hoadon);
                        }
                    }
                    hoadonArrayList= list;
                }
                FilterResults filterResults= new FilterResults();
                filterResults.values= hoadonArrayList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                hoadonArrayList = (ArrayList<HOADON>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
