package com.example.eyeonwaterapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.example.eyeonwaterapp.databinding.ActivityMytapsBinding;
import java.text.DateFormat;
import java.util.Calendar;

public class MytapsActivity extends DrawerBaseActivity {
    ActivityMytapsBinding activityMytapsBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMytapsBinding = ActivityMytapsBinding.inflate(getLayoutInflater());
        setContentView(activityMytapsBinding.getRoot());
        allocateActivityTitle("About");

        CardView card1 = findViewById(R.id.cardView1);
        CardView card2 = findViewById(R.id.cardView2);
        CardView card3 = findViewById(R.id.cardView3);
        CardView card4 = findViewById(R.id.cardView4);
        CardView card5 = findViewById(R.id.cardView5);

        TextView text1 = findViewById(R.id.textView1);
        TextView text2 = findViewById(R.id.textView2);
        TextView text3 = findViewById(R.id.textView3);
        TextView text4 = findViewById(R.id.textView4);
        TextView text5 = findViewById(R.id.textView5);
        TextView text6 = findViewById(R.id.textView6);
        TextView text7 = findViewById(R.id.textView7);
        TextView text8 = findViewById(R.id.textView8);
        TextView text9 = findViewById(R.id.textView9);
        TextView text10 = findViewById(R.id.textView10);
        TextView text11 = findViewById(R.id.textView11);

        // Load the animation from the XML file
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        text1.startAnimation(fadeInAnimation);
        text2.startAnimation(fadeInAnimation);
        text3.startAnimation(fadeInAnimation);
        text4.startAnimation(fadeInAnimation);
        text5.startAnimation(fadeInAnimation);
        text6.startAnimation(fadeInAnimation);
        text7.startAnimation(fadeInAnimation);
        text8.startAnimation(fadeInAnimation);
        text9.startAnimation(fadeInAnimation);
        text10.startAnimation(fadeInAnimation);
        text11.startAnimation(fadeInAnimation);

        // Load the animation from the XML file
        Animation slideInAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        card1.startAnimation(slideInAnimation);
        card3.startAnimation(slideInAnimation);
        card5.startAnimation(slideInAnimation);

        Animation slideInAnotherAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_another);
        card2.startAnimation(slideInAnotherAnimation);
        card4.startAnimation(slideInAnotherAnimation);
    }
}