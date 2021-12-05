package com.rin1903.bookstoremanager.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.rin1903.bookstoremanager.R;

public class fragment_list extends Fragment {
    private Spinner spinner;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_list,container,false);
        spinner= (Spinner) view.findViewById(R.id.spinner_select);
        String[] list_select= {"Khách Hàng", "Sách", "Hoá Đơn", "Nhà Cung Cấp", "Tác Giả" };
        ArrayAdapter adapter= new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item,list_select);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getAdapter().getItem(position).toString()){
                    case "Khách Hàng":
                        Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
                        break;
                    case "Sách":
                        Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();
                        break;
                    case "Hoá Đơn":
                        Toast.makeText(getActivity(), "3", Toast.LENGTH_SHORT).show();
                        break;
                    case "Nhà Cung Cấp":
                        Toast.makeText(getActivity(), "4", Toast.LENGTH_SHORT).show();
                        break;
                    case "Tác Giả":
                        Toast.makeText(getActivity(), "5", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }
}
