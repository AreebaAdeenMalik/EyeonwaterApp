package com.example.eyeonwaterapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;
    Animation topAnim, bottomAnim;
    ImageView image;
    TextView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        // Check the login status
        boolean isLoggedIn = checkLoginStatus();

        //animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //hooks
        image = findViewById(R.id.imageView);
        logo = findViewById(R.id.textView2);

        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLoggedIn) {
                    // User is already logged in, redirect to the home screen
                    Intent intent = new Intent(MainActivity.this, Home1Activity.class);
                    startActivity(intent);
                } else {
                    // User is not logged in, show MainActivity2
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(intent);
                }
                finish();
            }
        }, SPLASH_SCREEN);
    }
    // Helper method to check login status
    private boolean checkLoginStatus() {
        // Implement your login status check logic here
        // For example, you can use SharedPreferences to store login status
        // and check if the user is logged in or not
        // Replace this with your actual login status check code
        return false;
    }
}