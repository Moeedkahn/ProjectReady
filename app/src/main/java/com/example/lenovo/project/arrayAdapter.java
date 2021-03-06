package com.example.lenovo.project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class arrayAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<Item> modellist;
    private ArrayList<Item> ItemList;
    Bitmap bitmap;

    public  arrayAdapter(Context context, ArrayList<Item> list) {
        mContext = context;
        this.ItemList = list;
        inflater = LayoutInflater.from(mContext);
        this.modellist = new ArrayList<Item>();
        this.modellist.addAll(list);
    }
    static class ViewHolder{
        TextView Name;
        TextView Price;
        ImageView Image;
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
        Item currentShirt= ItemList.get(position);

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list, null, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.Name =
                    (TextView) convertView.findViewById(R.id.NAME);
            viewHolder.Price =
                    (TextView) convertView.findViewById(R.id.Price);
            viewHolder.Image =
                    (ImageView) convertView.findViewById(R.id.Image);

            convertView.setTag(viewHolder);
        }

        TextView Name =
                ((ViewHolder) convertView.getTag()).Name;
        TextView Price=
                ((ViewHolder) convertView.getTag()).Price;
        ImageView ContactImage =
                ((ViewHolder) convertView.getTag()).Image;

        Name.setText(currentShirt.itemname);
        Price.setText(currentShirt.price);

        String imageUri=currentShirt.imageurl;
        Picasso.with(mContext).load(imageUri).into(ContactImage);

            /*String pathToFile = currentShirt.image;
            DownloadImageWithURLTask downloadTask = new DownloadImageWithURLTask(ContactImage);
            downloadTask.execute(pathToFile);*/
        return convertView;
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

