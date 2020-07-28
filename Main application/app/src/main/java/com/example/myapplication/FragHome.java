package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.dinuscxj.progressbar.CircleProgressBar;

public class FragHome extends Fragment {
    private View view;

    private Fragment fragFirst;
    private Fragment fragSecond;

    private ViewPager pager;

    CircleProgressBar graph;
    Button next;
    Button previous;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater Inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = Inflater.inflate(R.layout.frag_home, container, false);

        graph = (CircleProgressBar)view.findViewById(R.id._graph);
        graph.setProgress(0);

        next = (Button) view.findViewById(R.id.but_next);
        previous = (Button) view.findViewById(R.id.but_pre);

        fragFirst = new Frag_First();
        fragSecond = new Frag_Second();

        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(new PagerAdapter(getChildFragmentManager()));
        pager.setCurrentItem(0);

        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                pager.setCurrentItem(1);
            }
        });

        previous.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                pager.setCurrentItem(0);
            }
        });


        return view;
    }

    private class PagerAdapter extends FragmentPagerAdapter  {
        public PagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position){
            if(position == 0){
                return fragFirst;
            }
            else if(position == 1){
                return fragSecond;
            }

            return null;
        }

        @Override
        public int getCount(){
            return 2;
        }
    }


}
