package com.example.lenovo.project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;

public class WishlistAdaptar extends BaseAdapter {
    public static final String EXTRA_MESSAGE = "";
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<Item> modellist;
    private ArrayList<Item> ItemList;
    private ArrayList<Item> OriginalList;
    DatabaseReference myref;
    private FirebaseAuth firebaseAuth;
    String Size;
    int indexofCurrent=0;
    String Email;

    public  WishlistAdaptar(Context context, ArrayList<Item> list,ArrayList<Item> editlist) {
        mContext = context;
        this.ItemList = list;
        this.OriginalList=editlist;
        inflater = LayoutInflater.from(mContext);
        this.modellist = new ArrayList<Item>();
        this.modellist.addAll(list);

    }
    static class ViewHolder{
        TextView Name;
        TextView Price;

        ImageView Image;
        ImageView Cross;
    }
    @Override
    public int getCount() {
        return ItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return ItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final Item currentShirt= ItemList.get(position);

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.cartlist, null, false);
           ViewHolder viewHolder = new ViewHolder();
            viewHolder.Name =
                    (TextView) convertView.findViewById(R.id.cartNAME);
            viewHolder.Price =
                    (TextView) convertView.findViewById(R.id.cartPrice);
            viewHolder.Image =
                    (ImageView) convertView.findViewById(R.id.cartImage);
            viewHolder.Cross =
                    (ImageView) convertView.findViewById(R.id.cartRemove);


            convertView.setTag(viewHolder);
        }
        ImageView b2= ((ViewHolder) convertView.getTag()).Cross;;
        b2.setTag(position);
        b2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int pos = (int)arg0.getTag();
                ItemList.remove(pos);
                WishlistAdaptar.this.notifyDataSetChanged();
                firebaseAuth= FirebaseAuth.getInstance();
                final FirebaseUser User=firebaseAuth.getCurrentUser();
                Email=firebaseAuth.getCurrentUser().getEmail();
                myref=FirebaseDatabase.getInstance().getReference("wishlist");
                check();
            }
        });



        ImageView b3=((ViewHolder) convertView.getTag()).Image;;
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDetail(currentShirt.itemname);
            }
        });

        TextView Name =
                ((ViewHolder) convertView.getTag()).Name;
        TextView Price=
                ((ViewHolder) convertView.getTag()).Price;
        ImageView ContactImage =
                ((ViewHolder) convertView.getTag()).Image;
        ImageView Cross =
                ((ViewHolder) convertView.getTag()).Cross;

        Name.setText(currentShirt.itemname);
        Price.setText(currentShirt.price);

        String imageUri = currentShirt.imageurl;
        Picasso.with(mContext).load(imageUri).into(ContactImage);

        String cross="https://upload.wikimedia.org/wikipedia/commons/thumb/c/c1/High-contrast-dialog-close.svg/600px-High-contrast-dialog-close.svg.png";
        Picasso.with(mContext).load(cross).into(Cross);

            /*String pathToFile = currentShirt.image;
            DownloadImageWithURLTask downloadTask = new DownloadImageWithURLTask(ContactImage);
            downloadTask.execute(pathToFile);*/
        return convertView;
    }
    public void updateDetail(String name) {
        Intent intent=new Intent(mContext,Detail.class);
        Toast.makeText(mContext,"Moving",Toast.LENGTH_LONG).show();
        intent.putExtra(EXTRA_MESSAGE,name);
        intent.putExtra("Edit_list",ItemList);
        intent.putExtra("Contact_list", OriginalList);
        mContext.startActivity(intent);
    }

   /* private class DownloadImageWithURLTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageWithURLTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String pathToFile = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(pathToFile).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }*/
    public void check ()
    {
        final String Email = firebaseAuth.getCurrentUser().getEmail();
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot x: dataSnapshot.getChildren())
                {
                    Wishlist temp;
                    temp=x.getValue(Wishlist.class);
                    String em=temp.uid;

                    if (Email.equals(em)) {
                        myref.removeEventListener(this);
                        oldList();
                        break;
                    }
                    indexofCurrent++;
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

        Toast.makeText(mContext,"Removed from Wishlist",Toast.LENGTH_SHORT).show();
        Wishlist current=new Wishlist();
        current.uid = Email;
        current.wisheditems=ItemList;
        String index = String.valueOf(indexofCurrent);
        indexofCurrent=0;
        myref.child(index).setValue(current);
    }
};





/*
public View getView(int position, View convertView, ViewGroup parent)
        {
        Shirts currentShirt= ShirtList.get(position);

        if(convertView == null) {
        convertView = inflater.inflate(R.layout.list, null, false);

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.Name =
        (TextView) convertView.findViewById(R.id.NAME);
        viewHolder.Price =
        (TextView) convertView.findViewById(R.id.Price);
        viewHolder.Quantity =
        (TextView) convertView.findViewById(R.id.Quantity);
        viewHolder.Image =
        (ImageView) convertView.findViewById(R.id.Image);

        convertView.setTag(viewHolder);

        }

        TextView Name =
        ((ViewHolder) convertView.getTag()).Name;
        TextView Price=
        ((ViewHolder) convertView.getTag()).Price;
        TextView Quantity =
        ((ViewHolder) convertView.getTag()).Quantity;
        ImageView ContactImage =
        ((ViewHolder) convertView.getTag()).Image;

        Name.setText(currentShirt.name);
        Price.setText(currentShirt.price);
        Quantity.setText(currentShirt.quantity);

        ContactImage.setImageResource(R.drawable.shirt_pic);

        return convertView;
        }

      */
/*  public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            ShirtList.clear();
            if (charText.length() == 0) {
                ShirtList.addAll(modellist);
            } else {
                for (Shirts latest : modellist) {
                    if (latest.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        contactList.add(latest);
                    }
                }
            }
            notifyDataSetChanged();
        }*/


