package com.example.eyeonwaterapp;

import android.os.Bundle;
import android.widget.TextView;

import com.example.eyeonwaterapp.databinding.ActivityHome1Binding;

import java.text.DateFormat;
import java.util.Calendar;

public class Home1Activity extends DrawerBaseActivity {

    ActivityHome1Binding activityHome1Binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHome1Binding = ActivityHome1Binding.inflate(getLayoutInflater());
        setContentView(activityHome1Binding.getRoot());
        allocateActivityTitle("Dashboard");

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        TextView textViewDate = findViewById(R.id.date);
        textViewDate.setText(currentDate);
    }
}
