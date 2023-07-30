package com.example.eyeonwaterapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
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

        // Check if the user is logged in. If not, redirect to MainActivity
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(Home1Activity.this, MainActivity.class));
            finish();
            return; // Add this to prevent further execution of the code in this activity
        }

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
                // Check if the reading in tapRef is greater than 200 and show notification if true
                if (sum > 200) {
                    showNotification();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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

    private void showNotification() {
        // Create an explicit intent for your MainActivity or any other activity you want to open when the user taps the notification
        Intent intent = new Intent(this, Home1Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.drop2)
                .setContentTitle("Eye On Water")
                .setContentText("Excess water usage detected.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        createNotificationChannel();
        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Water Usage Updates";
            String channelDescription = "\"Receive alerts and updates about your daily water usage";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_id", channelName, importance);
            channel.setDescription(channelDescription);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}