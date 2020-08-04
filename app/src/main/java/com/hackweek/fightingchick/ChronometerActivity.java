package com.hackweek.fightingchick;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.hackweek.fightingchick.database.FocusList;
import com.hackweek.fightingchick.database.FocusListDataBase;
import com.hackweek.fightingchick.toolpackage.ThreadHelper;

public class ChronometerActivity extends AppCompatActivity implements View.OnClickListener {
    private long timeWhenStopped = 0; // Minus milliseconds of total recorded time
    private int timeWhenStoppedMin = 0;
    private boolean isCounting = false;
    private int focusHour;
    private int focusMinute;
    private Chronometer mChronometer;
    private Button buttonBackChronometer;
    private TextView chronometerTaskName;
    private TextView chronometerTaskPassedTime;
    private Button startChronometer;
    private Button pauseChronometer;
    private FocusList mFocusList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronometer);
        mFocusList = (FocusList) getIntent().getSerializableExtra("focuslist_chronometer");//请传入Intent的时候调用
        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        mChronometer.setFormat("计时：%s");
        buttonBackChronometer = (Button) findViewById(R.id.button_back_chronometer);
        startChronometer = (Button) findViewById(R.id.button_start_chronometer);
        pauseChronometer = (Button) findViewById(R.id.button_pause_chronometer);
        chronometerTaskName = (TextView) findViewById(R.id.chronometer_task_name);
        chronometerTaskName.setText(mFocusList.whatTodo);
        chronometerTaskPassedTime = (TextView) findViewById(R.id.chronometer_task_passed_time);
        chronometerTaskPassedTime.setText("已进行" + formatFocusHourAndMinute(mFocusList));
        buttonBackChronometer.setOnClickListener(this);
        pauseChronometer.setOnClickListener(this);
        startChronometer.setOnClickListener(this);

    }

    private void showPauseDialog() {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle(chronometerTaskPassedTime.getText().toString());
        // add a list
        String[] chronometerChoices = {"今天就到这了，收工！（停止计时）",
                "再好好想十秒，我还能继续干下去！（继续计时）",
                "突然有些事，要打断一下（暂停计时）"};
        builder.setItems(chronometerChoices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: //stop
                        stopChro();
                        backToMain();
                        break;
                    case 1: // continue
                        startChro();
                        break;
                    case 2: // pause
                        pauseChro();
                        showRealPauseDialog();
                        break;
                }
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //展示用户选择了暂停->暂停之后的界面
    private void showRealPauseDialog(){
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle(chronometerTaskPassedTime.getText().toString());
        // add a list
        String[] chronometerChoices = {"继续（继续计时）",
                "停止（停止计时）",};
        builder.setItems(chronometerChoices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: //continue
                        startChro();
                        break;
                    case 1: // stop
                        stopChro();
                        backToMain();
                        break;
                }
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_back_chronometer:
                if (isCounting)
                    stopChro();
                backToMain();
                break;
            case R.id.button_pause_chronometer:
                showPauseDialog();
                break;
            case R.id.button_start_chronometer:
                startChro();
                break;
        }
    }

    //更新数据库里相应的FocusTime和动力值
    private void saveTimeToRoom(int deltaMinute) {
        if (deltaMinute != 0) {
            mFocusList.FocusTime += deltaMinute;
            mFocusList.energyValue += 4*(deltaMinute/20); //满20分钟动力值加4
            FocusListDataBase focusListDataBase = FocusListDataBase.getDatabase(getApplicationContext());
            ThreadHelper mThreadHelper = new ThreadHelper();
            mThreadHelper.updateFocusList(focusListDataBase, mFocusList);
        }
    }




    private void startChro() {
        mChronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        mChronometer.start();
        isCounting = true;
    }

    // save time and exit activity
    private void backToMain() {
        saveTimeToRoom(timeWhenStoppedMin);
        Intent intent = new Intent(ChronometerActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void stopChro() {
        timeWhenStopped = mChronometer.getBase() - SystemClock.elapsedRealtime();
        timeWhenStopped = -timeWhenStopped;
        // 此时timeWhenStopped记录需要保存的毫秒数
        timeWhenStoppedMin = (int) timeWhenStopped / (1000 * 60);
        mChronometer.stop();
        isCounting = false;

    }

    private void pauseChro() {
        timeWhenStopped = mChronometer.getBase() - SystemClock.elapsedRealtime();
        mChronometer.stop();
        isCounting = false;
    }

    //根据专注时长(min)计算出对应的小时和分钟,以"xx时xx分"的字符串返回
    private String formatFocusHourAndMinute(FocusList focusList) {
        StringBuilder stringBuilder = new StringBuilder("时分");
        focusHour = focusList.FocusTime / 60;
        focusMinute = focusList.FocusTime % 60;
        stringBuilder.insert(0, focusHour);
        stringBuilder.insert(2, focusMinute);
        return stringBuilder.toString();
    }
}