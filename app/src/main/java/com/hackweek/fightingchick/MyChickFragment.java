package com.hackweek.fightingchick;

import android.content.Context;
import android.content.SharedPreferences;
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

    private int energyValue;
    private TextView myChickName;
    private ImageView myChickProfile;
    private ProgressBar upgradeProgressBar;
    private TextView energyValueView;

    private SharedPreferences myChickSp;
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
        myChickProfile = view.findViewById(R.id.my_chick_profile);
        myChickName = view.findViewById(R.id.my_chick_name);
        upgradeProgressBar = view.findViewById(R.id.upgrade_progress_bar);
        energyValueView = view.findViewById(R.id.energy_value);
        myChickSp = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences bigSp = getActivity().getSharedPreferences(getString(R.string.bigSp_key),Context.MODE_PRIVATE);
        energyValue = bigSp.getInt(getString(R.string.energy_key),0);
        energyValueView.setText("动力值："+ energyValue);
        setMyChick();
    }

    // calculate the progress(0-100) according to the upper and lower bounds
    private int calculateProgress(int min, int max){
        return (energyValue-min)*100/(max-min);
    }

    // initialize the name, image and progress bar according to current energy value
    private void setMyChick(){
        if(energyValue<-50){//拖拉鸡
            myChickName.setText("拖拉鸡");
            myChickProfile.setImageResource(R.mipmap.tuolaji);
            if(energyValue>-100){
                upgradeProgressBar.setProgress(calculateProgress(-100,-50));
            }else upgradeProgressBar.setProgress(0);
        }else if(energyValue<0){//落汤鸡
            myChickName.setText("落汤鸡");
            myChickProfile.setImageResource(R.mipmap.luotangji);
            upgradeProgressBar.setProgress(calculateProgress(-50,0));
        }else if(energyValue<20){//白切鸡
            myChickName.setText("白切鸡");
            myChickProfile.setImageResource(R.mipmap.baiqieji);
            upgradeProgressBar.setProgress(calculateProgress(0,20));
        }else if(energyValue<50){//跑步鸡
            myChickName.setText("跑步鸡");
            myChickProfile.setImageResource(R.mipmap.paobuji);
            upgradeProgressBar.setProgress(calculateProgress(20,50));
        }else if(energyValue<80){//滑翔鸡
            myChickName.setText("滑翔鸡");
            myChickProfile.setImageResource(R.mipmap.huaxiangji);
            upgradeProgressBar.setProgress(calculateProgress(50,80));
        }else if(energyValue<100){//战斗鸡
            myChickName.setText("战斗鸡");
            myChickProfile.setImageResource(R.mipmap.zhandouji);
            upgradeProgressBar.setProgress(calculateProgress(80,100));
        }else{//永动鸡
            myChickName.setText("永动鸡");
            myChickProfile.setImageResource(R.mipmap.yongdongji);
        }
    }
}