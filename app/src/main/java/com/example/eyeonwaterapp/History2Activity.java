package com.example.eyeonwaterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.example.eyeonwaterapp.databinding.ActivityHistory2Binding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class History2Activity extends DrawerBaseActivity {

    LineChart mpLineChart;
    ActivityHistory2Binding activityHistory2Binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHistory2Binding = ActivityHistory2Binding.inflate(getLayoutInflater());
        setContentView(activityHistory2Binding.getRoot());
        allocateActivityTitle("Weekly History");

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        TextView textViewDate = findViewById(R.id.textView7);
        textViewDate.setText(currentDate);

        mpLineChart = (LineChart) findViewById(R.id.lineChart1);
        LineDataSet lineDataSet1 = new LineDataSet(dataValues1(), "Tap 1");
        LineDataSet lineDataSet2 = new LineDataSet(dataValues2(), "Tap 2");
        LineDataSet lineDataSet3 = new LineDataSet(dataValues3(), "Tap 3");
        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);
        dataSets.add(lineDataSet2);
        dataSets.add(lineDataSet3);

        mpLineChart.setBackgroundColor(Color.WHITE);

        LineData data = new LineData(lineDataSet1, lineDataSet2, lineDataSet3);
        mpLineChart.setData(data);
        mpLineChart.invalidate();
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
}