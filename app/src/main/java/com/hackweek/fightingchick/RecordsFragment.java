package com.hackweek.fightingchick;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;



import java.text.SimpleDateFormat;
import java.util.Date;


public class RecordsFragment extends Fragment {

    RecordsAdapter recordsAdapter;
    ViewPager2 viewPager;
    private TextView top_records_nickname;
    private TextView top_records_date;
    private TextView top_records_resolutions;
    private final String[] tabLabels = {"我的小鸡","活动统计","忏悔录&光荣录"};

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

        // init top view
        top_records_date = (TextView)view.findViewById(R.id.top_date);
        top_records_nickname = (TextView)view.findViewById(R.id.top_nickname);
        top_records_resolutions = (TextView)view.findViewById(R.id.top_resolutions);

        SharedPreferences RecordsSp = this.getActivity().getPreferences(Context.MODE_PRIVATE);

        top_records_nickname.setText(RecordsSp.getString(getString(R.string.nickname_key),"昵称"));
        top_records_resolutions.setText(RecordsSp.getString(getString(R.string.resolutions_key),"永动宣言"));
        // set date
        Date today = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("MM'月'dd'日'");
        top_records_date.setText(ft.format(today));
    }
}



