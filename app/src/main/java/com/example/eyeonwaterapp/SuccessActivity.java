package com.example.eyeonwaterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SuccessActivity extends AppCompatActivity {

    Button successMessageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        successMessageBtn = findViewById(R.id.success_message_btn);

        successMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SuccessActivity.this, "Password has been updated", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SuccessActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}