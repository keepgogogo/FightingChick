package com.hackweek.fightingchick;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class AboutUsFragment extends Fragment implements View.OnClickListener {

    private MainActivity mainActivity;
    private Button buttonForGetBack;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        mainActivity=(MainActivity)getActivity();
        buttonForGetBack=view.findViewById(R.id.ButtonForGetBackInAboutUsFragment);
        buttonForGetBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ButtonForGetBackInAboutUsFragment:
                mainActivity.setFragment(new MineFragment());
                break;
            default:
                break;
        }
    }


    public AboutUsFragment() {
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
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }
}