package com.hackweek.fightingchick;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class RecordsFragment extends Fragment {

    RecordsAdapter recordsAdapter;
    ViewPager2 viewPager;
    private final String[] tabLabels = {"我的小鸡","活动统计","光荣录&忏悔录"};

    public RecordsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_records, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recordsAdapter = new RecordsAdapter(this);
        viewPager = view.findViewById(R.id.records_pager);
        viewPager.setAdapter(recordsAdapter);
        TabLayout tabLayout = view.findViewById(R.id.records_tab_layout);
        new TabLayoutMediator(tabLayout,viewPager,
                ((tab, position) -> tab.setText(tabLabels[position]))).attach();
    }
}



