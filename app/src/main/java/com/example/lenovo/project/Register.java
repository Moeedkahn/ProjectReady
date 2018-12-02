package com.example.lenovo.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    EditText Name;
    EditText Email;
    EditText Password;
    EditText Phone;
    EditText Address;
    Button Registerr;
    String mName;
    String mEmail;
    String mPassword;
    String mPhone;
    String mAddress;
    ProgressDialog progressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth=FirebaseAuth.getInstance();
        Name=findViewById(R.id.registerName);
        Email=findViewById(R.id.registeremail);
        Password=findViewById(R.id.registerPasswaord);
        Phone=findViewById(R.id.registerPhone);
         Address=findViewById(R.id.registerAddress);
        Registerr=findViewById(R.id.registerRegister);

        progressBar=new ProgressDialog(this);



        Registerr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();
            }
        });
    }
    public void RegisterUser()
    {
        mName=Name.getText().toString();
        mEmail=Email.getText().toString();
        mPassword=Password.getText().toString();
        mPhone=Phone.getText().toString();
        mAddress=Address.getText().toString();
        if(TextUtils.isEmpty(mName))
        {
            Toast.makeText(this,"Please Enter Name",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(mEmail))
        {
            Toast.makeText(this,"Please Enter Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(mPassword))
        {
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(mPhone))
        {
            Toast.makeText(this,"Please Enter Phone",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(mAddress))
        {
            Toast.makeText(this,"Please Enter Address",Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setMessage("Registering in Progress....");
        progressBar.show();

        firebaseAuth.createUserWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"Registering",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(Register.this,LoginDetail.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Unsuccessful",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
