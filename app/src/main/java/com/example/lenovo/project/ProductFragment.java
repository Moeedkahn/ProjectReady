package com.example.lenovo.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProductFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "";
    arrayAdapter ShirtAdapter;
    GridView viewList;
    ArrayList<Shirts> ShirtList=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_product,container,false);

        ImageView image=new ImageView(getContext());

        image.setImageResource(R.drawable.shirt_pic);

        Shirts temp=new Shirts("POLO REPUBLICA HIDAKA",1000,10,10,10);
        ShirtList.add(temp);
        Shirts temp2=new Shirts("POLO REPUBLICA HIDAKA",1000,10,10,10);
        ShirtList.add(temp2);
        Shirts temp3=new Shirts("POLO REPUBLICA HIDAKA",1000,10,10,10);
        ShirtList.add(temp3);
        ShirtAdapter= new  arrayAdapter(getContext(), ShirtList);
        viewList = (GridView) view.findViewById(R.id.ProdList);
        viewList.setAdapter(ShirtAdapter);

        viewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateDetail(position);
            }
        });
        return view;
    }
    public void updateDetail(int i) {
        Intent intent=new Intent(getActivity(),Detail.class);
        Toast.makeText(getContext(),"Moving",Toast.LENGTH_LONG).show();
        String name=ShirtList.get(i).name;
        intent.putExtra(EXTRA_MESSAGE,name);
        getActivity().startActivity(intent);
    }
}
