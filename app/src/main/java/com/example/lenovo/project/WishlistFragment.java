package com.example.lenovo.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WishlistFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_wishlist,container,false);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser User=firebaseAuth.getCurrentUser();
        if(User ==null)
        {
            Button registerButton=new Button(getActivity());
            registerButton.setLayoutParams(params);
            registerButton.setText("Register");
            registerButton.setBackgroundColor(R.color.colorPrimaryDark);
        }
        Reg=(Button)view.findViewById(R.id.Register);
        login=(Button)view.findViewById(R.id.Login) ;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),LoginDetail.class);
                getActivity().startActivity(intent);
            }
        });
      Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registeration(view);
            }
        });
        return view;
    }
    public void Registeration(View view)
    {
        Intent intent=new Intent(getActivity(),Register.class);
        getActivity().startActivity(intent);
    }
}
