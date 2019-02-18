package com.example.lenovo.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;

public class Detail extends AppCompatActivity{

    ArrayList<Item> ItemList=new ArrayList<>();
    ArrayList<Item> ItemList2=new ArrayList<>();

    Wishlist current=new Wishlist();

    String Email;
    DatabaseReference myref;

    ArrayList<Item> EditList=new ArrayList<>();
    ArrayList<Item> serviceList=new ArrayList<>();
    private FirebaseAuth firebaseAuth;
    ImageView image;
    TextView Name;
    TextView  Price;
    TextView Quantity;
    Button Cart;
    Button wishlist;
    String Size;
    String rate;
    int counter=0;
    Item itemstobuy =new Item();
    Item temp;
    Button Ratingbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent i=getIntent();
        firebaseAuth= FirebaseAuth.getInstance();
        final FirebaseUser User=firebaseAuth.getCurrentUser();


        if(TextUtils.isEmpty(ProductFragment.EXTRA_MESSAGE))
        {
            ItemList=i.getParcelableArrayListExtra("Contact_list");
            String name=i.getStringExtra(cartadapter.EXTRA_MESSAGE);
            EditList=i.getParcelableArrayListExtra("Edit_list");
            temp=getItem(name);
        }
        else
        {
            ItemList=i.getParcelableArrayListExtra("Contact_list");
            String name=i.getStringExtra(ProductFragment.EXTRA_MESSAGE);
            temp=getItem(name);
        }
        image=findViewById(R.id.detailImage);
        Name=findViewById(R.id.detailNAME);
        Price=findViewById(R.id.detailPrice);
        Quantity=findViewById(R.id.detailQuantity);
        Cart=findViewById(R.id.detailCart);
        wishlist=findViewById(R.id.detailwishlist);
        Ratingbutton=findViewById(R.id.Rating);


        Spinner spinner = (Spinner) findViewById(R.id.size_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.size_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Size=parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Spinner ratingspinner = (Spinner) findViewById(R.id.rating_spinner);
        final ArrayAdapter<CharSequence> ratingadapter = ArrayAdapter.createFromResource(this, R.array.rating_array, android.R.layout.simple_spinner_dropdown_item);
        ratingadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ratingspinner.setAdapter(ratingadapter);
        ratingspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rate=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Ratingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rate.equals("Rating ▼"))
                {
                    Toast.makeText(getApplicationContext(),"Select a number From the Drop Down",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Rating newRating=new Rating();
                    newRating.rating=Integer.parseInt(rate);
                    Item itemstobuy = new Item();
                    itemstobuy.setPrice(temp.price);
                    itemstobuy.itemid = temp.getItemid();
                    itemstobuy.itemname = temp.itemname;
                    itemstobuy.imageurl = temp.imageurl;
                    itemstobuy.sizes = "1";
                    itemstobuy.sizem = "0";
                    itemstobuy.sizel = "0";
                    newRating.item=itemstobuy;
                    final FirebaseUser User=firebaseAuth.getCurrentUser();
                    Email=firebaseAuth.getCurrentUser().getEmail();
                    myref=FirebaseDatabase.getInstance().getReference("Rating");
                    String autogeneratedKey=myref.push().getKey();
                    myref.child(autogeneratedKey).setValue(newRating);
                    Toast.makeText(getApplicationContext(),"Thank you For Rating this Product",Toast.LENGTH_SHORT).show();
                }

            }
        });


        Name.setText(temp.itemname);
        Price.setText(temp.price);
        final int small=Integer.parseInt(temp.sizes);
        final int medium=Integer.parseInt(temp.sizem);
        final int large=Integer.parseInt(temp.sizel);
        final int total=small+medium+large;
        if(total==0)
        {
            Quantity.setText("Out of the Stock");
        }
        else
        {
            Quantity.setText(String.valueOf(total));
        }
        String imageUri = temp.imageurl;
        Picasso.with(this).load(imageUri).into(image);


     /*   String pathToFile = temp.image;
        DownloadImageWithURLTask downloadTask = new DownloadImageWithURLTask(image);
        downloadTask.execute(pathToFile);*/

        Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Size.equals("Size ▼")) {
                    Toast.makeText(getApplicationContext(), "Select a Size", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                    if (Size.equals("Small") && small == 0) {
                        counter = 1;
                    }
                    if (Size.equals("Medium") && medium == 0) {
                        counter = 1;
                    }
                    if (Size.equals("Large") && large == 0) {
                        counter = 1;
                    }
                    if (counter == 0) {
                        if (User == null) {
                            Toast.makeText(getApplicationContext(), "Please Sign in for this", Toast.LENGTH_SHORT).show();
                        } else {

                            Item itemstobuy = new Item();
                            itemstobuy.setPrice(temp.price);
                            itemstobuy.itemid = temp.getItemid();
                            itemstobuy.itemname = temp.itemname;
                            itemstobuy.imageurl = temp.imageurl;
                            if (Size.equals("Small")) {
                                itemstobuy.sizes = "1";
                                itemstobuy.sizem = "0";
                                itemstobuy.sizel = "0";
                            } else if (Size.equals("Medium")) {
                                itemstobuy.sizes = "0";
                                itemstobuy.sizem = "1";
                                itemstobuy.sizel = "0";
                            } else {
                                itemstobuy.sizes = "0";
                                itemstobuy.sizem = "0";
                                itemstobuy.sizel = "1";
                            }
                            serviceList.clear();
                            serviceList.add(itemstobuy);
                            Intent intent = new Intent(getApplicationContext(), CartService.class);
                            intent.putExtra("Contact_list", serviceList);
                            startService(intent);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Out of Stock", Toast.LENGTH_SHORT).show();
                        counter=0;
                    }

                }
            }
        });


        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Size.equals("Size ▼")) {
                    Toast.makeText(getApplicationContext(), "Select a Size", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (User == null) {
                        Toast.makeText(getApplicationContext(), "Please Sign in for this", Toast.LENGTH_SHORT).show();
                    } else {
                        itemstobuy.setPrice(temp.price);
                        itemstobuy.itemid = temp.getItemid();
                        itemstobuy.itemname = temp.itemname;
                        itemstobuy.imageurl = temp.imageurl;
                        if (Size.equals("Small")) {
                            itemstobuy.sizes = "1";
                            itemstobuy.sizem = "0";
                            itemstobuy.sizel = "0";
                        } else if (Size.equals("Medium")) {
                            itemstobuy.sizes = "0";
                            itemstobuy.sizem = "1";
                            itemstobuy.sizel = "0";
                        } else {
                            itemstobuy.sizes = "0";
                            itemstobuy.sizem = "0";
                            itemstobuy.sizel = "1";
                        }
                        serviceList.clear();
                        serviceList.add(itemstobuy);
                        Intent intent = new Intent(getApplicationContext(), WishlistService.class);
                        intent.putExtra("Contact_list", serviceList);
                        startService(intent);
                    }
                }
            }
        });
    }
    public Item getItem(String name)
    {
        for (int i=0;i<ItemList.size();i++)
        {
            if(ItemList.get(i).itemname.equals(name))
            {
                return ItemList.get(i);
            }
        }
        return  null;
    }


    private class DownloadImageWithURLTask extends AsyncTask<String, Void, Bitmap> {
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
    }
}
