package com.hackweek.fightingchick;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MyChickFragment extends Fragment {


    public MyChickFragment() {
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
        return inflater.inflate(R.layout.fragment_my_chick, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView myChickProfile = view.findViewById(R.id.my_chick_profile);
        TextView myChickName = view.findViewById(R.id.my_chick_name);
        ProgressBar upgradeProgressBar = view.findViewById(R.id.upgrade_progress_bar);
        TextView energyValue = view.findViewById(R.id.energy_value);
        //myChickName.setText();
        //upgradeProgressBar.setProgress();
        //energyValue.setText();
    }
}