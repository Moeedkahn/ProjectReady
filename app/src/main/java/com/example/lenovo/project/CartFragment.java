package com.example.lenovo.project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "";
    private FirebaseAuth firebaseAuth;
    private LinearLayout layout;
    String Email=new String();
    static final ArrayList<Item> ItemList=new ArrayList<>();
    ArrayList<Item> OriginalList=new ArrayList<>();
    cartadapter cartadapter;
    ValueEventListener mValueEventListener;

    ArrayList <Item> AfterList=new ArrayList<>();
    int []pricesIntList;
    DatabaseReference myRef;
    DatabaseReference myRef2;
    DatabaseReference myRef3;
    int indexofCurrent=0;
    boolean check=true;
    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_cart,container,false);
        return view;
    }
   @Override
    public void onResume()
    {
        super.onResume();
        layout=getView().findViewById(R.id.carttLayout);
        layout.removeAllViews();
        onStart();
    }
    @Override
    public void onStart()
    {
        super.onStart();
        layout=getView().findViewById(R.id.carttLayout);
        int total=0;

        firebaseAuth= FirebaseAuth.getInstance();
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
            myRef= FirebaseDatabase.getInstance().getReference("carts");
            myRef2= FirebaseDatabase.getInstance().getReference("items");

            retrieveObjects(getView());
            retrieveOriginalObjects(getView());
            Button logoutButton=new Button(getActivity());
            Button confirmOrder=new Button(getActivity());
            confirmOrder.setLayoutParams(params2);
            confirmOrder.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            confirmOrder.setText("Confirm Order");

            final ListView list=new ListView(getActivity());
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

            pricesIntList=new int[ItemList.size()];
            if(ItemList.size()>=0)
            {
                for(int i=0;i<ItemList.size();i++)
                {
                    pricesIntList[i]=Integer.parseInt(ItemList.get(i).price);
                }
                for(int i=0;i<ItemList.size();i++)
                {
                        total=total+pricesIntList[i];
                }
            }
            LinearLayout totalLayout=new LinearLayout(getActivity());
            totalLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            totalLayout.setOrientation(LinearLayout.HORIZONTAL);
            TextView totaltext=new TextView(getActivity());
            totaltext.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            totaltext.setText("TOTAL ():");
            final TextView totalamount=new TextView(getActivity());
            totalamount.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            totalLayout.addView(totaltext);
            totalamount.setText(String.valueOf(total));
            totalLayout.addView(totalamount);
            layout.setPadding(0,0,0,40);
            confirmOrder.setPaddingRelative(10,10,10,10);
            list.setPaddingRelative(0,0,0,30);

            layout.addView(confirmOrder);
            layout.addView(totalLayout);
            layout.addView(list);


            cartadapter=new cartadapter(getContext(),ItemList,OriginalList);
            list.setAdapter(cartadapter);
            //layout.addView(confirmOrder);

            myRef3 = FirebaseDatabase.getInstance().getReference("items");
            confirmOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isNetworkAvailable()) {
                        for (int k = 0; k < ItemList.size(); k++) {
                            for (int i = 0; i < OriginalList.size(); i++) {
                                if(!check)
                                {
                                    Toast.makeText(getActivity(), "cannot comfirm order", Toast.LENGTH_SHORT).show();
                                    break;
                                }

                                if (ItemList.get(k).itemname.equals(OriginalList.get(i).itemname)) {
                                    int sm = Integer.parseInt(OriginalList.get(i).sizes);
                                    int me = Integer.parseInt(OriginalList.get(i).sizem);
                                    int la = Integer.parseInt(OriginalList.get(i).sizel);
                                    if (ItemList.get(k).sizes.equals("1")) {
                                        Item myItem=new Item();
                                        int mySize=sm-1;
                                        if(mySize<=-1)
                                        {
                                            check=false;
                                            Toast.makeText(getActivity(), "Out of Stock in Small Size", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            myItem.sizes =  String.valueOf(mySize);
                                            myItem.sizem = OriginalList.get(i).sizem;
                                            myItem.sizel =OriginalList.get(i).sizel;

                                            myItem.itemname = OriginalList.get(i).itemname;
                                            myItem.price = OriginalList.get(i).price;
                                            myItem.itemid = OriginalList.get(i).itemid;
                                            myItem.imageurl = OriginalList.get(i).imageurl;


                                            OriginalList.set(i, myItem);
                                            myRef3.child(OriginalList.get(i).itemname).setValue(null);
                                            myRef3.child(OriginalList.get(i).itemname).setValue(OriginalList.get(i));
                                            retrieveOriginalObjects(v);
                                            break;
                                        }
                                    }
                                    else if (ItemList.get(k).sizem.equals("1")) {
                                        Item myItem=new Item();
                                        int mySize=me-1;
                                        if(mySize<=-1)
                                        {
                                            check=false;
                                            Toast.makeText(getActivity(), "Out of Stock in Medium Size", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            myItem.sizes = OriginalList.get(i).sizes;
                                            myItem.sizem = String.valueOf(mySize);
                                            myItem.sizel =  OriginalList.get(i).sizel;

                                            myItem.itemname = OriginalList.get(i).itemname;
                                            myItem.price = OriginalList.get(i).price;
                                            myItem.itemid = OriginalList.get(i).itemid;
                                            myItem.imageurl = OriginalList.get(i).imageurl;


                                            OriginalList.set(i, myItem);

                                            myRef3.child(OriginalList.get(i).itemname).setValue(null);
                                            myRef3.child(OriginalList.get(i).itemname).setValue(OriginalList.get(i));
                                            retrieveOriginalObjects(v);
                                            break;
                                        }
                                    }
                                    else if(ItemList.get(k).sizel.equals("1")){
                                        Item myItem=new Item();
                                        int mySize=la-1;
                                        if(mySize<=-1)
                                        {
                                            check=false;
                                            Toast.makeText(getActivity(), "Out of Stock in Large Size", Toast.LENGTH_SHORT).show();
                                        }
                                        else {

                                            myItem.sizes = OriginalList.get(i).sizes;
                                            myItem.sizem = OriginalList.get(i).sizem;
                                            myItem.sizel = String.valueOf(mySize);

                                            myItem.itemname = OriginalList.get(i).itemname;
                                            myItem.price = OriginalList.get(i).price;
                                            myItem.itemid = OriginalList.get(i).itemid;
                                            myItem.imageurl = OriginalList.get(i).imageurl;


                                            OriginalList.set(i, myItem);

                                            myRef3.child(OriginalList.get(i).itemname).setValue(null);
                                            myRef3.child(OriginalList.get(i).itemname).setValue(OriginalList.get(i));
                                            retrieveOriginalObjects(v);
                                            break;
                                        }
                                    }

                                }
                                else {
                                  /*  myRef3 = FirebaseDatabase.getInstance().getReference("newitems");
                                    myRef3.child(OriginalList.get(i).itemname).setValue(null);
                                    myRef3.child(OriginalList.get(i).itemname).setValue(OriginalList.get(i));
                                    retrieveOriginalObjects(v);*/
                                }
                            }
                        }
                        myRef3 = FirebaseDatabase.getInstance().getReference("items");

                       /* for (int i = 0; i < OriginalList.size(); i++)
                        {
                            myRef3.child(OriginalList.get(i).itemname).setValue(null);
                        }

                        for(int i=0;i<OriginalList.size();i++)
                        {
                            myRef3.child(OriginalList.get(i).itemname).setValue(OriginalList.get(i));
                        }*/

                      check();
                      if(check) {
                          Toast.makeText(getActivity(), "Order Confirmed", Toast.LENGTH_SHORT).show();
                          totalamount.setText("0");
                          cartadapter.notifyDataSetChanged();
                          final Handler handler = new Handler();
                          handler.postDelayed(new Runnable() {
                              @Override
                              public void run() {
                                  check = true;
                                  ItemList.clear();
                                  OriginalList.clear();
                                  AfterList.clear();
                                  layout.removeView(list);
                                  cartadapter.notifyDataSetChanged();
                              }
                          }, 2000);

                      }

                    }
                    else
                    {
                        Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                            }
                        }, 3000);
                    }
                }
            });
        }

    }

    public void retrieveObjects(View v){

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Cart value;
                String s="";
                for(DataSnapshot x:dataSnapshot.getChildren()){

                    value=x.getValue(Cart.class);
                    s+=value.toString()+"\n";
                    if(Email.equals(value.uid)) {
                         ItemList.clear();
                        ItemList.addAll(value.itemstobuy);
                    }
                }
                myRef.removeEventListener(this);

                //tv.setText(s);
                cartadapter.notifyDataSetChanged();
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
    public void retrieveOriginalObjects(View v){

        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
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
                myRef2.removeEventListener(this);
                cartadapter.notifyDataSetChanged();
                //tv.setText(s);


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
    public void check ()
    {
        final String Email2 = firebaseAuth.getCurrentUser().getEmail();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot x: dataSnapshot.getChildren())
                {
                    Cart temp;
                    temp=x.getValue(Cart.class);
                    String em=temp.uid;

                    if (Email2.equals(em)) {
                        myRef.removeEventListener(this);
                        oldList();
                        break;
                    }
                    indexofCurrent++;
                    cartadapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                return;
            }
        });
    }
    public void oldList()
    {

        Cart current=new Cart();
        current.uid =null;
        current.itemstobuy=null;
        String index = String.valueOf(indexofCurrent);
        indexofCurrent=0;
        myRef.child(index).setValue(current);
        cartadapter.notifyDataSetChanged();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void querydata(String mail){
        Query q= FirebaseDatabase.getInstance().getReference("carts")
                .orderByChild("useremail")
                .equalTo(mail);


        q.addListenerForSingleValueEvent(mValueEventListener);
        //runquery

    }
    public void Registeration(View view)
    {
        Intent intent=new Intent(getActivity(),Register.class);
        getActivity().startActivity(intent);
    }
}
