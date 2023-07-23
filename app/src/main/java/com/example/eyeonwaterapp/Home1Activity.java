package com.example.eyeonwaterapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.eyeonwaterapp.databinding.ActivityHome1Binding;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Home1Activity extends DrawerBaseActivity {
    ActivityHome1Binding activityHome1Binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHome1Binding = ActivityHome1Binding.inflate(getLayoutInflater());
        setContentView(activityHome1Binding.getRoot());
        allocateActivityTitle("Dashboard");

        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);   // Enable offline persistence if needed

        DatabaseReference tapRef = FirebaseDatabase.getInstance().getReference().child("Taps").child("Tap1").child("Data");
        DatabaseReference tap1Ref = FirebaseDatabase.getInstance().getReference().child("Taps").child("Tap1").child("Controls").child("Solenoid");
        DatabaseReference tap2Ref = FirebaseDatabase.getInstance().getReference().child("Taps").child("Tap2").child("Data");
        DatabaseReference tap3Ref = FirebaseDatabase.getInstance().getReference().child("Taps").child("Tap3").child("Data");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(calendar.getTime());
        TextView textViewDate = findViewById(R.id.date);
        textViewDate.setText(currentDate);
        TextView waterFlowTextView2 = findViewById(R.id.tap2text);
        TextView waterFlowTextView3 = findViewById(R.id.tap3text);
        tapRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sum = 0;
                for (DataSnapshot monthSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot daySnapshot : monthSnapshot.getChildren()) {
                        String date = daySnapshot.getKey();
                        if (currentDate.equals(date)) { // Match the date
                            for (DataSnapshot hourSnapshot : daySnapshot.getChildren()) {
                                int hourData = hourSnapshot.getValue(Integer.class);
                                sum += hourData;
                            }
                        }
                    }
                }
                TextView waterFlowTextView1 = findViewById(R.id.tap1text);
                TextView totalTextView = findViewById(R.id.total);
                waterFlowTextView1.setText(String.valueOf(sum));
                totalTextView.setText(String.valueOf(sum));
                Log.d("Home1Activity", "Total Sum: " + sum);
            }
                @Override
                public void onCancelled (@NonNull DatabaseError error){
                    // Handle the error
                    Log.e("Home1Activity", "Database Error: " + error.getMessage());
                }
        });
        tap2Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Retrieve the sensor data here
                int Data = snapshot.getValue(Integer.class);
                // Update your UI or perform any necessary actions with the sensor data
                waterFlowTextView2.setText(String.valueOf(Data));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //handle any errors
            }
        });
        tap3Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Retrieve the sensor data here
                int Data = snapshot.getValue(Integer.class);
                // Update your UI or perform any necessary actions with the sensor data
                waterFlowTextView3.setText(String.valueOf(Data));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //handle any errors
            }
        });
        tap1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Retrieve the switch state here
                boolean Solenoid = snapshot.getValue(Boolean.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors
            }
        });
        Switch tap1Switch = findViewById(R.id.switch1);
        tap1Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //update switch state in Firebase
                tap1Ref.setValue(b);
            }
        });
    }
    /* Define an inner class for the hour
    private class Hour {
        private double data;
        public Hour(double data) {
            this.data = data;
        }
        public double getData() {
            return data;
        }
        public void setData(double data) {
            this.data = data;
        }
    }
    // Define an inner class for the day
    private class Day {
        private Hour data;
        public Day(Hour data) {
            this.data = data;
        }
        public Hour getData() {
            return data;
        }
        public void setData(Hour data) {
            this.data = data;
        }
    }
    // Define an inner class for the month
    private class Month {
        private Day data;
        public Month(Day data) {
            this.data = data;
        }
        public Day getData() {
            return data;
        }
        public void setData(Day data) {
            this.data = data;
        }
    } */
}