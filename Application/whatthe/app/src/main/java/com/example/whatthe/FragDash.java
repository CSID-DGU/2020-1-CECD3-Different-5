package com.example.whatthe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class FragDash extends Fragment {
    private View view;

    private LineChart lineChart;
    ArrayList<Entry> entry_chart = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater Inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = Inflater.inflate(R.layout.frag_dashboard, container, false);

        lineChart = (LineChart) view.findViewById(R.id.chart);//layout의 id

        LineData chartData = new LineData();

        entry_chart.add(new Entry(2,3));
        LineDataSet lineDataSet = new LineDataSet(entry_chart,"그래프");
        chartData.addDataSet(lineDataSet);

        lineChart.setData(chartData);
        lineChart.invalidate();

        return view;
    }
}
