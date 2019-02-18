package com.example.lenovo.project;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable{
    String itemname;
    String itemid;
    String sizel;
    String sizem;
    String sizes;
    String imageurl;
    String price;

    public Item(){
        this.itemname = null;

        this.price=null;
        this.itemid = null;
        this.sizel = null;

        this.sizem = null;
        this.sizes = null;
        this.imageurl=null;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Item(String itemname, String price, String itemid, String sizel, String sizem, String sizes, String url) {
        this.itemname = itemname;
        this.price=price;
        this.itemid = itemid;
        this.sizel = sizel;
        this.sizem = sizem;
        this.sizes = sizes;
        this.imageurl=url;
    }
    public String getImageurl() {
        return imageurl;
    }

    public String getItemname() {
        return itemname;
    }

    public String getItemid() {
        return itemid;
    }

    public String getSizel() {
        return sizel;
    }

    public String getSizem() {
        return sizem;
    }

    public String getSizes() {
        return sizes;
    }

    @Override
    public String toString() {
        return this.itemid+"\n"+this.itemname+"\n"+this.price+"\n"+this.sizel+"\n"+this.sizem+"\n"+this.sizes+"\n"+this.imageurl;
    }

    private Item(Parcel in) {
        itemname=in.readString();
        itemid = in.readString();
        sizel = in.readString();
        sizem= in.readString();
        sizes= in.readString();
        imageurl= in.readString();
        price=in.readString();
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemname);
        dest.writeString(itemid);
        dest.writeString(sizel);
        dest.writeString(sizem);
        dest.writeString(sizes);
        dest.writeString(imageurl);
        dest.writeString(price);
    }
    public static final Creator<Item> CREATOR = new Creator<Item>() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
