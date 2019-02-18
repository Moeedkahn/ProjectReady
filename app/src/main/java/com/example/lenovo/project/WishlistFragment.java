package com.example.lenovo.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WishlistFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "hello";
    private static final String APP_ID="ca-app-pub-3940256099942544~3347511713";
    AdView mAdview;
    private FirebaseAuth firebaseAuth;
    private LinearLayout layout;
    String Email=new String();
    static final ArrayList<Item> ItemList=new ArrayList<>();
    ArrayList<Item> OriginalList=new ArrayList<>();
    WishlistAdaptar wishlistAdaptar;
    DatabaseReference myRef;
    DatabaseReference myRef2;
    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_wishlist,container,false);

        return view;
    }
    @Override
    public void onResume()
    {
        super.onResume();
        layout=getView().findViewById(R.id.wishlistLayout);
        layout.removeAllViews();
        onStart();
    }
    @Nullable
    @Override
    public void onStart()
    {
        super.onStart();
        layout=getView().findViewById(R.id.wishlistLayout);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser User=firebaseAuth.getCurrentUser();
        if(User ==null)
        {
            Button registerButton=new Button(getActivity());
            registerButton.setLayoutParams(params);
            registerButton.setText("Register");
            registerButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Registeration(getView());
                }
            });
            Button LoginButton=new Button(getActivity());
            LoginButton.setLayoutParams(params);
            LoginButton.setText("Login");
            LoginButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            LoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(),LoginDetail.class);
                    getActivity().startActivity(intent);
                }
            });
            layout.addView(registerButton);
            layout.addView(LoginButton);
        }
        else
        {
            Email=firebaseAuth.getCurrentUser().getEmail();
            myRef= FirebaseDatabase.getInstance().getReference("wishlist");
            myRef2= FirebaseDatabase.getInstance().getReference("items");
            retrieveOriginalObjects(getView());
            retrieveObjects(getView());
            Button logoutButton=new Button(getActivity());
            ListView list=new ListView(getActivity());
            list.setLayoutParams(params);
            logoutButton.setLayoutParams(params);
            logoutButton.setText("Logout");
            logoutButton.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_light));
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseAuth.signOut();
                    Intent intent=new Intent(getActivity(),MainActivity.class);
                    getActivity().startActivity(intent);
                }
            });
            layout.addView(logoutButton);
            list.setPadding(0,0,0,120);

            layout.addView(list);
            wishlistAdaptar=new  WishlistAdaptar(getContext(),ItemList,OriginalList);
            list.setAdapter( wishlistAdaptar);
        }

    }

    public void retrieveObjects(View v){

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Wishlist value;
                String s = "";
                for (DataSnapshot x : dataSnapshot.getChildren()) {

                    value = x.getValue(Wishlist.class);
                    s += value.toString() + "\n";
                    if(Email.equals(value.uid)) {
                        ItemList.clear();
                        ItemList.addAll(value.wisheditems);
                    }
                }
                wishlistAdaptar.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("abc", "Failed to read value.", error.toException());
            }
        });

    }
    public void retrieveOriginalObjects(View v){

        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Item value;
                String s="";
                for(DataSnapshot x:dataSnapshot.getChildren()){

                    value=x.getValue(Item.class);
                    s+=value.toString()+"\n";
                    OriginalList.add(value);
                }
                wishlistAdaptar.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("abc", "Failed to read value.", error.toException());
            }
        });
    }
    public void Registeration(View view)
    {
        Intent intent=new Intent(getActivity(),Register.class);
        getActivity().startActivity(intent);
    }


}
