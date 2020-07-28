package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Frag_Second extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater Inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = Inflater.inflate(R.layout.second_study, container, false);

        return view;
    }
}
