package com.example.eyeonwaterapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.eyeonwaterapp.databinding.ActivityHistory3Binding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class History3Activity extends DrawerBaseActivity {
    LineChart mpLineChart;
    int colorArray[] = {R.color.color1, R.color.color2, R.color.color3};
    int[] colorClassArray = new int[]{Color.BLUE, Color.CYAN, Color.GREEN, Color.RED};
    String[] legendName = {"Tap1", "Tap2", "Tap3"};
    ActivityHistory3Binding activityHistory3Binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHistory3Binding = ActivityHistory3Binding.inflate(getLayoutInflater());
        setContentView(activityHistory3Binding.getRoot());
        allocateActivityTitle("Monthly History");

        FirebaseApp.initializeApp(this);
        // Check if the user is logged in. If not, redirect to MainActivity
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(History3Activity.this, MainActivity.class));
            finish();
            return; // Add this to prevent further execution of the code in this activity
        }
        String userId = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference monthRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Taps").child("Tap1").child("Data");

        Calendar calendar = Calendar.getInstance();
        String currentMonth = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());

        TextView textViewMonth = findViewById(R.id.textView8);
        textViewMonth.setText(currentMonth);

        monthRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int monthsum = 0;
                for (DataSnapshot monthSnapshot : snapshot.getChildren()) {
                    // Loop through each month node
                    for (DataSnapshot daySnapshot : monthSnapshot.getChildren()) {
                        // Loop through each day node
                        for (DataSnapshot hourSnapshot : daySnapshot.getChildren()) {
                            // Loop through each hour node
                            Integer hourData = hourSnapshot.getValue(Integer.class);
                            Log.d("History3Activity", "Hour Data: " + hourData);
                            if (hourData != null) {
                                monthsum += hourData;
                            }
                        }
                    }
                }
                TextView monthtext = findViewById(R.id.monthData);
                monthtext.setText(String.valueOf(monthsum));
                Log.d("History3Activity", "Total Sum: " + monthsum);

                LineDataSet lineDataSet1 = new LineDataSet(dataValues1(snapshot), "Tap 1");
                // Set the custom value formatter to the LineDataSet
                lineDataSet1.setValueFormatter(new IntegerValueFormatter());
                ArrayList<LineDataSet> dataSets = new ArrayList<>();
                dataSets.add(lineDataSet1);

                mpLineChart = findViewById(R.id.lineChart2);
                mpLineChart.setBackgroundColor(Color.WHITE);
                mpLineChart.setDrawGridBackground(false);
                mpLineChart.setDrawBorders(true);
                mpLineChart.setBorderWidth(2);
                mpLineChart.setBorderColor(Color.BLUE);

                lineDataSet1.setLineWidth(4);
                lineDataSet1.setColor(Color.BLUE);
                lineDataSet1.setDrawCircles(true);
                lineDataSet1.setDrawCircleHole(true);
                lineDataSet1.setCircleColor(Color.BLUE);
                lineDataSet1.setCircleHoleColor(Color.GRAY);
                lineDataSet1.setCircleRadius(5);
                lineDataSet1.setCircleHoleRadius(4);
                lineDataSet1.setValueTextSize(10);
                lineDataSet1.setValueTextColor(Color.BLACK);
                lineDataSet1.enableDashedLine(5, 5, 0);

                lineDataSet1.setDrawFilled(true);
                lineDataSet1.setFillColor(Color.parseColor("#006DFF"));

                Legend legend = mpLineChart.getLegend();
                legend.setEnabled(true);
                legend.setTextColor(Color.BLUE);
                legend.setTextSize(12);
                legend.setForm(Legend.LegendForm.SQUARE);
                legend.setFormSize(10);
                legend.setXEntrySpace(20);
                legend.setFormToTextSpace(10);

                LegendEntry[] legendEntries = new LegendEntry[3];
                for (int i = 0; i < legendEntries.length; i++) {
                    LegendEntry entry = new LegendEntry();
                    entry.formColor = colorClassArray[i];
                    entry.label = String.valueOf(legendName[i]);
                    legendEntries[i] = entry;
                }
                legend.setCustom(legendEntries);

                XAxis xAxis = mpLineChart.getXAxis();
                YAxis yAxisLeft = mpLineChart.getAxisLeft();
                YAxis yAxisRight = mpLineChart.getAxisRight();

                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set the X-axis position to bottom
                xAxis.setEnabled(false);

                xAxis.setValueFormatter(new History1Activity.MyAxisValueFormatter());
                // Assuming your data starts from hour 0 and goes up to hour 23, set the min and max values accordingly
                xAxis.setAxisMinimum(0f);

                // Assuming your data values are non-negative, set the minimum value of Y-axis to 0
                yAxisLeft.setAxisMinimum(0f);
                yAxisRight.setAxisMinimum(0f);

                Description description = new Description();
                description.setText("This Month Water Consumption");
                description.setTextColor(Color.BLUE);
                description.setTextSize(10);
                mpLineChart.setDescription(description);

                mpLineChart.getAxisRight().setEnabled(false); // Disable the right Y-axis

                LineData data = new LineData(lineDataSet1);
                mpLineChart.setData(data);
                mpLineChart.animateX(5000);
                mpLineChart.invalidate();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
                Log.e("History3Activity", "Database Error: " + error.getMessage());
            }
        });
    }
    private static class MyAxisValueFormatter extends ValueFormatter implements IAxisValueFormatter {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return "Day " + (int) value;
        }
    }
    private ArrayList<Entry> dataValues1(DataSnapshot snapshot) {
        ArrayList<Entry> dataVals = new ArrayList<>();
        int xIndex = 0;
        for (DataSnapshot monthSnapshot : snapshot.getChildren()) {
            for (DataSnapshot daySnapshot : monthSnapshot.getChildren()) {
                String date = daySnapshot.getKey(); // Get the date as a string
                int daySum = 0;
                for (DataSnapshot hourSnapshot : daySnapshot.getChildren()) {
                    Integer hourData = hourSnapshot.getValue(Integer.class);
                    if (hourData != null) {
                        daySum += hourData;
                    }
                }
                dataVals.add(new Entry(xIndex, daySum)); // Add the sum for each day to the dataVals list
                xIndex++; // Increment the X-axis index for the next date
            }
        }
        return dataVals;
    }
    public class IntegerValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return String.valueOf((int) value);
        }
    }
}