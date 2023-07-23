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
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);   // Enable offline persistence if needed

        DatabaseReference dayRef = FirebaseDatabase.getInstance().getReference().child("Taps").child("Tap1").child("Data");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(calendar.getTime());
        TextView textViewDate = findViewById(R.id.textView6);
        textViewDate.setText(currentDate);

        mpLineChart = (LineChart) findViewById(R.id.lineChart);
        LineDataSet lineDataSet1 = new LineDataSet(dataValues1(), "Tap 1");
        LineDataSet lineDataSet2 = new LineDataSet(dataValues2(), "Tap 2");
        LineDataSet lineDataSet3 = new LineDataSet(dataValues3(), "Tap 3");

        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);

        mpLineChart.setBackgroundColor(Color.WHITE);
        mpLineChart.setDrawGridBackground(true);
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
        lineDataSet1.setValueTextColor(Color.BLUE);
        lineDataSet1.enableDashedLine(5,10, 0);

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

        XAxis xAxis = mpLineChart.getXAxis();
        YAxis yAxisLeft = mpLineChart.getAxisLeft();
        YAxis yAxisRight = mpLineChart.getAxisRight();

        xAxis.setValueFormatter(new MyAxisValueFormatter());

        Description description = new Description();
        description.setText("Today's Water Consumption");
        description.setTextColor(Color.BLUE);
        description.setTextSize(15);
        mpLineChart.setDescription(description);

        LineData data = new LineData(lineDataSet1, lineDataSet2, lineDataSet3);
        mpLineChart.setData(data);
        mpLineChart.animateX(5000);
        mpLineChart.invalidate();

        dayRef.addValueEventListener(new ValueEventListener() {
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
                TextView totalDay = findViewById(R.id.daytext);
                totalDay.setText(String.valueOf(sum));
                Log.d("Home1Activity", "Total Sum: " + sum);
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error){
                // Handle the error
                Log.e("Home1Activity", "Database Error: " + error.getMessage());
            }
        });
    }

    private ArrayList<Entry> dataValues1() {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        dataVals.add(new Entry(0, 20));
        dataVals.add(new Entry(1, 24));
        dataVals.add(new Entry(2, 2));
        dataVals.add(new Entry(3, 10));
        dataVals.add(new Entry(4, 28));

        return dataVals;
    }

    private ArrayList<Entry> dataValues2() {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        dataVals.add(new Entry(1, 15));
        dataVals.add(new Entry(2, 20));
        dataVals.add(new Entry(3, 25));
        dataVals.add(new Entry(4, 1));
        dataVals.add(new Entry(5, 30));

        return dataVals;
    }

    private ArrayList<Entry> dataValues3() {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        dataVals.add(new Entry(2, 10));
        dataVals.add(new Entry(3, 15));
        dataVals.add(new Entry(4, 5));
        dataVals.add(new Entry(5, 20));
        dataVals.add(new Entry(6, 13));

        return dataVals;
    }
    private static class MyAxisValueFormatter extends ValueFormatter implements IAxisValueFormatter{

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return "Hour "+value;
        }
    }
}