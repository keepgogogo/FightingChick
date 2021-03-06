package com.hackweek.fightingchick;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapProgressBar;


public class MyChickFragment extends Fragment implements View.OnClickListener{

    private int energyValue;
    private int energyNeedForUpgrade;
    private TextView myChickName;
    private BootstrapProgressBar upgradeProgressBar;
    private TextView energyValueView;
    private HorizontalScrollView scrollView;
    private LinearLayout[] chicks=new LinearLayout[7];
    private SharedPreferences myChickSp;

    private ImageButton nextChick;
    private ImageButton lastChick;
    private int currentChick;//0~6
    private int mychick;
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
        scrollView=(HorizontalScrollView)view.findViewById(R.id.my_chick_profiles);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        chicks[0]=(LinearLayout)view.findViewById(R.id.my_chick_profile_tuolaji);
        chicks[1]=(LinearLayout)view.findViewById(R.id.my_chick_profile_luotangji);
        chicks[2]=(LinearLayout)view.findViewById(R.id.my_chick_profile_baiqieji);
        chicks[3]=(LinearLayout) view.findViewById(R.id.my_chick_profile_paobuji);
        chicks[4]=(LinearLayout)view.findViewById(R.id.my_chick_profile_huaxiangji);
        chicks[5]=(LinearLayout)view.findViewById(R.id.my_chick_profile_zhandouji);
        chicks[6]=(LinearLayout)view.findViewById(R.id.my_chick_profile_yongdongji);
        lastChick=(ImageButton)view.findViewById(R.id.my_chick_last_chick);
        nextChick=(ImageButton)view.findViewById(R.id.my_chick_next_chick);
        lastChick.setOnClickListener(this);
        nextChick.setOnClickListener(this);
        upgradeProgressBar = (BootstrapProgressBar)view.findViewById(R.id.upgrade_progress_bar);
        //upgradeProgressBar.setScaleY(3f);
        energyValueView = (TextView)view.findViewById(R.id.energy_value);
        myChickSp = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences bigSp = getActivity().getSharedPreferences(getString(R.string.bigSp_key),Context.MODE_PRIVATE);
        energyValue = bigSp.getInt(getString(R.string.energy_key),0);
        energyValueView.setText("????????????"+ energyValue);
        setMyChick();
    }

    // calculate the progress(0-100) according to the upper and lower bounds
    private int calculateProgress(int min, int max){
        return (energyValue-min)*100/(max-min);
    }

    private void scrollToChick(int num){
        if(num>=0 && num<7){
            Handler handler =new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    int x=0;
                    if(num>0)
                        x=chicks[num].getLeft()-250;
                    scrollView.smoothScrollTo(x,0);
                }
            });
        }
    }
    private void hideLeft(){ lastChick.setVisibility(View.GONE);lastChick.setClickable(false);}
    private void hideRight(){ nextChick.setVisibility(View.GONE);nextChick.setClickable(false);}
    private void showLeft(){ lastChick.setVisibility(View.VISIBLE);lastChick.setClickable(true);}
    private void showRight(){nextChick.setVisibility(View.VISIBLE);nextChick.setClickable(true);}

    // initialize the name, image and progress bar according to current energy value
    private void setMyChick(){
        if(energyValue<-50){//?????????
            hideLeft();
            currentChick=0;
            mychick=0;
            energyNeedForUpgrade=-50-energyValue;
            if(energyValue>-100){
                upgradeProgressBar.setProgress(calculateProgress(-100,-50));
            }else upgradeProgressBar.setProgress(0);
        }else if(energyValue<0){//?????????
            hideLeft();
            currentChick=1;
            mychick=1;
            energyNeedForUpgrade=-energyValue;
            upgradeProgressBar.setProgress(calculateProgress(-50,0));
        }else if(energyValue<20){//?????????
            hideLeft();
            currentChick=2;
            mychick=2;
            energyNeedForUpgrade=20-energyValue;
            upgradeProgressBar.setProgress(calculateProgress(0,20));
        }else if(energyValue<50){//?????????
            currentChick=3;
            mychick=3;
            energyNeedForUpgrade=50-energyValue;
            upgradeProgressBar.setProgress(calculateProgress(20,50));
        }else if(energyValue<80){//?????????
            currentChick=4;
            mychick=4;
            energyNeedForUpgrade=80-energyValue;
            upgradeProgressBar.setProgress(calculateProgress(50,80));
        }else if(energyValue<100){//?????????
            currentChick=5;
            mychick=5;
            energyNeedForUpgrade=100-energyValue;
            upgradeProgressBar.setProgress(calculateProgress(80,100));
        }else{//?????????
            hideRight();
            currentChick=6;
            mychick=6;
        }
        scrollToChick(currentChick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.my_chick_last_chick:
                showRight();
                if(currentChick==mychick+1){
                    hideLeft();
                    currentChick-=1;
                    scrollToChick(currentChick);
                }else{
                    currentChick-=1;
                    scrollToChick(currentChick);
                }
                break;
            case R.id.my_chick_next_chick:
                if(currentChick==mychick){
                    Toast.makeText(getContext(), "??????"+energyNeedForUpgrade+"???????????????", Toast.LENGTH_SHORT).show();
                    break;
                }
                showLeft();
                if(currentChick<6){
                    currentChick+=1;
                    scrollToChick(currentChick);
                }
        }
    }
}