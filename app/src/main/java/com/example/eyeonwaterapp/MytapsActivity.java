package com.example.eyeonwaterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
        allocateActivityTitle("My Taps");

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        TextView textViewDate = findViewById(R.id.date1);
        textViewDate.setText(currentDate);
    }
}