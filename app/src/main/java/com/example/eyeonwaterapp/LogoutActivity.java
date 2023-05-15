package com.example.eyeonwaterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.eyeonwaterapp.databinding.ActivityLogoutBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LogoutActivity extends DrawerBaseActivity {

    Button btn1, btn2;
    ActivityLogoutBinding activityLogoutBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLogoutBinding = ActivityLogoutBinding.inflate(getLayoutInflater());
        setContentView(activityLogoutBinding.getRoot());
        allocateActivityTitle("Logout");

        btn1 = findViewById(R.id.button);
        btn2  = findViewById(R.id.button2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(LogoutActivity.this, LoginActivity.class));
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(LogoutActivity.this, Home1Activity.class));
            }
        });

    }
}