package com.example.eyeonwaterapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.eyeonwaterapp.databinding.ActivityLogoutBinding;
import com.google.android.material.shadow.ShadowRenderer;
import com.google.firebase.auth.FirebaseAuth;

public class LogoutActivity extends DrawerBaseActivity {
    Button btn1, btn2;
    ActivityLogoutBinding activityLogoutBinding;
    FirebaseAuth auth;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLogoutBinding = ActivityLogoutBinding.inflate(getLayoutInflater());
        setContentView(activityLogoutBinding.getRoot());
        allocateActivityTitle("Logout");

        btn1 = findViewById(R.id.button);
        btn2  = findViewById(R.id.button2);
        auth = FirebaseAuth.getInstance();
        preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                preferences.edit().putBoolean("isLoggedIn", false).apply(); // Set the flag to indicate the user is logged out
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences.edit().putBoolean("isLoggedIn", true).apply(); // Set the flag to indicate the user is logged in
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(LogoutActivity.this, Home1Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}