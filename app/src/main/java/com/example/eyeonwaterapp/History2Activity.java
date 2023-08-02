package com.example.eyeonwaterapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.example.eyeonwaterapp.databinding.ActivityHistory2Binding;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class History2Activity extends DrawerBaseActivity {
    LineChart mpLineChart;
    int colorArray[] = {R.color.color1, R.color.color2, R.color.color3};
    int[] colorClassArray = new int[] {Color.BLUE, Color.CYAN, Color.GREEN, Color.RED};
    String[] legendName = {"Tap1", "Tap2", "Tap3"};
    ActivityHistory2Binding activityHistory2Binding;
    private DatabaseReference weekRef;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private ArrayList<Integer> totalDataLastSevenDays = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHistory2Binding = ActivityHistory2Binding.inflate(getLayoutInflater());
        setContentView(activityHistory2Binding.getRoot());
        allocateActivityTitle("Weekly History");

        FirebaseApp.initializeApp(this);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        weekRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Taps").child("Tap1").child("Data");

        // Calculate the date seven days ago
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(calendar.getTime());
        TextView textViewDate = findViewById(R.id.textView7);
        textViewDate.setText(currentDate);

        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date sevenDaysAgo = calendar.getTime();
        weekRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Integer> dailyData = new ArrayList<>();

                // Iterate through each day in the month
                for (DataSnapshot monthSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot daySnapshot : monthSnapshot.getChildren()) {
                        String dateString = daySnapshot.getKey();

                        try {
                            Date date = dateFormat.parse(dateString);

                            // Check if the date is within the last seven days
                            if (date.after(sevenDaysAgo) || date.equals(sevenDaysAgo)) {
                                // Sum up the data for each hour of the day
                                int dailyTotal = 0;
                                // Sum up the data for each hour of the day
                                for (DataSnapshot hourSnapshot : daySnapshot.getChildren()) {
                                    int hourData = hourSnapshot.getValue(Integer.class);
                                    dailyTotal += hourData;
                                }
                                dailyData.add(dailyTotal);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                TextView totalWeek = findViewById(R.id.weektext);
                totalWeek.setText(String.valueOf(getTotalDataLastSevenDays(dailyData)));
                updateLineChart(dailyData);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Handle any errors
            }
        });
        mpLineChart = (LineChart) findViewById(R.id.lineChart2);
        setupLineChart();
    }
    private void setupLineChart() {
        mpLineChart.setBackgroundColor(Color.WHITE);
        mpLineChart.setDrawGridBackground(false);
        mpLineChart.setDrawBorders(true);
        mpLineChart.setBorderWidth(2);
        mpLineChart.setBorderColor(Color.BLUE);

        Legend legend = mpLineChart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(Color.BLUE);
        legend.setTextSize(12);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setFormSize(10);
        legend.setXEntrySpace(20);
        legend.setFormToTextSpace(10);

        LegendEntry[] legendEntries = new LegendEntry[3];
        for (int i=0; i<legendEntries.length; i++)
        {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colorClassArray[i];
            entry.label = String.valueOf(legendName[i]);
            legendEntries[i] = entry;
        }
        legend.setCustom(legendEntries);

        Description description = new Description();
        description.setText("This Week Water Consumption");
        description.setTextColor(Color.BLUE);
        description.setTextSize(10);
        mpLineChart.setDescription(description);

        mpLineChart.getAxisRight().setEnabled(false); // Disable the right Y-axis

        XAxis xAxis = mpLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(7); // Set the label count to 7 for 7 days
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, -6 + index); // Start from the first day (index 0)
                Date date = calendar.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd", Locale.US);
                return sdf.format(date);
            }
        });
    }
    private int getTotalDataLastSevenDays(ArrayList<Integer> dataValues) {
        int total = 0;
        for (Integer value : dataValues) {
            total += value;
        }
        return total;
    }
    private void updateLineChart(ArrayList<Integer> dataValues) {
        ArrayList<Entry> entries = new ArrayList<>();

        for (int i = 0; i < dataValues.size(); i++) {
            entries.add(new Entry(i, dataValues.get(i)));
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "Water Consumption");
        lineDataSet.setLineWidth(4);
        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setCircleColor(Color.BLUE);
        lineDataSet.setCircleHoleColor(Color.GRAY);
        lineDataSet.setCircleRadius(5);
        lineDataSet.setCircleHoleRadius(4);
        lineDataSet.setValueTextSize(15);
        lineDataSet.setValueTextColor(Color.BLUE);
        lineDataSet.enableDashedLine(5, 5, 0);

        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(Color.parseColor("#006DFF"));

        LineData data = new LineData(lineDataSet);
        mpLineChart.setData(data);
        mpLineChart.animateX(5000);
        mpLineChart.invalidate();
    }
}