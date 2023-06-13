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

public class SignupActivity extends AppCompatActivity {
    EditText signupname, signupusername, signuppassword, signupemail;
    Button signupbutton, loginbutton;
    CheckBox showcheck_btn1;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        showcheck_btn1 = findViewById(R.id.checkbox_btn1);
        auth = FirebaseAuth.getInstance();

        signupname = findViewById(R.id.name);
        signupusername = findViewById(R.id.username);
        signupemail = findViewById(R.id.email);
        signuppassword = findViewById(R.id.password);
        signupbutton = findViewById(R.id.signupbtn);
        loginbutton = findViewById(R.id.loginbtn);

        showcheck_btn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //for show password
                    signuppassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //for hide password
                    signuppassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    signupbutton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String name, username, email, password;

            name = signupname.getText().toString();
            username = signupusername.getText().toString();
            email = signupemail.getText().toString();
            password = signuppassword.getText().toString();

            if (username.isEmpty()){
                signupusername.setError("Please enter your User Name");
                signupusername.requestFocus();
            } else if (name.isEmpty()) {
                signupname.setError("Please enter your Name");
                signupname.requestFocus();
            }else if (email.isEmpty()) {
                signupemail.setError("Please enter your Email");
                signupemail.requestFocus();
            }else if (password.isEmpty()) {
                signupusername.setError("Please enter your Password");
                signupusername.requestFocus();
            }else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            finish();
                        }else {
                            Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
    }
}