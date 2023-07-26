package com.example.eyeonwaterapp;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.eyeonwaterapp.databinding.ActivityHistory1Binding;
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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class History1Activity extends DrawerBaseActivity {
    LineChart mpLineChart;
    int colorArray[] = {R.color.color1, R.color.color2, R.color.color3};
    int[] colorClassArray = new int[] {Color.BLUE, Color.CYAN, Color.GREEN, Color.RED};
    String[] legendName = {"Tap1", "Tap2", "Tap3"};
    ActivityHistory1Binding activityHistory1Binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHistory1Binding = ActivityHistory1Binding.inflate(getLayoutInflater());
        setContentView(activityHistory1Binding.getRoot());
        allocateActivityTitle("Daily History");

        FirebaseApp.initializeApp(this);
        DatabaseReference dayRef = FirebaseDatabase.getInstance().getReference().child("Taps").child("Tap1").child("Data");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(calendar.getTime());
        TextView textViewDate = findViewById(R.id.textView6);
        textViewDate.setText(currentDate);
        dayRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int daysum = 0;
                for (DataSnapshot monthSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot daySnapshot : monthSnapshot.getChildren()) {
                        String date = daySnapshot.getKey();
                        if (currentDate.equals(date)) { // Match the date
                            for (DataSnapshot hourSnapshot : daySnapshot.getChildren()) {
                                int hourData = hourSnapshot.getValue(Integer.class);
                                daysum += hourData;
                            }
                        }
                    }
                }
                TextView totalDay = findViewById(R.id.daytext);
                totalDay.setText(String.valueOf(daysum));
                Log.d("History1Activity", "Total Sum: " + daysum);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
                Log.e("History1Activity", "Database Error: " + error.getMessage());
            }
        });
        mpLineChart = (LineChart) findViewById(R.id.lineChart);
        fetchDataFromFirebase();
    }
    private void fetchDataFromFirebase() {
        DatabaseReference dayRef = FirebaseDatabase.getInstance().getReference().child("Taps").child("Tap1").child("Data");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(calendar.getTime());
        dayRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<HourData> hourDataList = new ArrayList<HourData>();

                for (DataSnapshot monthSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot daySnapshot : monthSnapshot.getChildren()) {
                        String date = daySnapshot.getKey();
                        if (currentDate.equals(date)) { // Match the date
                            for (DataSnapshot hourSnapshot : daySnapshot.getChildren()) {
                                String hourString = hourSnapshot.getKey();
                                int hour = Integer.parseInt(hourString.substring(11, 13)); // Extract the hour from the timestamp
                                int hourData = hourSnapshot.getValue(Integer.class);
                                hourDataList.add(new HourData(hour, hourData));
                            }
                        }
                    }
                }
                // After retrieving data, set up the line chart
                setupLineChart(hourDataList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
                Log.e("History1Activity", "Database Error: " + error.getMessage());
            }
        });
    }
    private void setupLineChart(ArrayList<HourData> hourDataList) {
        mpLineChart.setBackgroundColor(Color.WHITE);
        mpLineChart.setDrawGridBackground(false);
        mpLineChart.setDrawBorders(true);
        mpLineChart.setBorderWidth(2);
        mpLineChart.setBorderColor(Color.BLUE);

        LineDataSet lineDataSet1 = new LineDataSet(dataValues1(hourDataList), "Tap 1");

        lineDataSet1.setLineWidth(4);
        lineDataSet1.setColor(Color.RED);
        lineDataSet1.setDrawCircles(true);
        lineDataSet1.setDrawCircleHole(true);
        lineDataSet1.setCircleColor(Color.BLUE);
        lineDataSet1.setCircleHoleColor(Color.GRAY);
        lineDataSet1.setCircleRadius(5);
        lineDataSet1.setCircleHoleRadius(4);
        lineDataSet1.setValueTextSize(15);
        lineDataSet1.setValueTextColor(Color.BLUE);
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

        xAxis.setValueFormatter(new MyAxisValueFormatter());
        // Assuming your data starts from hour 0 and goes up to hour 23, set the min and max values accordingly
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(23f);

        // Assuming your data values are non-negative, set the minimum value of Y-axis to 0
        yAxisLeft.setAxisMinimum(0f);
        yAxisRight.setAxisMinimum(0f);

        Description description = new Description();
        description.setText("Today's Water Consumption");
        description.setTextColor(Color.BLUE);
        description.setTextSize(10);
        mpLineChart.setDescription(description);

        mpLineChart.getAxisRight().setEnabled(false); // Disable the right Y-axis

        LineData data = new LineData(lineDataSet1);
        mpLineChart.setData(data);
        mpLineChart.animateX(5000);
        mpLineChart.invalidate();
    }
    private ArrayList<Entry> dataValues1(ArrayList<HourData> hourDataList) {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        for (int i = 0; i < hourDataList.size(); i++) {
            HourData hourData = hourDataList.get(i);
            dataVals.add(new Entry(hourData.getHour(), hourData.getDataValue()));
        }
        return dataVals;
    }
    static class MyAxisValueFormatter extends ValueFormatter implements IAxisValueFormatter {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return "Hour " + (int) value;
        }
    }
    // Custom data class to store the hour and data value
    public static class HourData {
        private int hour;
        private int dataValue;

        public HourData(int hour, int dataValue) {
            this.hour = hour;
            this.dataValue = dataValue;
        }

        public int getHour() {
            return hour;
        }
        public int getDataValue() {
            return dataValue;
        }
    }
}