package com.example.eyeonwaterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.eyeonwaterapp.databinding.ActivityMytapsBinding;

public class MytapsActivity extends DrawerBaseActivity {

    ActivityMytapsBinding activityMytapsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMytapsBinding = ActivityMytapsBinding.inflate(getLayoutInflater());
        setContentView(activityMytapsBinding.getRoot());
        allocateActivityTitle("My Taps");
    }
}