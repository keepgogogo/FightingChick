package com.hackweek.fightingchick.reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.hackweek.fightingchick.ChronometerActivity;
import com.hackweek.fightingchick.MainActivity;
import com.hackweek.fightingchick.R;
import com.hackweek.fightingchick.database.FocusList;
import com.hackweek.fightingchick.database.FocusListDataBase;
import com.hackweek.fightingchick.toolpackage.ThreadHelper;

import java.util.List;
import java.util.Random;

public class ReminderActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView timeTo;
    private TextView justDoIt;
    private TextView later;
    private TextView otherStuff;
    private TextView lazy;
    private TextView gloriesConfessions;
    private FocusListDataBase focusListDataBase;
    private SharedPreferences reminderSp;
    private FocusList focusList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        focusListDataBase = FocusListDataBase.getDatabase(ReminderActivity.this);
        focusList=(FocusList)getIntent().getSerializableExtra(getString(R.string.focusList_to_reminder));
        timeTo = findViewById(R.id.reminder_timeto);

        timeTo.setText("该"+focusList.whatTodo+"啦！");
        justDoIt = findViewById(R.id.reminder_just_do_it);
        justDoIt.setOnClickListener(this);
        later=findViewById(R.id.reminder_later);
        later.setOnClickListener(this);
        otherStuff=findViewById(R.id.reminder_other_stuff);
        otherStuff.setOnClickListener(this);
        lazy=findViewById(R.id.reminder_lazy);
        lazy.setOnClickListener(this);
        gloriesConfessions=findViewById(R.id.reminder_glories_confessions);
        initGloriesConfessions();
    }

    private void initGloriesConfessions(){
        reminderSp = getSharedPreferences(getString(R.string.bigSp_key),MODE_PRIVATE);
        String toShowKey = "";
        //随机选择3个中的1个光荣/忏悔录展示
        Random random  = new Random();
        switch(random.nextInt(3)){
            case 0:
                toShowKey = getString(R.string.glories_confessions_to_show_1_key);
                break;
            case 1:
                toShowKey = getString(R.string.glories_confessions_to_show_2_key);
                break;
            case 2:
                toShowKey = getString(R.string.glories_confessions_to_show_3_key);
                break;
        }
        gloriesConfessions.setText(reminderSp.getString(toShowKey,"摆脱拖延，一起动起来"));
    }

    private void updateFocusList(){
        ThreadHelper threadHelper = new ThreadHelper();
        threadHelper.updateFocusList(focusListDataBase,focusList);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.reminder_just_do_it://来了就是干
                Toast.makeText(this, "动力值+5", Toast.LENGTH_SHORT).show();
                focusList.status=1;
                focusList.energyValue+=5;
                updateFocusList();
                int originalEnergyKey = reminderSp.getInt(getString(R.string.energy_key),0);
                reminderSp.edit().putInt(getString(R.string.energy_key),originalEnergyKey+2).apply();
                //如果要计时，跳转计时器
                if(focusList.FocusTime>=0){
                    Intent intentChrono = new Intent(ReminderActivity.this, ChronometerActivity.class);
                    intentChrono.putExtra(getString(R.string.focusList_to_chronometer),focusList);
                    startActivity(intentChrono);
                }else{
                    startMain();
                }
                break;
            case R.id.reminder_later://一会提醒 TODO
                reminderSp.edit().putBoolean(MainActivity.CHANGE_OCCURRED,true).apply();
                focusList.timeRung+=1;
                focusList.status=2;
                updateFocusList();
                startMain();
                break;
            case R.id.reminder_other_stuff:// 有事不完成，动力值-2
                Toast.makeText(this, "动力值-2", Toast.LENGTH_SHORT).show();
                focusList.status=3;
                focusList.energyValue-=2;
                updateFocusList();
                originalEnergyKey = reminderSp.getInt(getString(R.string.energy_key),0);
                reminderSp.edit().putInt(getString(R.string.energy_key),originalEnergyKey-2).apply();
                startMain();
                break;
            case R.id.reminder_lazy://懒了，动力值-5
                Toast.makeText(this, "动力值-5", Toast.LENGTH_SHORT).show();
                focusList.status=4;
                focusList.energyValue-=5;
                updateFocusList();
                originalEnergyKey = reminderSp.getInt(getString(R.string.energy_key),0);
                reminderSp.edit().putInt(getString(R.string.energy_key),originalEnergyKey-5).apply();
                startMain();
                break;
        }
    }

    private void startMain(){
        Intent intentMain = new Intent(ReminderActivity.this,MainActivity.class);
        startActivity(intentMain);
        finish();
    }

}