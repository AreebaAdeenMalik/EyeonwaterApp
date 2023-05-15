package com.example.eyeonwaterapp;

import android.os.Bundle;

import com.example.eyeonwaterapp.databinding.ActivityHome1Binding;

public class Home1Activity extends DrawerBaseActivity {

    ActivityHome1Binding activityHome1Binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHome1Binding = ActivityHome1Binding.inflate(getLayoutInflater());
        setContentView(activityHome1Binding.getRoot());
        allocateActivityTitle("Dashboard");
    }
}
