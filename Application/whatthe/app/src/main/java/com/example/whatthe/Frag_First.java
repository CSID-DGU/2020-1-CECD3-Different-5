package com.example.whatthe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class Frag_First extends Fragment {
    private View view;

    HorizontalBarChart cctchart, emochart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater Inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = Inflater.inflate(R.layout.first_study, container, false);
//
        cctchart = (HorizontalBarChart) view.findViewById(R.id.concentrationChart);

        ArrayList<BarEntry> cct = new ArrayList<BarEntry>();
        cct.add(new BarEntry(new float[] {8f,12f,1f},0));

        int UnCct = 0xFFC1E6FF, Cct = 0xFF508BE0;
        int[] color = new int[]{ Cct, UnCct, Cct};

        ArrayList year = new ArrayList();
        year.add("2008");


        BarDataSet bardataset = new BarDataSet(cct, "");
        cctchart.animateX(5000);
        BarData data = new BarData(year, bardataset);
        data.setDrawValues(false);
        bardataset.setColors(color);
        cctchart.setData(data);

        cctchart.setDescription("");
        cctchart.getAxisRight().setDrawLabels(false);
        cctchart.getXAxis().setDrawLabels(false);
        cctchart.getLegend().setEnabled(false);
//
        emochart = (HorizontalBarChart) view.findViewById(R.id.emotionChart);

        ArrayList<BarEntry> emo = new ArrayList<BarEntry>();
        emo.add(new BarEntry(new float[] {7f,3f,3f,6f,2f},0));

        int angry = 0xFFE77474, peace = 0xFFB4B4B4, joy = 0xFFF7CD7C;

        int[] colore = new int[]{ peace, angry, joy, peace, angry};

        ArrayList yeare = new ArrayList();
        yeare.add("2008");


        BarDataSet bardatasete = new BarDataSet(emo, "");
        emochart.animateX(5000);
        BarData datae = new BarData(yeare, bardatasete);
        datae.setDrawValues(false);
        bardatasete.setColors(colore);
        emochart.setData(datae);

        emochart.setDescription("");
        emochart.getAxisRight().setDrawLabels(false);
        emochart.getXAxis().setDrawLabels(false);
        emochart.getLegend().setEnabled(false);



        return view;
    }
}
