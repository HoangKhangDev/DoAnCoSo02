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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.rin1903.bookstoremanager.MainActivity;
import com.rin1903.bookstoremanager.R;
import com.rin1903.bookstoremanager.SQLite.SACH;
import com.rin1903.bookstoremanager.SQLite.TACGIA;
import com.rin1903.bookstoremanager.fragment.Fragment_Sach;
import com.rin1903.bookstoremanager.fragment.Fragment_TacGia;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SachAdapter extends RecyclerView.Adapter<SachAdapter.ViewHolder> implements Filterable {
    private ArrayList<SACH> sachArrayList;
    private ArrayList<SACH> sachArrayList_old;
    private Context context;

    private ViewBinderHelper viewBinderHelper= new ViewBinderHelper();


    public SachAdapter(ArrayList<SACH> sachArrayList, Context context) {
        this.sachArrayList = sachArrayList;
        this.context = context;
        this.sachArrayList_old= sachArrayList;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_tieude.setText("Tên Sách:"+sachArrayList.get(position).getTENSACH());
        holder.tv_mota.setText("Số Quyển:"+String.valueOf(sachArrayList.get(position).getSOQUYEN())+"\n Trạng thái:"+sachArrayList.get(position).getTRANGTHAI());
        byte[] hinh = sachArrayList.get(position).getHINH_SACH();
        Bitmap bitmap = BitmapFactory.decodeByteArray(hinh,0,hinh.length);
        holder.img_hinh.setImageBitmap(bitmap);
        int masach=sachArrayList.get(position).getMASACH();

        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(sachArrayList.get(position).getMASACH()));

        holder.tv_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              if(!String.valueOf(masach).isEmpty()){
                  new AlertDialog.Builder(context).setTitle("Delete")
                          .setMessage("Bạn có muốn xoá sách này không???").setNeutralButton("Có", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          sachArrayList.remove(holder.getAdapterPosition());
                          notifyItemRemoved(holder.getAdapterPosition());
                          MainActivity.database.QueryData("update SACH set TRANGTHAI='Ngừng kinh doanh' where MASACH="+masach);
                      }
                  }).setPositiveButton("Không", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          dialog.cancel();
                      }
                  }).show();
              }
            }
        });

        holder.tv_item_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_Sach fragment= new Fragment_Sach();
                Bundle bundle= new Bundle();
                bundle.putString("guidulieu","chinhsua_sach_"+sachArrayList.get(position).getMASACH());
                fragment.setArguments(bundle);
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_content, fragment).addToBackStack(context.getClass().getName())
                        .commit();
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_Sach fragment= new Fragment_Sach();
                Bundle bundle= new Bundle();
                bundle.putString("guidulieu","xem_sach_"+sachArrayList.get(position).getMASACH());
                fragment.setArguments(bundle);
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_content, fragment).addToBackStack(context.getClass().getName())
                        .commit();
            }
        });

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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String stringsearch= constraint.toString();
                if(stringsearch.isEmpty()){
                    sachArrayList= sachArrayList_old;
                }
                else {
                    ArrayList<SACH> list= new ArrayList<>();
                    for(SACH sach:sachArrayList_old){
                        if(sach.getTENSACH().toLowerCase().contains(stringsearch.toLowerCase())){
                            list.add(sach);
                        }
                    }
                    sachArrayList= list;
                }
                FilterResults filterResults= new FilterResults();
                filterResults.values= sachArrayList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                sachArrayList = (ArrayList<SACH>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
