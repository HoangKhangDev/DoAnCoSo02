package com.rin1903.bookstoremanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.rin1903.bookstoremanager.R;
import com.rin1903.bookstoremanager.SQLite.PHIEUNHAP;

import java.util.ArrayList;

public class PhieuNhapAdapter extends RecyclerView.Adapter<PhieuNhapAdapter.ViewHolder>{
    private ArrayList<PHIEUNHAP> phieunhapArrayList;
    private Context context;

    private ViewBinderHelper viewBinderHelper= new ViewBinderHelper();

    public PhieuNhapAdapter(ArrayList<PHIEUNHAP> phieunhapArrayList, Context context) {
        this.phieunhapArrayList = phieunhapArrayList;
        this.context = context;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_tieude.setText(phieunhapArrayList.get(position).getMAPHIEUNHAP());
        holder.tv_mota1.setText("Thành Tiền: "+phieunhapArrayList.get(position).getTHANHTIEN_PN());
        holder.tv_mota2.setText("Ngày Lập: "+phieunhapArrayList.get(position).getNGAY_PN());

        viewBinderHelper.bind(holder.swipeRevealLayout,phieunhapArrayList.get(position).getMAPHIEUNHAP());


    }

    @Override
    public int getItemCount() {
        return phieunhapArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_tieude,tv_mota1,tv_mota2;
        SwipeRevealLayout swipeRevealLayout;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tieude= itemView.findViewById(R.id.tv_item_khongcohinh_hienthi);
            tv_mota1=itemView.findViewById(R.id.tv_item_mota1_khongcohinh_hienthi);
            tv_mota2=itemView.findViewById(R.id.tv_item_mota2_khongcohinh_hienthi);
            swipeRevealLayout = itemView.findViewById(R.id.swipelayout_item_khongcohinh);
            linearLayout= itemView.findViewById(R.id.linearlayout_item_khongcohinh);
        }
    }
}
