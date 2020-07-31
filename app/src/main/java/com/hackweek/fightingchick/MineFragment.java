package com.hackweek.fightingchick;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MineFragment extends Fragment {

    private Button aboutUs;

    public MineFragment() {
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
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        aboutUs = (Button)view.findViewById(R.id.ButtonForWatchAboutUsInMineFragment);
        aboutUs.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity)getActivity();
                Fragment fragment = new TodoListFragment();
                activity.setFragment(fragment);
            }
        });
    }
}