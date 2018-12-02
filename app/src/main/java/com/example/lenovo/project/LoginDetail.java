package com.example.lenovo.project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginDetail extends AppCompatActivity {
    EditText Email;
    EditText Password;
    Button Login;
    TextView Registertext;
    private FirebaseAuth firebaseAuth;
    String mEmail;
    String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_detail);
        Email=findViewById(R.id.loginEmail);
        Password=findViewById(R.id.loginPasswaord);
        Login=findViewById(R.id.LoginLogin);
        Registertext=findViewById(R.id.loginRegister);
        mEmail=Email.getText().toString();
        mPassword=Password.getText().toString();
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser User=firebaseAuth.getCurrentUser();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    validate(mEmail,mPassword);
            }
        });

        Registertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Register.class);
                startActivity(intent);
            }
        });
    }
    public void validate(String Emai,String passwordd)
    {
            firebaseAuth.createUserWithEmailAndPassword(Emai,passwordd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                }
            });
    }
}
