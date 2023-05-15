package com.example.eyeonwaterapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import Model.User;

public class LoginActivity extends AppCompatActivity {

    ImageView back1;
    EditText loginusername, loginpassword;
    Button loginbutton, regbutton, forgetbtn;
    CheckBox showcheck_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        back1 = findViewById(R.id.ivBack);
        showcheck_btn = findViewById(R.id.checkbox_btn);

        loginusername = findViewById(R.id.username);
        loginpassword = findViewById(R.id.password);
        loginbutton = findViewById(R.id.loginbtn);
        regbutton = findViewById(R.id.registerbtn);

        forgetbtn = findViewById(R.id.button);

        showcheck_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    //for show password
                    loginpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    //for hide password
                    loginpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog = new ProgressDialog(LoginActivity.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Check if user not exist in database
                        if (snapshot.child(loginusername.getText().toString()).exists()){
                            //Get User info
                            //
                            mDialog.dismiss();
                            User user = snapshot.child(loginusername.getText().toString()).getValue(User.class);
                            if (user.getPassword().equals(loginpassword.getText().toString()))
                            {
                                Toast.makeText(LoginActivity.this, "Log in successfully !", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, Home1Activity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "Wrong Password !!!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            mDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "User Does not Exist in database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                String username = loginusername.getText().toString();
                String password = loginpassword.getText().toString();

                //validate input values
                if (TextUtils.isEmpty(username)) {
                    loginusername.setError("User Name is required");
                    loginusername.requestFocus();
                }
                if (TextUtils.isEmpty(password)) {
                    loginpassword.setError("Password is required");
                    loginpassword.requestFocus();
                    return;
                }
            }
        });

        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
               startActivity(intent);
            }
        });

        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
    }
    public void txtSignInForgotPasswordClicked(View v){
        Intent intent = new Intent(this, Forgetpassword.class);
        startActivity(intent);
    }
}