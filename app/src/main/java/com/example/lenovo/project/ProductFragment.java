package com.example.lenovo.project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
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


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "";
    private static final String APP_ID="ca-app-pub-9113648157256675~5074984124";
    arrayAdapter ItemAdapter;
    GridView viewList;
    static final ArrayList<Item> ItemList=new ArrayList<>();
    DatabaseReference myRef;
    AdView mAdview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_product,container,false);

        return view;

    }
    @Override
    public void onResume()
    {
        super.onResume();
        ItemList.clear();
        onStart();
    }
    @Override
    public void onStart()
    {
        super.onStart();

        if(isNetworkAvailable()) {
            myRef = FirebaseDatabase.getInstance().getReference("items");
            retrieveObjects(getView());
            ItemAdapter = new arrayAdapter(getContext(), ItemList);
            viewList = (GridView) getView().findViewById(R.id.ProdList);
            viewList.setAdapter(ItemAdapter);
            viewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    updateDetail(position);
                }
            });
        }
        else
        {
            Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    System.exit(0);
                }
            }, 3000);

        }

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void updateDetail(int i) {
        Intent intent=new Intent(getActivity(),Detail.class);
        String name=ItemList.get(i).getItemname();
        intent.putExtra(EXTRA_MESSAGE,name);
        intent.putExtra("Contact_list", ItemList);
        getActivity().startActivity(intent);
    }



    public void retrieveObjects(View v){

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Item value;
                String s="";
                ItemList.clear();
                for(DataSnapshot x:dataSnapshot.getChildren()){

                    value=x.getValue(Item.class);
                    s+=value.toString()+"\n";
                    ItemList.add(value);
                }

                //tv.setText(s);


                ItemAdapter.notifyDataSetChanged();


               /*
                Item value = dataSnapshot.getValue(Item.class);
                tv.setText(value.getItemid());
*/
                //               Log.d("abc",value.getItemid());

                //Log.d("abc", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("abc", "Failed to read value.", error.toException());
            }
        });

    }
}
