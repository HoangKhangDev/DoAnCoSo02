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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.rin1903.bookstoremanager.BuildConfig;
import com.rin1903.bookstoremanager.MainActivity;
import com.rin1903.bookstoremanager.R;
import com.rin1903.bookstoremanager.SQLite.KHACHHANG;
import com.rin1903.bookstoremanager.SQLite.TACGIA;
import com.rin1903.bookstoremanager.fragment.Fragment_HienThi;
import com.rin1903.bookstoremanager.fragment.Fragment_KhachHang;
import com.rin1903.bookstoremanager.fragment.Fragment_TacGia;

import java.util.ArrayList;

public class TacGiaAdapter  extends  RecyclerView.Adapter<TacGiaAdapter.ViewHolder> implements Filterable {
    private ArrayList<TACGIA> tacgiaArrayList;
    private ArrayList<TACGIA> tacgiaArrayOld;

    private Context context;
    private ViewBinderHelper viewBinderHelper= new ViewBinderHelper();

    public TacGiaAdapter(ArrayList<TACGIA> tacgiaArrayList, Context context) {
        this.tacgiaArrayList = tacgiaArrayList;
        this.tacgiaArrayOld=tacgiaArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_list_hienthi_cohinh,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_tieude.setText("Tên tác Giả:"+tacgiaArrayList.get(position).getTENTACGIA());
        holder.tv_mota.setText("Ngày Sinh:"+tacgiaArrayList.get(position).getNGAYSINH_TG());
        byte[] hinh= tacgiaArrayList.get(position).getHINH_TACGIA();
        Bitmap bitmap= BitmapFactory.decodeByteArray(hinh,0,hinh.length);
        holder.img.setImageBitmap(bitmap);
        TACGIA tacgia= tacgiaArrayList.get(position);

        holder.tv_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tacgiaArrayList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                MainActivity.database.QueryData("delete from TACGIA where MATACGiA='"+tacgiaArrayList.get(position).getMATACGIA()+"'");
            }
        });
        holder.tv_item_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_TacGia fragment_tacGia= new Fragment_TacGia();
                Bundle bundle= new Bundle();
                bundle.putString("guidulieu","chinhsua_tacgia_"+tacgia.getMATACGIA());
                fragment_tacGia.setArguments(bundle);
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_content, fragment_tacGia).addToBackStack(context.getClass().getName())
                        .commit();
            }
        });

        viewBinderHelper.bind(holder.swipeRevealLayout, tacgiaArrayList.get(position).getMATACGIA());



    }

    @Override
    public int getItemCount() {
        return tacgiaArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_tieude,tv_mota;
        private ImageView img;
        LinearLayout tv_item_delete;
        LinearLayout tv_item_edit;
        SwipeRevealLayout swipeRevealLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tieude= itemView.findViewById(R.id.tv_tieude_item_cohinh_listview_hienthi);
            tv_mota = itemView.findViewById(R.id.tv_mota_item_cohinh_listview_hienthi);
            img= itemView.findViewById(R.id.image_item_cohinh_list_hienthi);
            tv_item_delete= itemView.findViewById(R.id.tv_delete_item_cohinh);
            tv_item_edit = itemView.findViewById(R.id.tv_edit_item_cohinh);
            swipeRevealLayout= itemView.findViewById(R.id.swipelayout_item_cohinh);

        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String stringsearch= constraint.toString();
                if(stringsearch.isEmpty()){
                    tacgiaArrayList= tacgiaArrayOld;
                }
                else {
                    ArrayList<TACGIA> list= new ArrayList<>();
                    for(TACGIA tacgia:tacgiaArrayOld){
                        if(tacgia.getTENTACGIA().toLowerCase().contains(stringsearch.toLowerCase())){
                            list.add(tacgia);
                        }
                    }
                    tacgiaArrayList= list;
                }
                FilterResults filterResults= new FilterResults();
                filterResults.values= tacgiaArrayList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                tacgiaArrayList = (ArrayList<TACGIA>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
