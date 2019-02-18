package com.example.lenovo.project;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class User implements Parcelable{
    public String useremail;
    public String password;
    public String phno;
    public String address;
   /* public Cart cart;
    public Wishlist wishlist;*/

    public User(){ }

    public User(String useremail, String password, String phno, String address) {
        this.useremail = useremail;
        this.password = password;
        this.phno = phno;
        this.address = address;
    }

    public String getUseremail() {

        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    private User(Parcel in) {
        this.useremail=in.readString();
        password = in.readString();
       phno = in.readString();
        address = in.readString();
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(useremail);
        dest.writeString(password);
        dest.writeString(phno);
        dest.writeString(address);
        //dest.writeString(cart);
        //dest.writeString(wishlist);
    }
    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}