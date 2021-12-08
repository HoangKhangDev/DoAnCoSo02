package com.rin1903.bookstoremanager.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.rin1903.bookstoremanager.BuildConfig;
import com.rin1903.bookstoremanager.MainActivity;
import com.rin1903.bookstoremanager.R;
import com.rin1903.bookstoremanager.SQLite.NHACUNGCAP;
import com.rin1903.bookstoremanager.SQLite.SACH;
import com.rin1903.bookstoremanager.fragment.Fragment_NhaCungCap;

import java.util.ArrayList;

public class NhaCungCapAdapter extends RecyclerView.Adapter<NhaCungCapAdapter.ViewHolder> implements Filterable {
    private ArrayList<NHACUNGCAP> nhacungcapArrayList;
    private ArrayList<NHACUNGCAP> nhacungcapArrayList_old;
    private Context context;

    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public NhaCungCapAdapter(ArrayList<NHACUNGCAP> nhacungcapArrayList, Context context) {
        this.nhacungcapArrayList = nhacungcapArrayList;
        this.context = context;
        this.nhacungcapArrayList_old=nhacungcapArrayList;
        viewBinderHelper.setOpenOnlyOne(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view= inflater.inflate(R.layout.item_list_hienthi_cohinh,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
       if(nhacungcapArrayList.get(position)!=null){
           holder.tv_tieude.setText(nhacungcapArrayList.get(position).getTENNHACUNGCAP());
           holder.tv_mota.setText(nhacungcapArrayList.get(position).getSDT_NCC());
           byte[] hinh = nhacungcapArrayList.get(position).getHINH_NHACUNGCAP();
           Bitmap bitmap= BitmapFactory.decodeByteArray(hinh,0,hinh.length);
           holder.img_hinh.setImageBitmap(bitmap);
       }

        holder.tv_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nhacungcapArrayList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                MainActivity.database.QueryData("delete from NHACUNGCAP where MANHACUNGCAP='"+nhacungcapArrayList.get(position).getMANHACUNGCAP()+"'");
            }
        });

        viewBinderHelper.bind(holder.swipeRevealLayout,nhacungcapArrayList.get(position).getMANHACUNGCAP());

        holder.tv_item_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle= new Bundle();
                bundle.putString("guidulieu","chinhsua_nhacungcap_"+nhacungcapArrayList.get(position).getMANHACUNGCAP());
                Fragment_NhaCungCap fragment_nhaCungCap= new Fragment_NhaCungCap();
                fragment_nhaCungCap.setArguments(bundle);
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content,fragment_nhaCungCap).addToBackStack(context.getClass().getName()).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        if(nhacungcapArrayList.size()>0){
            return nhacungcapArrayList.size();
        }
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_tieude;
        TextView tv_mota;
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String stringsearch= constraint.toString();
                if(stringsearch.isEmpty()){
                    nhacungcapArrayList= nhacungcapArrayList_old;
                }
                else {
                    ArrayList<NHACUNGCAP> list= new ArrayList<>();
                    for(NHACUNGCAP nhacungcap:nhacungcapArrayList_old){
                        if(nhacungcap.getTENNHACUNGCAP().toLowerCase().contains(stringsearch.toLowerCase())){
                            list.add(nhacungcap);
                        }
                    }
                    nhacungcapArrayList= list;
                }
                FilterResults filterResults= new FilterResults();
                filterResults.values= nhacungcapArrayList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                nhacungcapArrayList = (ArrayList<NHACUNGCAP>)results.values;
                notifyDataSetChanged();
            }
        };
    }
}
