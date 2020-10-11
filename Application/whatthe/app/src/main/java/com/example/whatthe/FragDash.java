package com.example.whatthe;

import android.graphics.Color;
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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class FragDash extends Fragment {
    private View view;

    private LineChart chart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater Inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = Inflater.inflate(R.layout.frag_dashboard, container, false);

        chart = (LineChart) view.findViewById(R.id.Lchart);//layout의 id

        ArrayList<Entry> values = new ArrayList<>();

        values.add(new Entry(100.0f, 0));
        values.add(new Entry(50.0f, 0));
        values.add(new Entry(25.0f, 0));

        LineDataSet set1 = new LineDataSet(values, "DataSet 1");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("1.0");
        xVals.add("2.0");
        xVals.add("3.0");

        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);
        chart.invalidate();

        return view;
    }
}
