package com.example.eyeonwaterapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.google.firebase.database.ValueEventListener;

import Model.User;

public class SignupActivity extends AppCompatActivity {

    ImageView back2;
    EditText signupname, signupusername, signuppassword, signupemail;
    Button signupbutton, loginbutton;
    CheckBox showcheck_btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        back2 = findViewById(R.id.ivBack1);
        showcheck_btn1 = findViewById(R.id.checkbox_btn1);

        signupname = findViewById(R.id.name);
        signupusername = findViewById(R.id.username);
        signupemail = findViewById(R.id.email);
        signuppassword = findViewById(R.id.password);
        signupbutton = findViewById(R.id.signupbtn);
        loginbutton = findViewById(R.id.loginbtn);

        showcheck_btn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    //for show password
                    signuppassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    //for hide password
                    signuppassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog(SignupActivity.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.child(signupusername.getText().toString()).exists())
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignupActivity.this, "Username already register", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            mDialog.dismiss();
                            User user = new User(signupname.getText().toString(), signuppassword.getText().toString(), signupemail.getText().toString());
                            table_user.child(signupusername.getText().toString()).setValue(user);
                            Toast.makeText(SignupActivity.this, "Signup successfully ! Now you can Login !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                String fullname = signupname.getText().toString();
                String username = signupusername.getText().toString();
                String email = signupemail.getText().toString();
                String password = signuppassword.getText().toString();

                //validate input values
                if (TextUtils.isEmpty(fullname)) {
                    signupname.setError("Full Name is required");
                    signupname.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(username)) {
                    signupusername.setError("User Name is required");
                    signupusername.requestFocus();
                }
                if (TextUtils.isEmpty(email)) {
                    signupemail.setError("Email is required");
                    signupemail.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    signuppassword.setError("Password is required");
                    signuppassword.requestFocus();
                    return;
                }
            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
    }
}