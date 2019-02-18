package com.example.lenovo.project;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartService extends Service {
    DatabaseReference myref;
    ArrayList<Item> ItemList=new ArrayList<>();
    ArrayList<Item> ItemList2=new ArrayList<>();
    private FirebaseAuth firebaseAuth;
    String Size;
    static boolean myCounter=false;
    int indexofCurrent=0;
    String Email;
    Item toAdd=new Item();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public int onStartCommand(Intent intent, int flags, int startId) {

        ItemList=intent.getParcelableArrayListExtra("Contact_list");
        toAdd=ItemList.get(0);
        firebaseAuth= FirebaseAuth.getInstance();
        final FirebaseUser User=firebaseAuth.getCurrentUser();
        Email=firebaseAuth.getCurrentUser().getEmail();
        myref=FirebaseDatabase.getInstance().getReference("carts");
        check();
        return START_NOT_STICKY;
    }
    public void check ()
    {
        final String Email = firebaseAuth.getCurrentUser().getEmail();
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot x: dataSnapshot.getChildren())
                {
                    Cart temp;
                    temp=x.getValue(Cart.class);
                    String em=temp.uid;
                    if (Email.equals(em)) {

                        ItemList2.clear();
                        ItemList2.addAll(temp.itemstobuy);
                        myCounter = true;
                        myref.removeEventListener(this);
                        oldList();
                        break;
                    }
                    indexofCurrent++;
                }
                if(!myCounter) {
                    newList();
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
        ItemList2.add(toAdd);
        current.uid = Email;
        for(int i=0;i<ItemList2.size();i++)
        {
            current.itemstobuy.add(ItemList2.get(i));
        }
        Toast.makeText(getApplicationContext(),"Added To Cart",Toast.LENGTH_SHORT).show();
        String index = String.valueOf(indexofCurrent);
        indexofCurrent=0;
        myref.child(index).setValue(current);
    }
    public void newList()
    {
        ArrayList<Item> lat = new ArrayList<>();
        lat.add(toAdd);
        Cart current = new Cart();
        current.uid = Email;
        current.itemstobuy = lat;
        Toast.makeText(getApplicationContext(),"Added To Cart",Toast.LENGTH_SHORT).show();
        if(indexofCurrent==0)
        {
            myref.child("0").setValue(current);
        }
        else {
            myref.child(String.valueOf(indexofCurrent)).setValue(current);
            indexofCurrent=0;
        }

    }
}
