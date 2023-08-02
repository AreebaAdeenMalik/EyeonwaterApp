package com.example.eyeonwaterapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    EditText loginemail, loginpassword;
    Button loginbutton, regbutton, forgetbtn;
    CheckBox showcheck_btn;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        showcheck_btn = findViewById(R.id.checkbox_btn);
        auth = FirebaseAuth.getInstance();

        loginemail = findViewById(R.id.email1);
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
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;

                email = loginemail.getText().toString();
                password = loginpassword.getText().toString();

                if (email.isEmpty()) {
                    loginemail.setError("Please enter your Email");
                    loginemail.requestFocus();
                } else if (password.isEmpty()) {
                    loginpassword.setError("Please enter your Password");
                    loginpassword.requestFocus();
                } else {
                    // Check if the user is already logged in
                    if (auth.getCurrentUser() != null) {
                        // User is already logged in, proceed to Home1Activity
                        startActivity(new Intent(LoginActivity.this, Home1Activity.class));
                        finish();
                    } else {
                        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(LoginActivity.this, Home1Activity.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
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
    }
    public void txtSignInForgotPasswordClicked(View v){
        Intent intent = new Intent(this, Forgetpassword.class);
        startActivity(intent);
    }
}