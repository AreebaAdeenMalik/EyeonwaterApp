package com.example.eyeonwaterapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Forgetpassword extends AppCompatActivity {

    ImageView back3;
    private EditText edittextemail;
    private Button btn;
    private String email;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);

        back3 = findViewById(R.id.ivBack2);

        edittextemail = (EditText) findViewById(R.id.editTextTextEmailAddress2);
        btn = findViewById(R.id.button);

        dialog = new ProgressDialog(Forgetpassword.this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading....");

        back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Forgetpassword.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = edittextemail.getText().toString();

                if (email.isEmpty()) {
                    Toast.makeText(Forgetpassword.this, "Please provide your Email", Toast.LENGTH_SHORT).show();
                } else {
                    forgotPassword();
                }
            }
        });
    }

    private void forgotPassword() {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(Forgetpassword.this, "Check Your Email", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Forgetpassword.this, LoginActivity.class));
                    finish();
                }else {
                    Toast.makeText(Forgetpassword.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}