package com.example.eyeonwaterapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity2 extends AppCompatActivity {

    Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main2);

        // Check if the user is already logged in. If yes, redirect to Home1Activity
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(MainActivity2.this, Home1Activity.class));
            finish();
            return; // Add this to prevent further execution of the code in this activity
        }

        login = findViewById(R.id.login1);
        register = findViewById(R.id.register1);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity2.this, "Login has been clicked", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity2.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity2.this, "Register has been clicked", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity2.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}